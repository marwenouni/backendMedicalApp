package com.myapp.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.demo.entity.Produit;

public  interface ProductRepository extends JpaRepository<Produit, Long>{

	 List<Produit> findAll();
}
