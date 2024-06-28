package com.group.springservice.account;

import com.group.springservice.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final JavaMailSender mailSender;

    public void processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailToken();
        sendSignUpConfirmEmail(newAccount);
    }

    public Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword()) // TODO encoding
                .emailVerified(false)
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyCreatedByWeb(true)
                .build();
        return accountRepository.save(account);
    }

    private void sendSignUpConfirmEmail(Account newAccount) {
        newAccount.generateEmailToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Confirm Sign Up");
        mailMessage.setTo("/check-email-token?token="+ newAccount.getEmailCheckToken() +"&email="+ newAccount.getEmail());
        mailSender.send(mailMessage);
    }


}
