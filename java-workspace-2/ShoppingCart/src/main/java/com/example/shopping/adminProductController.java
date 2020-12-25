package com.example.shopping;

import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shopping.model.data.Category;
import com.example.shopping.model.data.Product;
import com.example.shopping.model.data.categoryRepository;
import com.example.shopping.model.data.productRepository;

@Controller
@RequestMapping("/admin/products")
public class adminProductController {
	@Autowired
	private productRepository productRepo;

	@Autowired
	private categoryRepository categoryRepo;
	
	@GetMapping
	public String index(Model model, @RequestParam(value = "page",required = false) Integer p) {
		
		int perPage = 6;
		int page = (p!=null)?p:0;
		
		Pageable pageable = PageRequest.of(page,perPage);
		Page<Product> products = productRepo.findAll(pageable);
		
		List<Category> categories = categoryRepo.findAll();
		HashMap<Integer,String> cats = new HashMap<>();
		for(Category cat: categories) {
			cats.put(cat.getId(), cat.getName());
		}
		model.addAttribute("cats",cats);
		model.addAttribute("products",products);
		
		Long count = productRepo.count();
		double pageCount = Math.ceil((double)count/(double)perPage);
		
		model.addAttribute("pageCount", (int)pageCount);
		model.addAttribute("perPage", perPage);
		model.addAttribute("count", count);
		model.addAttribute("page", page);
		
		return "admin/products/index";
	}
	
	@GetMapping("/add")
	public String add(Product product,Model model) {
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("categories", categories);
		
		
		return "admin/products/add";
	}
	
	@PostMapping("/add")
	public String add(@Valid Product product,
			BindingResult bindingResult,
			MultipartFile file,
			RedirectAttributes redirectAttributes,
			Model model) throws IOException{
		
	
		List<Category> categories = categoryRepo.findAll();
		
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("categories", categories);
			return "admin/products/add";
		}
		
		boolean fileOk=  false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/"+filename);
		
		if(filename.endsWith("jpg")||filename.endsWith("png")) {
			fileOk = true;
		}

		redirectAttributes.addFlashAttribute("message","Product Added");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		String slug = product.getName().toLowerCase().replace(" ", "-");
		Product proExists = productRepo.findBySlug(slug); 
		
		if(!fileOk) {
			redirectAttributes.addFlashAttribute("message","Image need to be jpg or png");
			redirectAttributes.addFlashAttribute("alertClass","alert-success");
			redirectAttributes.addFlashAttribute("product",product);
		}else if(proExists!=null) {
			redirectAttributes.addFlashAttribute("message","Product Existed, choose another");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
			redirectAttributes.addFlashAttribute("product",product);
		}else {
			product.setSlug(slug);
			product.setImage(filename);
			productRepo.save(product);
			
			System.out.print(Files.write(path,bytes));
		}
		
		
		return "redirect:/admin/products/add";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id,Model model) {
		Product product = productRepo.getOne(id);
		List<Category> categories = categoryRepo.findAll();
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		return "admin/products/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@Valid Product product,
			BindingResult bindingResult,
			MultipartFile file,
			RedirectAttributes redirectAttributes,
			Model model) throws IOException{
		
	
        Product cur = productRepo.getOne(product.getId());
		
		List<Category> categories = categoryRepo.findAll();
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("productName", cur.getName());
			model.addAttribute("categories", categories);
			return "admin/products/edit";
		}
		
		boolean fileOk=  false;
		byte[] bytes = file.getBytes();
		String filename = file.getOriginalFilename();
		Path path = Paths.get("src/main/resources/static/media/"+filename);
		
		
        if(!file.isEmpty()) {
		if(filename.endsWith("jpg")||filename.endsWith("png")) {
			fileOk = true;
		}
        }else {
        	fileOk = true;
        }

		redirectAttributes.addFlashAttribute("message","Product Edited");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		String slug = product.getName().toLowerCase().replace(" ", "-");
		Product proExists = productRepo.findBySlugAndIdNot(slug,product.getId()); 
		
		if(!fileOk) {
			redirectAttributes.addFlashAttribute("message","Image need to be jpg or png");
			redirectAttributes.addFlashAttribute("alertClass","alert-success");
			redirectAttributes.addFlashAttribute("product",product);
		}else if(proExists!=null) {
			redirectAttributes.addFlashAttribute("message","Product Existed, choose another");
			redirectAttributes.addFlashAttribute("alertClass","alert-danger");
			redirectAttributes.addFlashAttribute("product",product);
		}else {
			product.setSlug(slug);
			
			if(!file.isEmpty()) {
				Path path2 = Paths.get("src/main/resources/static/media/"+cur.getImage());
				Files.delete(path2);
				product.setImage(filename);
				Files.write(path, bytes);
			}else {
				product.setImage(cur.getImage());
				
			}
			productRepo.save(product);
			
		}
		
		
		return "redirect:/admin/products/edit/" +product.getId();
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id,RedirectAttributes redirectAttributes)throws IOException {
		Product product = productRepo.getOne(id);
		Product cur = productRepo.getOne(product.getId());
	//	Path path2 = Paths.get("src/main/resources/static/media/"+cur.getImage());
	//	Files.delete(path2);
		productRepo.deleteById(id);
		redirectAttributes.addFlashAttribute("message","Product Deleted");
		redirectAttributes.addFlashAttribute("alertClass","alert-success");
		return "redirect:/admin/products";
	}
}
