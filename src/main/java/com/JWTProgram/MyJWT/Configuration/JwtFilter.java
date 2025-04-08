package com.JWTProgram.MyJWT.Configuration;

import com.JWTProgram.MyJWT.UserServices.JWTServices;
import com.JWTProgram.MyJWT.UserServices.MyUserDetailServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTServices jwtServices;

    @Autowired
    ApplicationContext  applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token= null;
        String username=null;

        if (authHeader != null  && authHeader.startsWith("Bearer"))  {
            token = authHeader.substring(7);
            username =jwtServices.extractToken();
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails=applicationContext.getBean(MyUserDetailServices.class).loadUserByUsername(username);

            if (jwtServices.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken Authtoken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                Authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(Authtoken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
