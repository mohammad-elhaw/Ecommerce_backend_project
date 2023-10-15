package com.backend.ecommerce.event.listener;

import com.backend.ecommerce.event.RegistrationCompleteEvent;
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
@Component
@AllArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    private JWTService jwtService;
    private IAuthUserService userService;
    private EmailService emailService;

    @SneakyThrows
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        LocalUser user = event.getUser();

        // 2. Create Verification Token for the user
        String verificationToken = jwtService.generateToken(user);

        // 3. Save the Verification Token for the user
        userService.saveEmailToken(user, verificationToken);

        // 4. build the verification url to be sent to the user
        String url = event.getApplicationUrl() + "/email/verify?token=" + verificationToken;

        // 5. send the email
        log.info("Click the link to verify your registration :{} ", url);
            emailService.sendVerificationEmail(url, user);
    }
}
