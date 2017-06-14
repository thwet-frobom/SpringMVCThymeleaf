package com.amh.pm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amh.pm.entity.User;
import com.amh.pm.service.UserService;

@Controller
public class UserController {

    private UserService userService;

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
    
    @RequestMapping(value = "/registrationuser", method = RequestMethod.POST)
    public String userRegister(@Validated @ModelAttribute User user, BindingResult result,Model model, HttpServletRequest request) {
        if(result.hasErrors()){
            return "registration";
        }else{
            String name = user.getName();
            String email = user.getEmail();
            
           User userNameCheck = userService.findUserIdByName(name);
           User userEmailCheck = userService.findUserByEmail(email);
           
           if(userNameCheck !=null){
               String nameExist = "User Name is already exist:";
               model.addAttribute("nameExist",nameExist);
               return "registration";
           }else if(userEmailCheck !=null){
               String emailExist = "User Email is already exist:";
               model.addAttribute("emailExist", emailExist);
               return "registration";
           }else{
               userService.save(user);    
               return "redirect:login";
           }
        }
    }
    
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginForm(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(value = "/loginuser", method = RequestMethod.POST)
	public String loginUser(@Validated @ModelAttribute("userForm") User user, BindingResult result, Model model,
			HttpServletRequest req) {

		 HttpSession session = req.getSession(true);
	        String name = user.getName();
	        String password = user.getPassword();
	        User u = userService.userByName(name, password);      
	        if (u == null) {
	            session.setAttribute("message", "Username or Password Incorrect");
	            return "redirect:login";
	        } else {
	            session.setAttribute("userId", u.getId());
	            session.setAttribute("name", u.getName());
	            session.setAttribute("password", u.getPassword());
	        }
	        return "redirect:organizations";		
	}

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String addCustomer(@Validated @ModelAttribute User user, BindingResult result, Model model) {
        userService.save(user);
        return "redirect:users";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("users", this.userService.findAll());
        return "users";
    }

    @RequestMapping(value = "users/{id}", method = RequestMethod.GET)
    public String showDetails(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "userDetails";
    }

}
