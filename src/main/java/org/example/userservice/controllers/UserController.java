package org.example.userservice.controllers;

import org.example.userservice.Exceptions.InvalidUserException;
import org.example.userservice.dtos.*;
import org.example.userservice.models.Token;
import org.example.userservice.models.User;
import org.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto request){
        User user = userService.register(request.getName(),
                request.getEmail(),
                request.getPassword());

        return SignUpResponseDto.from(user);

        //return new SignUpResponseDto();
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) throws InvalidUserException{
        Token token = userService.login(request.getEmail(), request.getPassword());
        if (token == null){
            throw new InvalidUserException();
        }
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token.getValue());
        return response;
        // return new LoginResponseDto();
    }

    @PostMapping("/logOut")
    public ResponseEntity<Void> logout(@RequestBody LogOutDto request){
        boolean isvalid = userService.logout(request.getEmail(), request.getTokenValue());
        HttpStatus status = isvalid ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(status);
        //return null;
    }

    @GetMapping("/validateToken/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable("token")  String token){
        User user = userService.validateToken(token);
        ResponseEntity responseEntity;
        if (user == null){
             responseEntity = new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        else
            responseEntity = new ResponseEntity<>(true, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenDto tokenDto){
        boolean isValidToken = userService.validateTokenUpdated(tokenDto.getTokenValue(), tokenDto.getEmail());
        if (! isValidToken){
            System.out.println(new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED).getBody());
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.UNAUTHORIZED);// false;
        }
        else {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
    }
}
