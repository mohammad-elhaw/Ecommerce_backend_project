package com.backend.ecommerce.service;

import com.backend.ecommerce.exception.RefreshTokenException;
import com.backend.ecommerce.model.LocalUser;
import com.backend.ecommerce.model.RefreshToken;
import com.backend.ecommerce.model.repository.RefreshTokenRepo;
import com.backend.ecommerce.model.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-token.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    @Autowired
    private UserRepo userRepo;

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepo.findByRefreshToken(token);
    }

    public RefreshToken generateRefreshToken(String email){
        LocalUser user = userRepo.findByEmailIgnoreCase(email).get();
        if(refreshTokenRepo.findByUser(user).isPresent()){
            //RefreshToken refreshToken = refreshTokenRepo.findByUser(user).get();
            //refreshTokenRepo.delete(refreshToken);
            refreshTokenRepo.deleteByUser(user);
        }

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepo.findByEmailIgnoreCase(email).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshTokenRepo.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) throws RefreshTokenException {
        RefreshToken refreshToken = refreshTokenRepo.findByRefreshToken(token.getRefreshToken())
                .orElseThrow(()->new RefreshTokenException(token.getRefreshToken(), "Given refresh token doesn't exists in database"));

        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepo.delete(token);
            throw new RefreshTokenException(token.getRefreshToken(), "Refresh token was expired. Please make a new login request");
        }
        return token;
    }

}
