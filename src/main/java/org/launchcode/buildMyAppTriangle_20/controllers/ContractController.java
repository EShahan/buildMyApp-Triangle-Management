package org.launchcode.buildMyAppTriangle_20.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("contracts")
public class ContractController {
    @GetMapping()
    public String index() {
        return "contracts/index";
    }
}
