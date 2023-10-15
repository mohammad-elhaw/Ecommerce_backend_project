package com.backend.ecommerce.event.listener;

import com.backend.ecommerce.event.ResetPasswordEvent;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.service.EmailService;
import com.backend.ecommerce.service.JWTService;
import com.backend.ecommerce.service.interfaces.IAuthUserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class ResetPasswordEventListener implements ApplicationListener<ResetPasswordEvent> {

    private JWTService jwtService;
    private IAuthUserService userService;
    private EmailService emailService;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ResetPasswordEvent event) {

        LocalUser user = event.getUser();

        String resetToken = jwtService.generateToken(user);

        userService.saveResetToken(user, resetToken);

        String url = event.getClientUrl() + "/auth/password/reset?token=" + resetToken;

        log.info("Click the link to reset password :{} ", url);
        emailService.sendResetEmail(url, user);

    }
}
