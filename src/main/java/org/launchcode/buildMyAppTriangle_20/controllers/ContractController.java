package org.launchcode.buildMyAppTriangle_20.controllers;

import jakarta.validation.Valid;
import org.launchcode.buildMyAppTriangle_20.models.Contract;
import org.launchcode.buildMyAppTriangle_20.models.User;
import org.launchcode.buildMyAppTriangle_20.models.data.ContractRepository;
import org.launchcode.buildMyAppTriangle_20.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("contracts")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("contracts", contractRepository.findAll());
        return "contracts/index";
    }

    @GetMapping("add")
    public String displayAddContractForm(Model model) {
        model.addAttribute(new Contract());
        model.addAttribute("employees", userRepository.findUserByRoleName("ROLE_EMPLOYEE"));
        model.addAttribute("customers", userRepository.findUserByRoleName("ROLE_CUSTOMER"));
        return "contracts/add";
    }

    @PostMapping("add")
    public String processAddContractForm(@ModelAttribute @Valid Contract newContract, @RequestParam("users") List<Long> users, ModelMap model,
                                         Errors errors) {
        if (errors.hasErrors()) {
            return "contracts/add";
        }
        else {
        Collection<User> contractUsers = new ArrayList<>();
        for (User user : userRepository.findAllById(users)) {
            contractUsers.add(user);
        }
            newContract.setContractUsers(contractUsers);
            contractRepository.save(newContract);
        }
        return "redirect:/contracts";
    }

    @GetMapping("delete")
    public String displayDeleteContractForm(Model model) {
        model.addAttribute("contracts", contractRepository.findAll());
        return "contracts/delete";
    }

    @PostMapping("delete")
    public String processDeleteContractForm(@RequestParam @Valid Long contractId) {
        contractRepository.deleteById(contractId);
        return "redirect:/contracts";
    }

    @GetMapping("view/{id}")
    public String displayView(Model model, @PathVariable Long id) {
        Optional optionalContract = contractRepository.findById(id);
        if (optionalContract.isPresent()) {
            Contract contract = (Contract) optionalContract.get();
            model.addAttribute("contract", contract);
            model.addAttribute("customers", userRepository.findUserByRoleAndContract(id, "ROLE_CUSTOMER"));
            model.addAttribute("employees", userRepository.findUserByRoleAndContract(id, "ROLE_EMPLOYEE"));
            return "contracts/view";
        } else {
            return "redirect:/contracts";
        }
    }

    @GetMapping("view/{id}/update")
    public String displayUpdateContract(Model model, @PathVariable Long id) {
        Optional optionalContract = contractRepository.findById(id);
        if (optionalContract.isPresent()) {
            Contract contract = (Contract) optionalContract.get();
            model.addAttribute("contract", contract);
            model.addAttribute("employees", userRepository.findUserByRoleName("ROLE_EMPLOYEE"));
            model.addAttribute("customers", userRepository.findUserByRoleName("ROLE_CUSTOMER"));
            return "contracts/update";
        } else {
            return "redirect:/contracts";
        }
    }

    @PostMapping("view/{id}/update")
    public String processUpdateContract(Model model, @PathVariable Long id, @ModelAttribute @Valid Contract contract, @RequestParam("users") List<Long> users,
                                        Errors errors) {
        if (errors.hasErrors()) {
            return "view/{id}/update";
        }
        else {
            Collection<User> contractUsers = new ArrayList<>();
            for (User user : userRepository.findAllById(users)) {
                contractUsers.add(user);
            }
            contract.setContractUsers(contractUsers);
            contractRepository.save(contract);
        }
        return "redirect:/contracts/view/" + id;
    }
}
