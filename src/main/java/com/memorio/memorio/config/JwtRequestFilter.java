package com.memorio.memorio.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.memorio.memorio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Folgendes Feld wird in den Header der Requests durchsucht, dieses Feld *muss* zwingend gesetzt werden!!
        final String requestTokenHeader = request.getHeader("Authtoken");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null) {
            jwtToken = requestTokenHeader;
            try {
                // Nach dem User suchen fuer den das Token generiert wurde um eine Korrelation zwischen Token und User zu scahffen
                // Dadurch das Benutzerinfos fuer die Tokengenerierung verwendet wurden ist das moeglich
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Token konnte nicht gelesen werden");
            } catch (ExpiredJwtException e) {
                System.out.println("Token ist abgelaufen");
            }
        } else {
            // Wenn kein User fuer das Token gefunden wurde ODER es gar kein Token bislang gibt Ausgabe durch logger
            logger.warn("Kein Token gesetzt");
        }

        // Sobald ein User gefunden wurde
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userService.loadUserByUsername(username);

            // Laden des Users und validieren
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Sobald der User verifiziert wurde, es also ein passendes Token gibt
                // Weitergabe an Spring
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
