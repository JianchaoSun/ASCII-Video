package com.example.shopping.model.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface adminRepository extends JpaRepository<Admin,Integer>{
	Admin findByUsername(String username);
}