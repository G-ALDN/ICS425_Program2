package edu.metrostate.program2;

import edu.metrostate.program2.model.AppUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_USER"));

        if (isUser) {
            setDefaultTargetUrl("/user/home");
        }
        else{
            setDefaultTargetUrl("/");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
