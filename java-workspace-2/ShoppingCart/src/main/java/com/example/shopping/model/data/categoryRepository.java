package com.example.shopping.model.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface categoryRepository extends JpaRepository<Category, Integer> {
	Category findByName(String name);
	
	Category findBySlugAndIdNot(int id,String slug);

	Category findBySlug(String slug);
}
