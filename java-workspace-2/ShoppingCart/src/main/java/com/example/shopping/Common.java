package com.example.shopping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.shopping.model.data.Cart;
import com.example.shopping.model.data.Category;
import com.example.shopping.model.data.Page;
import com.example.shopping.model.data.categoryRepository;
import com.example.shopping.model.data.pageRepository;

@ControllerAdvice
public class Common {
	
	@Autowired
	pageRepository pageRepo;
	@Autowired
	categoryRepository categoryRepo;
	
	@ModelAttribute
	public void sharedData(Model model, HttpSession session, Principal principal) {
		
		if(principal!=null) {
			model.addAttribute("principal", principal.getName());
		}
		List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
		List<Category> categories = categoryRepo.findAll();
		
		boolean cartActive = false;
		if(session.getAttribute("cart") != null) {
			HashMap<Integer,Cart> cart = (HashMap<Integer,Cart>)session.getAttribute("cart");
			int size= 0;
			double total = 0;
			for(Cart value: cart.values()) {
				size += value.getQuantity();
				total += value.getQuantity()* Double.parseDouble(value.getPrice());
			}
			model.addAttribute("csize", size);
			session.setAttribute("ctotal", total);
			
			cartActive = true;
		}
		
		model.addAttribute("cartActive", cartActive);
		model.addAttribute("cpages", pages);
		model.addAttribute("ccategories", categories);
	}

}
