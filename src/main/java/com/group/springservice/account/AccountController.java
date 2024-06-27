package com.group.springservice.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signup(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }
}
