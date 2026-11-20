package kz.iitu.springlab.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // FACTOR_PASSWORD - Spring Security 7-нің ішкі белгісі, рөл емес, сондықтан тек ROLE_ көрсетеміз
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(a -> a.startsWith("ROLE_"))
                    .sorted()
                    .toList();
            model.addAttribute("username", auth.getName());
            model.addAttribute("roles", roles);
            model.addAttribute("isAdmin", roles.contains("ROLE_ADMIN"));
        }
        return "index";
    }
}