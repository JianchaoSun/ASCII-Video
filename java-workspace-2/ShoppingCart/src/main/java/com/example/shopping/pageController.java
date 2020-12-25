package com.example.shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shopping.model.data.Page;
import com.example.shopping.model.data.categoryRepository;
import com.example.shopping.model.data.pageRepository;

@Controller
@RequestMapping("/")
public class pageController {
	@Autowired
	pageRepository pageRepo;
	
	
	
	@GetMapping
	public String home(Model model) {
		Page page = pageRepo.findBySlug("home");
		
		model.addAttribute("page", page);
		return "page";
	}
	
	@GetMapping("/{slug}")
	public String home(@PathVariable String slug,Model model) {
		Page page = pageRepo.findBySlug(slug);
		if(page==null) {
			return "redirect:/";
		}
		
		model.addAttribute("page", page);
		return "page";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	

}
