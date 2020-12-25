package com.example.shopping;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.model.data.Category;
import com.example.shopping.model.data.Product;
import com.example.shopping.model.data.categoryRepository;
import com.example.shopping.model.data.pageRepository;
import com.example.shopping.model.data.productRepository;

@Controller
@RequestMapping("/category")
public class categoryController {
	@Autowired
	productRepository productRepo;
	@Autowired
	categoryRepository categoryRepo;
	
	@GetMapping("/{slug}")
	public String category(@PathVariable String slug,Model model, @RequestParam(value = "page",required = false) Integer p) {
		
		int perPage = 6;
		int page = (p!=null)?p:0;
		
		long count = 0;
		Pageable pageable = PageRequest.of(page,perPage);
		
		if(slug.equals("all")) {
			Page<Product> products = productRepo.findAll(pageable);
			count = productRepo.count();
			model.addAttribute("products", products);
		}else {
			Category category = categoryRepo.findBySlug(slug);
			if(category ==null) {
				return "redirect:/";
			}
			int categoryId = category.getId();
			String categoryName = category.getName();
			List<Product> products = productRepo.finAllByCategoryId(categoryId);
			count = productRepo.countByCategoryId(categoryId);
			
			model.addAttribute("products", products);
			model.addAttribute("categoryName",categoryName);
		}
		

		double pageCount = Math.ceil((double)count/(double)perPage);
		
		model.addAttribute("pageCount", (int)pageCount);
		model.addAttribute("perPage", perPage);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		
		return "products";
	}
}
