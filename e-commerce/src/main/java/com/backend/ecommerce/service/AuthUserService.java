package com.backend.ecommerce.service;

import com.backend.ecommerce.api.dto.*;
import com.backend.ecommerce.event.RegistrationCompleteEvent;
import com.backend.ecommerce.event.ResetPasswordEvent;
import com.backend.ecommerce.exception.*;
import com.backend.ecommerce.model.*;
import com.backend.ecommerce.model.repository.*;
import com.backend.ecommerce.service.interfaces.ICartService;
import com.backend.ecommerce.service.interfaces.IAuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthUserService implements IAuthUserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final VerificationTokenRepo verificationTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final RefreshTokenService refreshTokenService;
    private final ApplicationEventPublisher publisher;
    private final AccessTokenRepo accessTokenRepo;
    private final ResetTokenRepo resetTokenRepo;
    private final ICartService cartService;
    @Value("${jwt.expiration}")
    private Long JWT_EXPIRATION_DATE;
    @Value("${client.url}")
    private String CLIENT_URL;

    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) throws InvalidEmailOrPasswordException, UserIsNotEnableException {
        Optional<LocalUser> user = userRepo.findByEmailIgnoreCase(loginRequest.getEmail());
        if(user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            if(user.get().isEnabled()){
                Authentication authentication =
                        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
                MyCustomUserDetails userDetails = (MyCustomUserDetails) authentication.getPrincipal();

                Optional<AccessToken> accessToken = accessTokenRepo.findByUser(userDetails.getUser());
                accessToken.ifPresent(accessTokenRepo::delete);

                String token = jwtService.generateToken(userDetails.getUser());
                AccessToken theToken = new AccessToken();
                theToken.setUser(userDetails.getUser());
                theToken.setAccessToken(token);
                theToken.setExpiryDate(Instant.now().plusMillis(JWT_EXPIRATION_DATE));
                accessTokenRepo.save(theToken);

                ResponseCookie jwtCookie = jwtService.generateJwtCookie(token);
                RefreshToken refreshToken = refreshTokenService.generateRefreshToken(userDetails.getUsername());
                ResponseCookie jwtRefreshCookie = jwtService.generateJwtRefreshCookie(refreshToken.getRefreshToken());
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                        .body(new LoginResponse(userDetails.getUser().getUserId(), userDetails.getUsername(), userDetails.getUser().getRoles()));
            }else{
                throw new UserIsNotEnableException("activate your account to login.");
            }
        }else{
            throw new InvalidEmailOrPasswordException("Invalid Email Or Password.");
        }
    }

    @Override
    public void createUser(RegisterRequest registerRequest) throws UserAlreadyExistsException {

        if(userRepo.findByEmailIgnoreCase(registerRequest.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User Already Exists.");
        }

        Role userRole = roleRepo.findByRoleName("ROLE_USER");
        LocalUser user = new LocalUser();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.getRoles().add(userRole);
        user.setCreatedAt(LocalDateTime.now());
        user.setPhoneNumber(registerRequest.getPhoneNumber());

        LocalUser savedUser =userRepo.save(user);
        cartService.createCart(savedUser);

        userRepo.save(user);

        CompletableFuture.runAsync(()->publisher.publishEvent(new RegistrationCompleteEvent(savedUser, CLIENT_URL)));
        //publisher.publishEvent(new RegistrationCompleteEvent(savedUser, CLIENT_URL));
    }

//    private String applicationUrl(HttpServletRequest request) {
//        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//    }

    @Override
    public void saveEmailToken(LocalUser user, String token) {
        Optional<VerificationToken> theToken = verificationTokenRepo.findByUser(user);
        theToken.ifPresent(verificationTokenRepo::delete);
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);
    }

    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws RefreshTokenException {
        String refreshToken = jwtService.getJwtRefreshFromCookie(request);
        if(refreshToken != null && refreshToken.length() > 0){

            refreshTokenService.verifyExpiration(refreshToken);

            return refreshTokenService.findByToken(refreshToken)
                    .map(RefreshToken::getUser)
                    .map(localUser -> {
                        String jwtToken = createAccessToken(localUser);
                        ResponseCookie jwtCookie = jwtService.generateJwtCookie(jwtToken);

                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                                .body(new SuccessMessage(
                                        HttpStatus.OK.value(),
                                        new Date(),
                                        "Token is refreshed successfully."));
                    }).get();
        }
        return ResponseEntity.
                badRequest().
                body(new ErrorMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        new Date(),
                        "Refreshed token is empty"));
    }

    private String createAccessToken(LocalUser user) {
        Optional<AccessToken> tokenDB = accessTokenRepo.findByUser(user);
        tokenDB.ifPresent(accessTokenRepo::delete);
        String jwtAccess =jwtService.generateToken(user);
        AccessToken accessToken = new AccessToken(jwtAccess,user, Instant.now().plusMillis(JWT_EXPIRATION_DATE));
        accessTokenRepo.save(accessToken);
        return jwtAccess;
    }

    @Override
    public ResponseEntity<?> verifyEmail(String token) {
        Optional<VerificationToken> theToken = verificationTokenRepo.findByToken(token);
        if(theToken.isPresent()){
            LocalUser user = theToken.get().getUser();
            if(user.isEnabled()){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new SuccessMessage(
                                HttpStatus.OK.value(),
                                new Date(),
                                "this account has already verified, you can login."
                        ));
            }
            if(!jwtService.validateEmailToken(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorMessage(
                                HttpStatus.BAD_REQUEST.value(),
                                new Date(),
                                "Token is expired verify the account again."
                        ));
            }
            user.setEnabled(true);
            userRepo.save(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessMessage(
                            HttpStatus.OK.value(),
                            new Date(),
                            "now you can login."
                    ));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(
                            HttpStatus.BAD_REQUEST.value(),
                            new Date(),
                            "invalid verification token"
                    ));
        }
    }

    @SneakyThrows
    @Override
    public void enableUser(String email){
        LocalUser user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(()->new EmailNotFoundException("this email is invalid"));

        if(user.isEnabled()) return;
        CompletableFuture.runAsync(()->publisher.publishEvent(new RegistrationCompleteEvent(user, CLIENT_URL)));
//            publisher.publishEvent(new RegistrationCompleteEvent(user.get(), CLIENT_URL));
    }

    @SneakyThrows
    @Override
    public void logoutUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        LocalUser user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(()->new EmailNotFoundException("Invalid token"));

        refreshTokenService.deleteByUser(user);
        accessTokenRepo.deleteByUser(user);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @SneakyThrows
    @Override
    public void createResetPassword(String email){
        LocalUser user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(()-> new EmailNotFoundException("Your Email is Invalid"));

        CompletableFuture.runAsync(()->publisher.publishEvent(new ResetPasswordEvent(user, CLIENT_URL)));
//        publisher.publishEvent(new ResetPasswordEvent(user.get(), CLIENT_URL));
    }

    @Override
    public void saveResetToken(LocalUser user, String token) {
        Optional<ResetToken> theToken = resetTokenRepo.findByUser(user);
        theToken.ifPresent(resetTokenRepo::delete);
        ResetToken resetToken = new ResetToken(token, user);
        resetTokenRepo.save(resetToken);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) throws InvalidResetTokenException {
        Optional<ResetToken> resetToken = resetTokenRepo.findByResetToken(request.getToken());
        if(resetToken.isEmpty() || !jwtService.validateToken(resetToken.get().getResetToken())){
            throw new InvalidResetTokenException("Reset token invalid try to reset password again.");
        }
        LocalUser user = resetToken.get().getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
    }

}
