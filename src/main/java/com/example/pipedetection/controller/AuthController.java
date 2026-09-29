package com.example.pipedetection.controller;

import com.example.pipedetection.dto.LoginRequest;
import com.example.pipedetection.dto.LoginResponse;
import com.example.pipedetection.dto.RegisterRequest;
import com.example.pipedetection.entity.Contact;
import com.example.pipedetection.repository.ContactRepository;
import com.example.pipedetection.entity.AppUser;
import com.example.pipedetection.repository.AppUserRepository;
import com.example.pipedetection.security.AuthSupport;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContactRepository contactRepository;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.contactRepository = contactRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        session.setAttribute(AuthSupport.USER_SESSION_KEY, user);
        return toResponse(user);
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@Valid @RequestBody RegisterRequest request) {
        String username = request.getUsername().trim();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists");
        }

        Contact customer = new Contact();
        customer.setName(request.getDisplayName().trim());
        customer.setContactType(Contact.ContactType.MUNICIPAL_WATER_BOARD);
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        Contact savedCustomer = contactRepository.save(customer);

        AppUser user = new AppUser(
                username,
                passwordEncoder.encode(request.getPassword()),
                AppUser.Role.CUSTOMER,
                savedCustomer.getId(),
                request.getDisplayName().trim());
        AppUser savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    @GetMapping("/me")
    public LoginResponse me(HttpSession session) {
        AppUser user = AuthSupport.currentUser(session);
        if (user == null) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in");
        return toResponse(user);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }

    private LoginResponse toResponse(AppUser user) {
        return new LoginResponse(user.getUsername(), user.getDisplayName(), user.getRole().name(), user.getCustomerId());
    }
}
