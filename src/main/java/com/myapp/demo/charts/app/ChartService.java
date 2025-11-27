// -----------------------------------------------------------------------------
//  HIPAA COMPLIANCE NOTICE:
//  This service handles PHI (Protected Health Information).
//  Do not log, export, or comment any chart-identifying data.
//

package com.myapp.demo.charts.app;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.myapp.demo.charts.infra.repository.ChartClinicalRepository;
import com.myapp.demo.charts.infra.repository.ChartConsentRepository;
import com.myapp.demo.charts.infra.repository.ChartCoreRepository;
import com.myapp.demo.charts.infra.repository.ChartIdentityRepository;
import com.myapp.demo.charts.infra.repository.ChartInsuranceRepository;
import com.myapp.demo.charts.api.dto.ChartDto;
import com.myapp.demo.charts.app.interfaces.IChartService;
import com.myapp.demo.entity.Chart;
import com.myapp.demo.charts.domain.ChartClinical;
import com.myapp.demo.charts.domain.ChartConsent;
import com.myapp.demo.charts.domain.ChartCore;
import com.myapp.demo.charts.domain.ChartIdentity;
import com.myapp.demo.charts.domain.ChartInsurance;
import com.myapp.demo.charts.api.ChartEventsController;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChartService implements IChartService {

	List<Chart> chart;

	ChartEventsController sse;
	private final ChartCoreRepository coreRepo;
	private final ChartIdentityRepository idRepo;
	private final ChartClinicalRepository clinicalRepo;
	private final ChartInsuranceRepository insRepo;
	private final ChartConsentRepository consentRepo;

	@Autowired
	public ChartService(ChartCoreRepository coreRepo, ChartIdentityRepository idRepo,
			ChartClinicalRepository clinicalRepo, ChartInsuranceRepository insRepo,
			ChartConsentRepository consentRepo, ChartEventsController sse) {
		this.coreRepo = coreRepo;
		this.idRepo = idRepo;
		this.clinicalRepo = clinicalRepo;
		this.insRepo = insRepo;
		this.consentRepo = consentRepo;
		this.sse = sse;
	}

	private ChartDto toDto(ChartCore core) {
		var i = core.getIdentity();
		var cl = core.getClinical();
		var ins = core.getInsurance();
		var cs = core.getConsent();

		return new ChartDto(core.getId(), core.getIdCabinet(), core.getIdProvider(), core.getClientUuid(),

				// identity
				i != null ? i.getFirstName() : null, i != null ? i.getMiddleName() : null,
				i != null ? i.getLastName() : null, i != null ? i.getPreferredName() : null,
				i != null ? i.getGender() : null, i != null ? i.getBirthday() : null,

				// photo
				i != null ? i.getPhotoUrl() : null,

				// contact
				i != null ? i.getPhoneMobile() : null, i != null ? i.getPhoneHome() : null,
				i != null ? i.getEmail() : null, i != null ? i.getAddressLine1() : null,
				i != null ? i.getAddressLine2() : null, i != null ? i.getCity() : null, i != null ? i.getState() : null,
				i != null ? i.getZipCode() : null,

				// relation
				i != null ? i.getMaritalStatus() : null, i != null ? i.getEmergencyContactName() : null,
				i != null ? i.getEmergencyContactPhone() : null, i != null ? i.getEmergencyContactRelation() : null,

				// portal info
				i != null ? i.getHasPortalAccount() : null, i != null ? i.getPortalUsername() : null,
				i != null ? i.getLastPortalLogin() : null,

				// assurance
				ins != null ? ins.getProvider() : null, ins != null ? ins.getMemberId() : null,
				ins != null ? ins.getPlanName() : null,
				(ins != null && ins.getExpirationDate() != null) ? ins.getExpirationDate() : null,

				// consentements
				cs != null ? cs.getHipaaConsentSigned() : null, cs != null ? cs.getHipaaConsentAt() : null,
				cs != null ? cs.getTelehealthConsentSigned() : null, cs != null ? cs.getDataSharingConsent() : null,
				cs != null ? cs.getTelehealthConsentAt() : null,
				// clinique
				cl != null ? cl.getBloodType() : null, cl != null ? cl.getHeightCm() : null,
				cl != null ? cl.getWeightKg() : null, cl != null ? cl.getSmokingStatus() : null,
				cl != null ? cl.getAlcoholUse() : null,

				// SDOH
				cl != null ? cl.getOccupation() : null, cl != null ? cl.getEmployer() : null,
				cl != null ? cl.getHousingStatus() : null, cl != null ? cl.getFinancialStrain() : null,
				cl != null ? cl.getTransportationAccess() : null,

				// allergies / conditions / meds
				cl != null ? cl.getAllergies() : null, cl != null ? cl.getChronicConditions() : null,
				cl != null ? cl.getMedications() : null,

				// last visits
				core.getLastEncounterAt(), core.getNextAppointmentAt(), core.getVersion(),core.getUpdatedAt());
	}

	/** charge l’aggregate complet en 1 requête (via @EntityGraph dans le repo) */
	private Optional<ChartCore> loadAggregate(Long id) {
		return coreRepo.findWithAllById(id);
	}

	/* ===================== CRUD & queries en DTO ===================== */

	@Transactional(readOnly = true)
	@Override
	public List<ChartDto> getCharts() {
		// si tu as un @EntityGraph findAllWithAll(), utilise-le; sinon mappe simple:
		return coreRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<ChartDto> getChartById(Long id) {
		return loadAggregate(id).map(this::toDto);
	}

	

	@Transactional(readOnly = true)
	public List<ChartDto> findAllChartByIdCabinet(Long idCabinet) {
		var cores = coreRepo.findAllByIdCabinet(idCabinet);
		return cores.stream().map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	public List<ChartDto> findAllChartByIdProvider(Long idProvider) {
		var cores = coreRepo.findAllByIdProvider(idProvider);
		return cores.stream().map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<ChartDto> findAllChartByFilter(Pageable pageable) {
		Page<ChartCore> page = coreRepo.findAll(pageable); 

		List<ChartDto> dtos = page.getContent().stream().peek(pc -> System.out.println("core=" + pc.getId()))
				.map(this::toDto).peek(dto -> System.out.println("dto.id=" + dto.id())).toList();

		return new PageImpl<>(dtos, pageable, page.getTotalElements());
	}

	@Transactional(readOnly = true)
	@Override
	public List<ChartDto> findAllChartByFirstName(String firstname) {
		var ids = idRepo.findByFirstNameStartingWith(firstname);
		return ids.stream().map(pi -> loadAggregate(pi.getId()).orElse(pi.getCore())).map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<ChartDto> getChartsStartingWith(String firstname, Pageable pageable) {

		var all = idRepo.findByFirstNameStartingWith(firstname);
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), all.size());
		List<ChartDto> slice = (start >= all.size() ? List.<ChartDto>of()
				: all.subList(start, end).stream().map(pi -> loadAggregate(pi.getId()).orElse(pi.getCore()))
						.map(this::toDto).toList());
		return new PageImpl<>(slice, pageable, all.size());
	}

//	@Override
//	public Page<Chart> getChartsStartingWith2(String firstname, Pageable pageable) {
//		// Fetch the complete list of charts (or from another service)
//		List<Chart> charts = chartRepo.findByFirstNameStartingWith(firstname);
//
//		// Create a sublist for the given page
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		Sort sort = pageable.getSort();
//		int startItem = currentPage * pageSize;
//		List<Chart> list;
//
//		if (charts.size() < startItem) {
//			list = List.of(); // Return an empty list if startItem is beyond list size
//		} else {
//			int toIndex = Math.min(startItem + pageSize, charts.size());
//			list = charts.subList(startItem, toIndex);
//		}
//
//		// Create the Page object
//		Pageable sortAndFilter = PageRequest.of(currentPage, pageSize, sort);
//		return new PageImpl<>(list, sortAndFilter, charts.size());
//	}

	@Transactional(readOnly = true)
	@Override
	public Page<ChartDto> getChartsByFirstNameContaining(String firstname, Pageable pageable) {
		var all = idRepo.findByFirstNameContaining(firstname);
		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), all.size());
		List<ChartDto> slice = (start >= all.size() ? List.<ChartDto>of()
				: all.subList(start, end).stream().map(pi -> loadAggregate(pi.getId()).orElse(pi.getCore()))
						.map(this::toDto).toList());
		return new PageImpl<>(slice, pageable, all.size());
	}

	/**
	 * mixte search between firstname and lastname.
	 *
	 * @param chaine     de caractére
	 * @param pagination
	 * @return list of charts
	 */

	@Transactional(readOnly = true)
	@Override
	public Page<ChartDto> getChartsByFirstNameAndLastNameContaining(String firstname, String lastname,
			Pageable pageable, String sort, String filter) {

		// stratégie simple : union puis intersection sur les IDs
		Set<Long> byFirst = idRepo.findByFirstNameContaining(firstname).stream().map(ChartIdentity::getId)
				.collect(Collectors.toSet());
		Set<Long> byLast = idRepo.findByLastNameContaining(lastname).stream().map(ChartIdentity::getId)
				.collect(Collectors.toSet());

		Set<Long> ids;
		if (lastname != null && !lastname.isBlank()) {
			ids = new HashSet<>(byFirst);
			ids.retainAll(byLast); // intersection
		} else {
			ids = byFirst;
		}

		// chargement, mapping, tri basique
		List<ChartDto> all = ids.stream().map(this::getChartById) // Optional<ChartDto>
				.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

		Comparator<ChartDto> cmp = Comparator.comparing(ChartDto::id, Comparator.nullsLast(Long::compareTo));
		if ("FirstName".equalsIgnoreCase(filter))
			cmp = Comparator.comparing(ChartDto::firstName, Comparator.nullsLast(String::compareToIgnoreCase));
		if ("Birthday".equalsIgnoreCase(filter))
			cmp = Comparator.comparing(ChartDto::birthday, Comparator.nullsLast(LocalDate::compareTo));
		if ("City".equalsIgnoreCase(filter))
			cmp = Comparator.comparing(ChartDto::city, Comparator.nullsLast(String::compareToIgnoreCase));
		if ("Id".equalsIgnoreCase(filter))
			cmp = Comparator.comparing(ChartDto::id, Comparator.nullsLast(Long::compareTo));
		if ("DESC".equalsIgnoreCase(sort))
			cmp = cmp.reversed();

		all.sort(cmp);

		int start = (int) pageable.getOffset();
		int end = Math.min(start + pageable.getPageSize(), all.size());
		List<ChartDto> slice = (start >= all.size()) ? List.of() : all.subList(start, end);
		return new PageImpl<>(slice, pageable, all.size());
	}

//	@Override
//	public Page<Chart> getChartsByFirstNameAndLastNameContaining2(String firstname, String lastname,
//			Pageable pageable, String sort, String filter) {
//		// Fetch the complete list of charts (or from another service)
//		Set<Chart> set, set2;
//		if (!(lastname.length() > 0)) {
//			List<Chart> chartsByFirstName = chartRepo.findByFirstNameContainingAndLastNameContaining(firstname,
//					lastname);
//			List<Chart> chartsByLastName = chartRepo.findByFirstNameContainingAndLastNameContaining(lastname,
//					firstname);
//			set = new HashSet<>(chartsByFirstName);
//			set.addAll(chartsByLastName);
//		} else {
//			List<Chart> chartsByFirstName = chartRepo.findByFirstName(firstname);
//			List<Chart> chartsByLastName = chartRepo.findByLastName(firstname);
//
//			List<Chart> chartsByFirstName2 = chartRepo.findByFirstNameStartingWith(lastname);
//			List<Chart> chartsByLastName2 = chartRepo.findByLastNameStartingWith(lastname);
//
//			set = new HashSet<>(chartsByFirstName);
//			set.addAll(chartsByLastName);
//
//			set2 = new HashSet<>(chartsByFirstName2);
//			set2.addAll(chartsByLastName2);
//
//			set.retainAll(set2);
//		}
//
//		// Convertir l'ensemble en une liste
//		List<Chart> charts = new ArrayList<>(set);
//		List<Chart> sortedList=null;
//		// Sort the list by Chart ID
//		switch (filter) {
//		case "FirstName": {
//
//			sortedList = charts.stream().sorted(Comparator.comparing(Chart::getFirstName))
//					.collect(Collectors.toList());
//		}
//		case "Birthday": {
//
//			sortedList = charts.stream().sorted(Comparator.comparing(Chart::getBirthday))
//					.collect(Collectors.toList());
//		}
//		case "City": {
//
//			sortedList = charts.stream().sorted(Comparator.comparing(Chart::getCity))
//					.collect(Collectors.toList());
//		}
//		case "Id": {
//
//			sortedList = charts.stream().sorted(Comparator.comparing(Chart::getId))
//					.collect(Collectors.toList());
//		}}
//		
//		
//		if (sort.equals("DESC")) {
//			//sortedList = sortedList.reversed();
//		}
//
//		// Create a sublist for the given page
//		int pageSize = pageable.getPageSize();
//		int currentPage = pageable.getPageNumber();
//		int startItem = currentPage * pageSize;
//		List<Chart> pageList;
//
//		if (sortedList.size() < startItem) {
//			pageList = List.of(); // Return an empty list if startItem is beyond list size
//		} else {
//			int toIndex = Math.min(startItem + pageSize, sortedList.size());
//			pageList = sortedList.subList(startItem, toIndex);
//		}
//
//		// Create the Page object
//		return new PageImpl<>(pageList, PageRequest.of(currentPage, pageSize), sortedList.size());
//
//	}

	/* ===================== CREATE / UPDATE ===================== */

	@Override
	public ChartDto add(ChartDto dto) {
		var core = createOrGetCore(dto);
		upsertIdentity(core, dto);
		upsertClinical(core, dto);
		upsertInsurance(core, dto);
		upsertConsent(core, dto);
		if (sse != null)
			sse.notifyChartsChanged();
		return toDto(coreRepo.findWithAllById(core.getId()).orElse(core));
	}

	@Override
	public ChartDto update(ChartDto dto) {
		if (dto.id() == null)
			throw new IllegalArgumentException("id is required for update");
		var core = coreRepo.findById(dto.id()).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Chart id="+dto.id()+" not found"));
		upsertIdentity(core, dto);
		upsertClinical(core, dto);
		upsertInsurance(core, dto);
		upsertConsent(core, dto);
		core.setIdCabinet(dto.idCabinet());
		core.setIdProvider(dto.idProvider());
		if (sse != null)
			sse.notifyChartsChanged();
		return toDto(coreRepo.findWithAllById(core.getId()).orElse(core));
	}
	
	

	@Override
	public void delete(int id) {
		Long lid = (long) id;
		// soft-delete possible; ici hard delete par simplicité
		consentRepo.deleteById(lid);
		insRepo.deleteById(lid);
		clinicalRepo.deleteById(lid);
		idRepo.deleteById(lid);
		coreRepo.deleteById(lid);
		if (sse != null)
			sse.notifyChartsChanged();
	}

	@Override
	public ChartDto createIdempotent(ChartDto dto) {
		var core = createOrGetCore(dto);
		return toDto(coreRepo.findWithAllById(core.getId()).orElse(core));
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<ChartDto> findByClientUuid(String clientUuid) {
		return coreRepo.findByClientUuid(clientUuid).map(this::toDto);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ChartDto> findUpdated(Instant since) {
	    List<ChartCore> cores = coreRepo.findByUpdatedAtAfter(since);
	    return cores.stream()
	                .map(this::toDto)
	                .toList();
	}


	@Transactional(readOnly = true)
	@Override
	public List<ChartDto> findAllChartByLastName(String lastname) {
		return idRepo.findByLastNameContaining(lastname).stream()
				.map(pi -> loadAggregate(pi.getId()).orElse(pi.getCore())).map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public List<ChartDto> findAllChartByBirthday(LocalDate birthday) {
		return idRepo.findByBirthday(birthday).stream().map(pi -> loadAggregate(pi.getId()).orElse(pi.getCore()))
				.map(this::toDto).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public List<ChartDto> findAllChartByPhoneMobile(String phone) {
		return idRepo.findByPhoneMobile(phone).stream().map(pi -> loadAggregate(pi.getId()).orElse(pi.getCore()))
				.map(this::toDto).toList();
	}

//	  @Transactional(readOnly = true)
//	  @Override
//	  public List<ChartDto> findByBirthdayPrefix(String prefix) {
//	    // si tu gardes la table de dates en identity, expose une méthode dans idRepo
//	    return idRepo.searchByBirthdayPrefix(prefix).stream()
//	        .map(pi -> loadAggregate(pi.getId()).orElse(pi.getCore()))
//	        .map(this::toDto)
//	        .toList();
//	  }
//	  
//	  Page<ChartDto> searchByBirthdayPrefix(String prefix, Pageable pageable) {
//		  Page<ChartIdentity> page = idRepo.searchByBirthdayPrefix(prefix, pageable);
//
//		  List<ChartDto> dtos = page.getContent().stream()
//		      .map(id -> {
//		        var core = coreRepo.findById(id.getCoreId()).orElse(null);
//		        var clinical = clinicalRepo.findByCoreId(id.getCoreId()).orElse(null);
//		        var ins = insRepo.findByCoreId(id.getCoreId()).orElse(null);
//		        var consent = consentRepo.findByCoreId(id.getCoreId()).orElse(null);
//		        return toDto(core, id, clinical, ins, consent); // ton assembleur
//		      })
//		      .toList();
//
//		  return new PageImpl<>(dtos, pageable, page.getTotalElements());
//		}

	/* ===================== Internal upserts ===================== */

	@Transactional
	private ChartCore createOrGetCore(ChartDto dto) {
		// 1) Résoudre/normaliser le UUID
		final String uuid = (dto.clientUuid() != null && !dto.clientUuid().isBlank()) ? dto.clientUuid()
				: java.util.UUID.randomUUID().toString();

		// 2) Essayer de retrouver un core existant
		var existing = coreRepo.findByClientUuid(uuid);
		ChartCore core;
		if (existing.isPresent()) {
			core = existing.get();
		} else {
			// 3) Créer le core et flusher pour obtenir l'ID (nécessaire pour @MapsId)
			core = new ChartCore();
			core.setIdCabinet(dto.idCabinet());
			core.setClientUuid(uuid);
			core.setIdProvider(dto.idProvider());
			core.setLastEncounterAt(dto.lastEncounterAt());
			core.setNextAppointmentAt(dto.nextAppointmentAt());
			core = coreRepo.saveAndFlush(core); // IMPORTANT: flush pour avoir core.getId()
		}

		if (core.getIdentity() == null) {
			var id = new ChartIdentity();
			id.setCore(core);
			id.setFirstName(dto.firstName());
			id.setMiddleName(dto.middleName());
			id.setLastName(dto.lastName());
			id.setPreferredName(dto.preferredName());
			id.setGender(dto.gender());
			id.setBirthday(dto.birthday());
			id.setPhotoUrl(dto.photoUrl());
            id.setPhoneMobile(dto.phoneMobile());
			id.setPhoneHome(dto.phoneHome());
			id.setEmail(dto.email());
			id.setAddressLine1(dto.addressLine1());
			id.setAddressLine2(dto.addressLine2());
            id.setCity(dto.city());
			id.setState(dto.state());
			id.setZipCode(dto.zipCode());
			id.setMaritalStatus(dto.maritalStatus());
			id.setEmergencyContactName(dto.emergencyContactName());
			id.setEmergencyContactPhone(dto.emergencyContactPhone());
			id.setEmergencyContactRelation(dto.emergencyContactRelation());
			id.setHasPortalAccount(dto.hasPortalAccount());
			id.setPortalUsername(dto.portalUsername());
			id.setLastPortalLogin(dto.lastPortalLogin());

			idRepo.save(id);
			core.setIdentity(id);
		}

		if (core.getClinical() == null) {
			var cl = new ChartClinical();
			cl.setCore(core);
			
			cl.setHeightCm(dto.heightCm());
			cl.setWeightKg(dto.weightKg());
			cl.setSmokingStatus(dto.smokingStatus());
			cl.setEmployer(dto.employer());
			cl.setFinancialStrain(dto.financialStrain());
			cl.setTransportationAccess(dto.transportationAccess());
			cl.setHousingStatus(dto.housingStatus());
			cl.setOccupation(dto.occupation());
			cl.setAlcoholUse(dto.alcoholUse());
			cl.setAllergies(dto.allergies() != null ? dto.allergies() : java.util.List.of());
			cl.setChronicConditions(dto.chronicConditions() != null ? dto.chronicConditions() : java.util.List.of());
//	    PharmacyInfo pharmacyInfo=new PharmacyInfo();
//	    pharmacyInfo.setAddress(dto.);
//	    pharmacyInfo.setFax(uuid);
//	    pharmacyInfo.setName(uuid);
//	    pharmacyInfo.setPhone(uuid);
			clinicalRepo.save(cl);
			core.setClinical(cl);
		}

		if (core.getInsurance() == null) {
			var ins = new ChartInsurance();
			ins.setCore(core);
			
			ins.setProvider(dto.insuranceProvider());
			ins.setMemberId(dto.insuranceMemberId());
			ins.setPlanName(dto.insurancePlanName());
			ins.setExpirationDate((dto.insuranceExpiration() != null) ? dto.insuranceExpiration() : null);
			
			insRepo.save(ins);
			core.setInsurance(ins);
		}

		if (core.getConsent() == null) {
			var cs = new ChartConsent();
			cs.setCore(core);
			
			cs.setHipaaConsentSigned(Boolean.TRUE.equals(dto.hipaaConsentSigned()));
			cs.setHipaaConsentAt(dto.hipaaConsentAt());
			cs.setTelehealthConsentSigned(Boolean.TRUE.equals(dto.telehealthConsentSigned()));
			cs.setDataSharingConsent(Boolean.TRUE.equals(dto.dataSharingConsent()));
			cs.setTelehealthConsentAt(dto.telehealthConsentAt());
			
			consentRepo.save(cs);
			core.setConsent(cs);
		}

		return core;
	}

	private void upsertIdentity(ChartCore core, ChartDto dto) {
		var id = core.getIdentity() != null ? core.getIdentity() : new ChartIdentity();
		id.setCore(core);
		id.setFirstName(dto.firstName() != null ? dto.firstName() : id.getFirstName());
		id.setMiddleName(dto.middleName() != null ? dto.middleName() : id.getMiddleName());
		id.setLastName(dto.lastName() != null ? dto.lastName() : id.getLastName());
		id.setPreferredName(dto.preferredName() != null ? dto.preferredName() : id.getPreferredName());
		id.setGender(dto.gender() != null ? dto.gender() : id.getGender());
		id.setBirthday(dto.birthday() != null ? dto.birthday() : id.getBirthday());
		id.setPhotoUrl(dto.photoUrl() != null ? dto.photoUrl() : id.getPhotoUrl());
		id.setPhoneMobile(dto.phoneMobile() != null ? dto.phoneMobile() : id.getPhoneMobile());
		id.setPhoneHome(dto.phoneHome() != null ? dto.phoneHome() : id.getPhoneHome());
		id.setEmail(dto.email() != null ? dto.email() : id.getEmail());
		id.setAddressLine1(dto.addressLine1() != null ? dto.addressLine1() : id.getAddressLine1());
		id.setAddressLine2(dto.addressLine2() != null ? dto.addressLine2() : id.getAddressLine2());
		id.setCity(dto.city() != null ? dto.city() : id.getCity());
		id.setState(dto.state() != null ? dto.state() : id.getState());
		id.setZipCode(dto.zipCode() != null ? dto.zipCode() : id.getZipCode());
		id.setMaritalStatus(dto.maritalStatus() != null ? dto.maritalStatus() : id.getMaritalStatus());
		id.setEmergencyContactName(
				dto.emergencyContactName() != null ? dto.emergencyContactName() : id.getEmergencyContactName());
		id.setEmergencyContactPhone(
				dto.emergencyContactPhone() != null ? dto.emergencyContactPhone() : id.getEmergencyContactPhone());
		id.setEmergencyContactRelation(dto.emergencyContactRelation() != null ? dto.emergencyContactRelation()
				: id.getEmergencyContactRelation());
		id.setHasPortalAccount(dto.hasPortalAccount() != null ? dto.hasPortalAccount() : id.getHasPortalAccount());
		id.setPortalUsername(dto.portalUsername() != null ? dto.portalUsername() : id.getPortalUsername());
		id.setLastPortalLogin(dto.lastPortalLogin() != null ? dto.lastPortalLogin() : id.getLastPortalLogin());

		idRepo.save(id);
		core.setIdentity(id);
	}

	private void upsertClinical(ChartCore core, ChartDto dto) {
		var cl = core.getClinical() != null ? core.getClinical() : new ChartClinical();
		cl.setCore(core);

		cl.setHeightCm(dto.heightCm() != null ? dto.heightCm() : cl.getHeightCm());
		cl.setWeightKg(dto.weightKg() != null ? dto.weightKg() : cl.getWeightKg());
		cl.setSmokingStatus(dto.smokingStatus() != null ? dto.smokingStatus() : cl.getSmokingStatus());
		cl.setEmployer(dto.employer() != null ? dto.employer() : cl.getEmployer());
		cl.setFinancialStrain(dto.financialStrain() != null ? dto.financialStrain() : cl.getFinancialStrain());
		cl.setTransportationAccess(
				dto.transportationAccess() != null ? dto.transportationAccess() : cl.getTransportationAccess());
		cl.setHousingStatus(dto.housingStatus() != null ? dto.housingStatus() : cl.getHousingStatus());
		cl.setOccupation(dto.occupation() != null ? dto.occupation() : cl.getOccupation());
		cl.setAlcoholUse(dto.alcoholUse() != null ? dto.alcoholUse() : cl.getAlcoholUse());
		cl.setBloodType(dto.bloodType() != null ? dto.bloodType() : cl.getBloodType());

		cl.setMedications(dto.medications() != null ? dto.medications() : cl.getMedications());
		cl.setAllergies(dto.allergies() != null ? dto.allergies() : cl.getAllergies());
		cl.setChronicConditions(dto.chronicConditions() != null ? dto.chronicConditions() : cl.getChronicConditions());

		clinicalRepo.save(cl);
		core.setClinical(cl);
	}

	private void upsertInsurance(ChartCore core, ChartDto dto) {
		var ins = core.getInsurance() != null ? core.getInsurance() : new ChartInsurance();
		ins.setCore(core);

		ins.setProvider(dto.insuranceProvider() != null ? dto.insuranceProvider() : ins.getProvider());
		ins.setMemberId(dto.insuranceMemberId() != null ? dto.insuranceMemberId() : ins.getMemberId());
		ins.setPlanName(dto.insurancePlanName() != null ? dto.insurancePlanName() : ins.getPlanName());
		ins.setExpirationDate(dto.insuranceExpiration() != null ? dto.insuranceExpiration() : ins.getExpirationDate());

		insRepo.save(ins);
		core.setInsurance(ins);
	}

	private void upsertConsent(ChartCore core, ChartDto dto) {
		var cs = core.getConsent() != null ? core.getConsent() : new ChartConsent();
		cs.setCore(core);

		cs.setHipaaConsentSigned(
				dto.hipaaConsentSigned() != null ? dto.hipaaConsentSigned() : cs.getHipaaConsentSigned());
		cs.setTelehealthConsentSigned(dto.telehealthConsentSigned() != null ? dto.telehealthConsentSigned()
				: cs.getTelehealthConsentSigned());
		cs.setDataSharingConsent(
				dto.dataSharingConsent() != null ? dto.dataSharingConsent() : cs.getDataSharingConsent());
		cs.setHipaaConsentAt(dto.hipaaConsentAt() != null ? dto.hipaaConsentAt() : cs.getHipaaConsentAt());
		cs.setTelehealthConsentAt(
				dto.telehealthConsentAt() != null ? dto.telehealthConsentAt() : cs.getTelehealthConsentAt());

		consentRepo.save(cs);
		core.setConsent(cs);
	}



}
