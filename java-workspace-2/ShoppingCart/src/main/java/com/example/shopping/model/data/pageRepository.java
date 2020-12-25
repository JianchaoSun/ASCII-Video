package com.example.shopping.model.data;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.shopping.model.data.Page;
public interface pageRepository extends JpaRepository<Page,Integer>{
	
	Page findBySlug(String slug);
	
	@Query("SELECT p FROM Page p WHERE p.id != :id and p.slug = :slug")
	Page findBySlug(int id,String slug);
	
//	Page findBySlugAndIdNot(int id,String slug);
	
	List<Page> findAllByOrderBySortingAsc();
	
}
