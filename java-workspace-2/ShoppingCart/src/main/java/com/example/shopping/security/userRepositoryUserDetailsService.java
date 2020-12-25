package com.example.shopping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shopping.model.data.Admin;
import com.example.shopping.model.data.User;
import com.example.shopping.model.data.adminRepository;
import com.example.shopping.model.data.userRepository;

@Service
public class userRepositoryUserDetailsService implements UserDetailsService{
	
	@Autowired
	private userRepository userRepo;
	
	@Autowired
	private adminRepository adminRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		Admin admin= adminRepo.findByUsername(username);
		if(user!=null) {
			return user;
		}
		if(admin!=null) {
			return admin;
		}
		throw new UsernameNotFoundException("User "+username+" not found");
		
	}
	
}
