package com.example.shopping;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shopping.model.data.Cart;
import com.example.shopping.model.data.Category;
import com.example.shopping.model.data.Product;
import com.example.shopping.model.data.productRepository;

@Controller
@RequestMapping("/cart")
public class cartConntroller {
	@Autowired
	private productRepository productRepo;
	
	
	@GetMapping("/add/{id}")
	public String add(@PathVariable int id,HttpSession session,
			Model model,@RequestParam(value="cartPage",required=false) String cartPage) {
		Product product = productRepo.getOne(id);
		
		if(session.getAttribute("cart") == null) {
			HashMap<Integer,Cart> cart = new HashMap<>();
			cart.put(id,new Cart(id,product.getName(),product.getPrice(),1,product.getImage()));
			session.setAttribute("cart", cart);
		}else {
			HashMap<Integer,Cart> cart = (HashMap<Integer,Cart>)session.getAttribute("cart");
			if(cart.containsKey(id)) {
				int q = cart.get(id).getQuantity();
				cart.put(id, new Cart(id,product.getName(),product.getPrice(),q+1,product.getImage()));
			}else {
				cart.put(id, new Cart(id,product.getName(),product.getPrice(),1,product.getImage()));
				session.setAttribute("cart", cart);
			}
		}
		HashMap<Integer,Cart> cart = (HashMap<Integer,Cart>)session.getAttribute("cart");
		int size =0;
		double total =0;
		for(Cart value: cart.values()) {
			size += value.getQuantity();
			total += value.getQuantity()* Double.parseDouble(value.getPrice());
		}
		model.addAttribute("size", size);
		model.addAttribute("total", total);
		
		if(cartPage!=null) {
			return "redirect:/cart/view";
		}
		
		return "cart_view";
	}
	
	@RequestMapping("/view")
	public String View(HttpSession session,Model model) {
		if(session.getAttribute("cart")==null) {
			return "redirect:/";
		}
		HashMap<Integer,Cart> cart = (HashMap<Integer,Cart>)session.getAttribute("cart");
		int size =0;
		double total =0;
		for(Cart value: cart.values()) {
			size += value.getQuantity();
			total += value.getQuantity()* Double.parseDouble(value.getPrice());
		}
		model.addAttribute("size", size);
		model.addAttribute("total", total);
		model.addAttribute("cart", cart);
		
		model.addAttribute("notCartViewPage", true);
		return "cart";
	}
	
	@GetMapping("/subtract/{id}")
	public String subtract(@PathVariable int id,HttpSession session,Model model,HttpServletRequest httpreq) {
		Product product = productRepo.getOne(id);
		HashMap<Integer,Cart> cart = (HashMap<Integer,Cart>)session.getAttribute("cart");
		int qty = cart.get(id).getQuantity();
		if(qty == 1) {
			cart.remove(id);
			if(cart.size()==0) {
				session.removeAttribute("cart");
			}
		}else {
			cart.put(id, new Cart(id,product.getName(),product.getPrice(),qty-1,product.getImage()));
		}
		String refLink = httpreq.getHeader("referer");
		
		return "redirect:" +refLink;
	}
	
	@GetMapping("/remove/{id}")
	public String remove(@PathVariable int id,HttpSession session,Model model,HttpServletRequest httpreq) {
		
		HashMap<Integer,Cart> cart = (HashMap<Integer,Cart>)session.getAttribute("cart");
		
		cart.remove(id);
		if(cart.size()==0) {		
			session.removeAttribute("cart");
		}
		
		
		String refLink = httpreq.getHeader("referer");
		
		return "redirect:" +refLink;
	}
	
	@GetMapping("/clear")
	public String clear(HttpSession session,HttpServletRequest httpreq) {
		
				
		session.removeAttribute("cart");
		
		String refLink = httpreq.getHeader("referer");
		
		return "redirect:" +refLink;
	}
	
}
