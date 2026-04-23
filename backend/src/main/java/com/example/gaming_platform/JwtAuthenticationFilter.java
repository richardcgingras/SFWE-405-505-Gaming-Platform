package com.example.gaming_platform;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.gaming_platform.entity.UserProfile;
import com.example.gaming_platform.service.JwtTokenProvider;
import com.example.gaming_platform.service.UserProfileService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final UserProfileService userProfileService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserProfileService userProfileService) {
        this.tokenProvider = tokenProvider;
        this.userProfileService = userProfileService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            System.out.println("Request URL: " + request.getRequestURL());
            System.out.println("Request method: " + request.getMethod());
            String jwt = extractJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromToken(jwt);
                UserProfile userProfile = userProfileService.getUserProfileById(userId);
                if (userProfile != null) {
                    UserPrincipal userPrincipal = UserPrincipal.create(userProfile);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userPrincipal,
                            null,
                            userPrincipal.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // Propagate token validation info to request
                    request.setAttribute("validatedJwt", jwt);
                    request.setAttribute("validToken", Boolean.TRUE);
                    request.setAttribute("currentUserId", userId);
                    request.setAttribute("username", userProfile.getUserName());
                    System.out.println("JWT Token validated for user: " + userProfile.getUserName());
                } else {
                    System.err.println("User not found for ID: " + userId);
                }
            } else {
                System.out.println("JWT token is missing or invalid: " + jwt);
            }
        } catch (Exception ex) {
            System.err.println("Could not set user authentication in security context: " + ex.getMessage());
            ex.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("Authorization header: " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}