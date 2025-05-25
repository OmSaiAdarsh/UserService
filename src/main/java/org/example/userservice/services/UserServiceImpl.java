package org.example.userservice.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.userservice.models.Token;
import org.example.userservice.models.User;
import org.example.userservice.repositories.TokenRepository;
import org.example.userservice.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
public class UserServiceImpl implements UserService {
    private final TokenRepository tokenRepository;
    UserRepository userRepository;
    // happy screen
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token login(String email, String password) {
        // check nd validate the user and then return the token
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()){
            // throw user not exists Exception
            return null;
        }
        User user = optionalUser.get();
        if (! bCryptPasswordEncoder.matches(password, user.getPassword())){
            // throw incorrect password exception
            return null;
        }

        Token token = new Token();
        // UUID is also one of teh industry pattern but currently we are using Common Langs Library.
        token.setValue(UUID.randomUUID().toString());
        //token.setValue(randomAlphanumeric(128));
        token.setUser(user);

        // get the Expiry date using calendar
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        Date expiration = calendar.getTime();

        token.setExpiryAt(expiration);

        tokenRepository.save(token);
        return token;
    }

    @Override
    public User register(String name, String email, String password) {
        List<User> users = userRepository.findAllByEmail(email);
        if (users.size()>0){
            return null;
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        return user;
    }

    @Override
    public User validateToken(String tokenValue) {
        Optional<Token> optionalToken= tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(tokenValue, false, new Date());
        if (optionalToken.isEmpty()){
            return null;
        }
        Token token = optionalToken.get();

        return token.getUser();
    }

    @Override
    public boolean validateTokenUpdated(String token, String email){
        if(token == null || email == null || token.isEmpty() || email.isEmpty()){
            return false;
        }
        //System.out.println(email);
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());
        if (optionalToken.isEmpty()){
            return false;
        }
        Token tokenVal = optionalToken.get();

        User user = tokenVal.getUser();
        //System.out.println(email);
        //System.out.println(user.getEmail());
        if (!email.equals(user.getEmail())){
            return false;
        }
        return true;
    }

    @Override
    public boolean logout(String email, String tokenValue) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(tokenValue, false, new Date());
        //check if token exists or not
        if (optionalToken.isEmpty()){
            return false;
        }
        // check the token value is same or not, its anyhow same as it is derived from that, (redundancy)
        Token token = optionalToken.get();
        if (!token.getValue().equals(tokenValue)){
            return false;
        }
        // Check if it belongs to the same user
        if(!token.getUser().getEmail().equals(email)){
            return false;
        }
        // checking the expiry or deleted condition
        if (token.getExpiryAt().before(new Date()) || token.isDeleted()){
            return false;
        }
        // now the token is valid, try to expire it
        token.setDeleted(true);
        token.setExpiryAt(new Date());
        tokenRepository.save(token);
        return true;
    }
}
