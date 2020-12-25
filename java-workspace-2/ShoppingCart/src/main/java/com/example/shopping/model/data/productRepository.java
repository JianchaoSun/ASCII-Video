package com.example.shopping.model.data;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface productRepository extends JpaRepository<Product,Integer>{

	Product findBySlug(String slug);

	Product findBySlugAndIdNot(String slug, int id);
	
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId ")
	List<Product> finAllByCategoryId(int categoryId);

	long countByCategoryId(int categoryId);
	
}
