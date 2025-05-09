package org.launchcode.buildMyAppTriangle_20.controllers;

import jakarta.validation.Valid;
import org.launchcode.buildMyAppTriangle_20.models.Contract;
import org.launchcode.buildMyAppTriangle_20.models.User;
import org.launchcode.buildMyAppTriangle_20.models.data.RoleRepository;
import org.launchcode.buildMyAppTriangle_20.models.data.UserRepository;
import org.launchcode.buildMyAppTriangle_20.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
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
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findUserByUsername(userDetails.getUsername());
        if (currentUser.getUserRoles().contains(roleRepository.findByName("ROLE_ADMIN"))) {
            model.addAttribute("admins", userRepository.findUserByRoleName("ROLE_ADMIN"));
            model.addAttribute("employees", userRepository.findUserByExclusiveRole(2));
            model.addAttribute("customers", userRepository.findUserByRoleName("ROLE_CUSTOMER"));
        }
        else {
            model.addAttribute("admins", userRepository.findUsersByMatchingContracts(userRepository.findAllUserContractIds(currentUser.getId()), "ROLE_ADMIN"));
            model.addAttribute("employees", userRepository.findUsersByMatchingContracts(userRepository.findAllUserContractIds(currentUser.getId()), "ROLE_EMPLOYEE"));
            model.addAttribute("customers", userRepository.findUsersByMatchingContracts(userRepository.findAllUserContractIds(currentUser.getId()), "ROLE_CUSTOMER"));
        }
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
        model.addAttribute("employees", userRepository.findUserByExclusiveRole(2)); //Finding by exclusive roll prevents admins who are also employees from being deleted.
        model.addAttribute("customers", userRepository.findUserByRoleName("ROLE_CUSTOMER"));
        return "accounts/delete";
    }

    @PostMapping("delete")
    public String processDeleteEmployeeForm(@RequestParam("users") List<Long> users) {
        users.forEach(user -> {
            Collection<Contract> userContracts = userRepository.findAllUserContracts(user);
            //For each contract, get a list of all users on the contract sans the user we want to delete and update it
            userContracts.forEach(userContract -> {
                    userContract.setContractUsers(userRepository.getContractUserListMinusUser(userContract.getId(), user));
            });
            //Delete the user
            userRepository.deleteById(user);
        });

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
