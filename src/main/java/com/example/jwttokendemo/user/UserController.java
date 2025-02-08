package com.example.jwttokendemo.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.jwttokendemo.authentication.AuthService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private final AuthService authService;
	
	public UserController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping("/center")
	public String getUserCenterPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		String nickname = authService.getNicknameFromUserDetails(userDetails);
		model.addAttribute("nickname", nickname);
		return "UserCenterPage.html";
	}
}
