package com.myapp.demo.service;

import java.util.List;

import com.myapp.demo.entity.Produit;

public interface IProduitService {
	
	List<Produit> getProduits();
	
	public Produit getProduit(String ref);
		
	public void addProduit(Produit produit);
	
	public void updateProduit(Produit produit);
	
	public void deleteProduit(String ref);

	

}
