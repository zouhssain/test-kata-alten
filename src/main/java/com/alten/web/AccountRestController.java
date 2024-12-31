package com.alten.web;

import com.alten.entities.AppUser;
import com.alten.service.AccountService;
import com.alten.utils.JWTUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/account")
    public List<AppUser> appUsers(){
        return accountService.listUsers();
    }

    @PostMapping(path = "/account")
    public AppUser createAccount(@RequestBody AppUser appUser){
        accountService.addNewUser(appUser);
        return appUser;
    }

    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String authToken = request.getHeader(JWTUtil.AUTH_HEADER);
        if(authToken!=null && authToken.startsWith(JWTUtil.PREFIX)){
            try {
                String jwt = authToken.substring(JWTUtil.PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String email = decodedJWT.getSubject();
                AppUser appUser = accountService.loadUserByEmail(email);
                String JwtAcceseToken = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+JWTUtil.EXPIRE_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);

                Map<String,String> idToken = new HashMap<>();
                idToken.put("access-token",JwtAcceseToken);
                idToken.put("refresh-token",jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);
            }catch (Exception e){
                throw e;
            }
        }else {
            throw new RuntimeException("Refresh Token required !");
        }
    }

}