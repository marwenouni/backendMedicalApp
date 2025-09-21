package com.myapp.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.demo.Repository.ProductRepository;
import com.myapp.demo.entity.Produit;

@Service
public class ProductService implements IProduitService{

	List<Produit> produits;
	@Autowired
	ProductRepository productRepo;
	
	public ProductService() {
		
	}

	
	
	@Override
	public List<Produit> getProduits() {
		return productRepo.findAll();
	}
	
	@Override
	public Produit getProduit(String ref) {
		for(Produit produit : this.produits)
		{
			if (produit.getRef().equals(ref))
				return produit;
						
		}
		return null;
	}

	@Override
	public void addProduit(Produit produit) {
		produits.add(produit);
		
	}

	@Override
	public void updateProduit(Produit produit) {
		System.out.println(getProduit(produit.getRef()));
		if(getProduit(produit.getRef())!=null)
		{
			deleteProduit(produit.getRef());
			produits.add(produit);
		}
		
		
	}

	@Override
	public void deleteProduit(String ref) {
		Produit produit= new Produit();
		produit.setRef(ref);
		produits.remove(produit);
		
	}

}
