package com.auth0.example;

import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
@Controller
public class CallbackController {

    @Autowired
    private AuthController controller;
    private final String redirectOnFail;
    private final String redirectOnSuccess;

    public CallbackController() {
        this.redirectOnFail = "/login";
        this.redirectOnSuccess = "https://integration-appsnew.azuga.com/";
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected void postCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    private void handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Tokens tokens = controller.handle(req);
            
            Cookie cookie = new Cookie("oauthToken-apikey", tokens.getAccessToken());
            cookie.setDomain(".azuga.com");
            cookie.setPath("/");
            cookie.setSecure(true);
            res.addCookie(cookie);
            System.out.println("cookie set : "+cookie.toString());
            System.out.println("accessToken : "+tokens.getAccessToken());
            SessionUtils.set(req, "accessToken", tokens.getAccessToken());
            System.out.println("ID Token	:	"+tokens.getIdToken());
            SessionUtils.set(req, "idToken", tokens.getIdToken());
            
            Cookie c = new Cookie("sanCookie", "232e2eer");
            c.setDomain(".azuga.com");
            c.setPath("/");
            c.setMaxAge(22222);
            res.addCookie(c);
            
            Cookie stagingCookie = new Cookie("sanStagingCookie", "232e2eer");
            stagingCookie.setDomain("staging3.azuga.com");
            stagingCookie.setPath("/");
            stagingCookie.setMaxAge(22233);
            res.addCookie(stagingCookie);
            
            Cookie genCookie = new Cookie("sanGeneralCookie", "232e2eer");
            genCookie.setDomain("azuga.com");
            genCookie.setPath("/");
            genCookie.setMaxAge(24242);
            res.addCookie(genCookie);
            
            Cookie authDev = new Cookie("sanAuthDevCookie", "232e2eer");
            authDev.setDomain("dev-sandeeps.auth0.com");
            authDev.setPath("/");
            authDev.setMaxAge(2323465);
            res.addCookie(authDev);
            
            
            
            res.sendRedirect(redirectOnSuccess+"?code="+Math.random()*1000);
        } catch (IdentityVerificationException e) {
            e.printStackTrace();
            res.sendRedirect(redirectOnFail+"?code="+Math.random()*1000);
        }
    }

}
