package com.example.shopping.model.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<User,Integer>{
	User findByUsername(String username);
}
