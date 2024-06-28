package com.group.springservice.account;

import com.group.springservice.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JavaMailSender mailSender;

    @Autowired
    private AccountRepository accountRepository;

    @DisplayName("間違った入力の場合テスト失敗")
    @Test
    void checkEmailToken_with_wrong_input()  throws Exception {
        mockMvc.perform(get("/check-email-token")
                .param("token", "asdfasdf")
                .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(unauthenticated())
        ;
    }

    @DisplayName("メールトークンが存在しているか確認")
    @Test
    void checkEmailToken_with_right_input()  throws Exception {
        Account account = Account.builder()
                .email("yuda3@hotmail.com")
                .password("123456789")
                .nickname("keesun")
                .build();
        Account saved = accountRepository.save(account);
        saved.generateEmailToken();
        mockMvc.perform(get("/check-email-token")
                        .param("token", saved.getEmailCheckToken())
                        .param("email", saved.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"))
        ;
    }


    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated())
        ;
    }
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "kimkim")
                        .param("email", "kim")
                        .param("password", "kim12")
                        .with(csrf())
                ).andExpect(status().isOk())

                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated())
                .andDo(print());
    }

    @Test
    void signUpSubmit_with_right_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "kimkim")
                        .param("email", "kim@gamil.com")
                        .param("password", "kim1212399")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
            ;
        Account account = accountRepository.findByEmail("kim@gamil.com");
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "kim1212399");
        assertNotNull(account.getEmailCheckToken());
        then(mailSender).should().send(any(SimpleMailMessage.class));
    }

}