package org.launchcode.buildMyAppTriangle_20.controllers;

import jakarta.validation.Valid;
import org.launchcode.buildMyAppTriangle_20.models.User;
import org.launchcode.buildMyAppTriangle_20.models.data.RoleRepository;
import org.launchcode.buildMyAppTriangle_20.models.data.UserRepository;
import org.launchcode.buildMyAppTriangle_20.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("accounts")
public class AccountController {
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("employees", userRepository.findUserByRoleName("ROLE_EMPLOYEE"));
        model.addAttribute("customers", userRepository.findUserByRoleName("ROLE_CUSTOMER"));
        return "accounts/index.html";
    }
    @GetMapping("add")
    public String displayAddEmployeeForm(Model model) {
        model.addAttribute(new User());
        return "accounts/add";
    }

    @PostMapping(value = "add",
            // In order to export to database when encrypted, the data has to be changed to a specific type.
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public String processAddEmployeeForm(@ModelAttribute @Valid User newUser, @RequestParam String role,
                                         Errors errors, Model model) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (errors.hasErrors()) {
            return "accounts/add";
        }

        try {
            userDetailsService.loadUserByUsername(newUser.getUsername());
        } catch (Exception UsernameNotFoundException) {
            userDetailsService.createUser(newUser, role);
            return "redirect:/accounts";
        }
        return "accounts/add";
    }

    @GetMapping("delete")
    public String displayDeleteEmployeeForm(Model model) {
        model.addAttribute("employees", userRepository.findUserByRoleName("ROLE_EMPLOYEE"));
        model.addAttribute("customers", userRepository.findUserByRoleName("ROLE_CUSTOMER"));
        return "accounts/delete";
    }

    @PostMapping("delete")
    public String processDeleteEmployeeForm(@RequestParam @Valid Long employeeId) {
        userRepository.deleteById(employeeId);
        return "redirect:/accounts";
    }

    @GetMapping("view/{id}")
    public String displayViewEmployee(Model model, @PathVariable Long id) {
        Optional optionalEmployee = userRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            User user = (User) optionalEmployee.get();
            model.addAttribute("user", user);
            return "accounts/view";
        } else {
            return "redirect:/employees";
        }
    }

    @GetMapping("view/{id}/update")
    public String displayUpdateEmployee(Model model, @PathVariable Long id) {
        Optional optionalEmployee = userRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            User user = (User) optionalEmployee.get();
            model.addAttribute("user", user);
            return "accounts/update";
        } else {
            return "redirect:/accounts";
        }
    }

    @PostMapping(
            value = "view/{id}/update",
            // In order to export to database when encrypted, the data has to be changed to a specific type.
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public String processUpdateCustomer(Model model, @PathVariable Long id, @ModelAttribute @Valid User user, @RequestParam String role,
                                        Errors errors) {
        if (errors.hasErrors()) {
            return "view/" + id + "/update";
        }
        else {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDetailsService.createUser(user, role);
        }
        return "redirect:/accounts/view/" + id;
    }
}
