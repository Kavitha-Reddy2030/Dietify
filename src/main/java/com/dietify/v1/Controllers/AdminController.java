package com.dietify.v1.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dietify.v1.Entity.User;
import com.dietify.v1.Repository.UserRepo;
import com.dietify.v1.Service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepo userRepo; // Use JPA repository for CRUD operations

	@GetMapping("/profile")
	public String profile(Model model) {
		// Check if user is authenticated
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return "redirect:/index"; // Redirect to login if not authenticated
		}

		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		String email = principal.getName();
		User user = userRepo.findByEmail(email); // Or userService.getCurrentUser()
		model.addAttribute("user", user);

		return "admin_profile"; // Render the admin profile page
	}

	@GetMapping("/users") // Revised endpoint for clarity
	public String getAllUsers(Model model) {
		List<User> users;
		users = userRepo.findAll(); // Use JPA repository for simplicity
		model.addAttribute("users", users);
		return "admin_users"; // Render the user data view
	}

	@PostMapping("/user/delete")
	public String deleteEmployee(@RequestParam("id") Integer userId) {
		userRepo.deleteById(userId);
		return "redirect:/admin/users";
	}

	@GetMapping("/user/edit")
	public String showEditUserForm(@RequestParam("id") int userId, Model model) {
		User user = userRepo.findById(userId).orElse(null);
		model.addAttribute("user", user);
		return "editusers";
	}

	@PostMapping("/user/update")
	public String updateUser(@ModelAttribute("user") User updatedUser) {
		// Perform update operation in the database
		User existingUser = userRepo.findById(updatedUser.getId()).orElse(null);

		if (existingUser != null) {
			// Update user information
			existingUser.setName(updatedUser.getName());
			// Update other fields as needed
			userRepo.save(existingUser);
		}

		return "redirect:/admin/users";
	}

}