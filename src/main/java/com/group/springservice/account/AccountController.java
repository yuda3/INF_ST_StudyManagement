package com.group.springservice.account;

import com.group.springservice.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;


    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder binder){
        binder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signup(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubMit(@Validated SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword()) // TODO encoding
                .emailVerified(false)
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyCreatedByWeb(true)
                .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Confirm Sign Up");
        mailMessage.setTo("/check-email-token?token="+ newAccount.getEmailCheckToken() +"&email="+ newAccount.getEmail());
        mailSender.send(mailMessage);
        return "redirect:/";
    }


}
