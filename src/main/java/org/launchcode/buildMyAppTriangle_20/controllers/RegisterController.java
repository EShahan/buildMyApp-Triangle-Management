package org.launchcode.buildMyAppTriangle_20.controllers;

import jakarta.validation.Valid;
import org.launchcode.buildMyAppTriangle_20.models.User;
import org.launchcode.buildMyAppTriangle_20.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("register")
    public String registerUser(Model model) {
        model.addAttribute(new User());
        return "register";
    }

    @PostMapping(
            value = "register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
                    MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public String addUser(@ModelAttribute @Valid User newUser, @RequestParam String employeeCode,
                          Errors errors, Model model) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (errors.hasErrors() || (employeeCode != null && !employeeCode.equals("12345"))) {
            return "register";
        }

        try {
            userDetailsService.loadUserByUsername(newUser.getUsername());
        } catch (Exception UsernameNotFoundException) {
            if (employeeCode.equals("12345")) {
                userDetailsService.createUser(newUser, "ROLE_EMPLOYEE");
            }
            else {
                userDetailsService.createUser(newUser, "ROLE_CUSTOMER");
            }
            return "redirect:/login";
        }
        return "register";
    }
}
