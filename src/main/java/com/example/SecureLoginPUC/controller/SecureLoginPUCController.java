package com.example.SecureLoginPUC.controller;

import com.example.SecureLoginPUC.repository.UserJsonRepository;
import com.example.SecureLoginPUC.service.EmailService;
import com.example.SecureLoginPUC.service.PasswordResetService;
import com.example.SecureLoginPUC.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecureLoginPUCController {

    private final UserService userService;
    private final UserJsonRepository userRepository;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;
    private final String baseUrl;

    public SecureLoginPUCController(UserService userService,
                                    UserJsonRepository userRepository,
                                    PasswordResetService passwordResetService,
                                    EmailService emailService,
                                    @Value("${app.base-url}") String baseUrl) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
        this.baseUrl = baseUrl;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        try {
            userService.register(username, email, password, confirmPassword);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            return "register";
        }
    }

    @GetMapping("/recoverpassword")
    public String recoverPasswordPage() {
        return "recoverpassword";
    }

    @PostMapping("/recoverpassword")
    public String recoverPassword(@RequestParam String email, Model model) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = passwordResetService.createToken(user.getEmail());
            emailService.sendRecoveryEmail(user.getEmail(), baseUrl + "/resetpassword?token=" + token);
        });
        model.addAttribute("mensagem", "Se este email estiver cadastrado, enviaremos as instruções de recuperação.");
        return "recoverpassword";
    }

    @GetMapping("/resetpassword")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        if (passwordResetService.getEmail(token) == null) {
            model.addAttribute("erro", "Link inválido ou já utilizado.");
        }
        model.addAttribute("token", token);
        return "resetpassword";
    }

    @PostMapping("/resetpassword")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                Model model) {
        String email = passwordResetService.getEmail(token);
        model.addAttribute("token", token);

        if (email == null) {
            model.addAttribute("erro", "Link inválido ou já utilizado.");
            return "resetpassword";
        }
        try {
            userService.updatePassword(email, password, confirmPassword);
            passwordResetService.invalidate(token);
            return "redirect:/login?reset";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "resetpassword";
        }
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("admin", authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        return "home";
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "admin";
    }
}
