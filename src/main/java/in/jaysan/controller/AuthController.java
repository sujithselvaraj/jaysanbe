package in.jaysan.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/check")
    public Map<String, Boolean> checkAuthentication(Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        return Map.of("authenticated", isAuthenticated);
    }
}
