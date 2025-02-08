package com.example.jwttokendemo.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String getHomePage() {
		return "HomePage.html";
	}
	
	@GetMapping("/login")
	public String getLoginPage() {
		return "LoginPage.html";
	}
	
}
