package com.example.shopping;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class webConfig implements WebMvcConfigurer {
/*	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
	}*/
	
	
	public void addResourceHandler(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/media/**").addResourceLocations("file:/Applications/java-workspace-2/ShoppingCart/src/main/resources/static");
	}

}
