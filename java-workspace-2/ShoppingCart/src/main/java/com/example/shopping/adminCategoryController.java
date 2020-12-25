package com.example.shopping;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.model.data.Category;
import com.example.shopping.model.data.Page;
import com.example.shopping.model.data.categoryRepository;

@Controller
@RequestMapping("/admin/categories")
public class adminCategoryController {
	@Autowired
	private categoryRepository categoryRepo;
	
	@GetMapping
	public String index(Model model) {
		List<Category>  categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		return "/admin/categories/index";
	}

	@GetMapping("/add")
	public String add(@ModelAttribute Category category) {
	//	model.addAttribute("page", new Page());
		return "admin/categories/add";
	}
	
	@PostMapping("/add")
	public String add(@Valid Category category,BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {
		
		
	//	Page cur = categoryRepo.getOne(page.getId());
		
		if(bindingResult.hasErrors()) {
			return "admin/categories/add";
		}

		redirectAttributes.addFlashAttribute("message","Category Added");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		String slug = category.getName().toLowerCase().replace(" ", "-");
		
		Category catExists = categoryRepo.findByName(slug); 
		
		if(catExists!=null) {
			redirectAttributes.addFlashAttribute("message","Category Existed, choose another");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
			redirectAttributes.addFlashAttribute("categoryInfo",category);
		}else {
			category.setSlug(slug);
			category.setSorting(100);
			categoryRepo.save(category);
		}
		
		/*
		 * <input type="text" class="form-control" th:fields="*{title}" placeholder="Title">
          <span class="error" th:if="${#fields.hasErrors('title')}" th:errors="*{title}"></span>
		 */
		
		return "redirect:/admin/categories/add";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id,Model model) {
		Category category = categoryRepo.getOne(id);
		model.addAttribute("category", category);
		return "admin/categories/edit";
	}
	
	
	@PostMapping("/edit")
	public String edit(@Valid Category category,BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {
		if(bindingResult.hasErrors()) {
			return "admin/categories/edit";
		}

		redirectAttributes.addFlashAttribute("message","Category Edited");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		String slug = category.getName().toLowerCase().replace(" ", "-");
		Category catExists = categoryRepo.findByName(category.getName());//  findBySlugAndIdNot(category.getId(), category.getName()); 
		
		if(catExists!=null) {
			redirectAttributes.addFlashAttribute("message","Category Existed, choose another");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
		//	redirectAttributes.addFlashAttribute("ca",page);
		}else {
			category.setSlug(slug);
			
			categoryRepo.save(category);
		}
		
		/*
		 * <input type="text" class="form-control" th:fields="*{title}" placeholder="Title">
          <span class="error" th:if="${#fields.hasErrors('title')}" th:errors="*{title}"></span>
		 */
		
		return "redirect:/admin/categories/edit/"+category.getId();
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id,RedirectAttributes redirectAttributes) {
		categoryRepo.deleteById(id);

		redirectAttributes.addFlashAttribute("message","Category Deleted");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		return "redirect:/admin/categories";
	}
	
}
