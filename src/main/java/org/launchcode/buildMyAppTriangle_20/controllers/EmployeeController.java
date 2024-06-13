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
@RequestMapping("employees")
public class EmployeeController {
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
        return "employees/index.html";
    }

    @GetMapping("add")
    public String displayAddEmployeeForm(Model model) {
        model.addAttribute(new User());
        return "employees/add";
    }

    @PostMapping(value = "add",
            // In order to export to database when encrypted, the data has to be changed to a specific type.
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public String processAddEmployeeForm(@ModelAttribute @Valid User newUser,
                                         Errors errors, Model model) {
        newUser.setUserRoles(Arrays.asList(roleRepository.findByName("ROLE_EMPLOYEE")));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (errors.hasErrors()) {
            return "employees/add";
        }

        try {
            userDetailsService.loadUserByUsername(newUser.getUsername());
        } catch (Exception UsernameNotFoundException) {
            userDetailsService.createUser(newUser, "ROLE_EMPLOYEE");
            return "redirect:/employees";
        }
        return "employees/add";
    }

    @GetMapping("delete")
    public String displayDeleteEmployeeForm(Model model) {
        model.addAttribute("employees", userRepository.findUserByRoleName("ROLE_EMPLOYEE"));
        model.addAttribute("customers", userRepository.findUserByRoleName("ROLE_CUSTOMER"));
        return "employees/delete";
    }

    @PostMapping("delete")
    public String processDeleteEmployeeForm(@RequestParam @Valid Long employeeId) {
        userRepository.deleteById(employeeId);
        return "redirect:/employees";
    }

    @GetMapping("view/{id}")
    public String displayViewEmployee(Model model, @PathVariable Long id) {
        Optional optionalEmployee = userRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            User user = (User) optionalEmployee.get();
            model.addAttribute("employee", user);
            return "employees/view";
        } else {
            return "redirect:/employees";
        }
    }

    @GetMapping("view/{id}/update")
    public String displayUpdateEmployee(Model model, @PathVariable Long id) {
        Optional optionalEmployee = userRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            User user = (User) optionalEmployee.get();
            model.addAttribute("employee", user);
            return "employees/update";
        } else {
            return "redirect:/employees";
        }
    }

    @PostMapping(
            value = "view/{id}/update",
            // In order to export to database when encrypted, the data has to be changed to a specific type.
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public String processUpdateCustomer(Model model, @PathVariable Long id, @ModelAttribute @Valid User employee,
                                        Errors errors) {
        if (errors.hasErrors()) {
            return "view/" + id + "/update";
        }
        else {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employee.setUserRoles(Arrays.asList(roleRepository.findByName("ROLE_EMPLOYEE")));
            userRepository.save(employee);
        }
        return "redirect:/employees/view/" + id;
    }
}
