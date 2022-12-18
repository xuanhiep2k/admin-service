package com.example.adminservice.config.security;

import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomAuthorizationFilter extends BasicAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    public CustomAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String tokenBearer = request.getHeader(Constants.SECURITY.HEADER_STRING);
        if (tokenBearer != null) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (request.getRequestURI() != null && !request.getRequestURI().startsWith("/auth")) {
            throw new IOException("Authentication error");
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenBearer = request.getHeader(Constants.SECURITY.HEADER_STRING);
        if (tokenBearer != null) {
            CustomUserDetails user = null;
            try {
                // parse the token.
                user = jwtTokenUtil.getUserFromToken(jwtTokenUtil.getTokenFromBearer(tokenBearer));
            } catch (Exception ex) {
                request.setAttribute("expired", ex.getMessage());
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                ObjectMapper mapper = new ObjectMapper();
                ResponseObject errorData = new ResponseObject();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                errorData.setCode(Constants.RESPONSE_CODE.UNAUTHORIZED);
                errorData.setMessage("Token không đúng hoặc đã hết hạn");
                out.print(mapper.writeValueAsString(errorData));
                out.flush();
                logger.error(ex.getMessage(), ex);
            }
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            }
            return null;
        }
        return null;
    }

}
