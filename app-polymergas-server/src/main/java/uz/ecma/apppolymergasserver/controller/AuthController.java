package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.payload.*;
import uz.ecma.apppolymergasserver.repository.RoleRepository;
import uz.ecma.apppolymergasserver.repository.UserRepository;
import uz.ecma.apppolymergasserver.security.AuthService;
import uz.ecma.apppolymergasserver.security.CurrentUser;
import uz.ecma.apppolymergasserver.security.JwtTokenProvider;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticate;

    @Autowired
    AuthService authService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/me")
    public HttpEntity<?> getUser(@CurrentUser User user) {
        return ResponseEntity.ok(user);
    }


    @PostMapping("/register")
    public HttpEntity<?> register(@Valid @RequestBody ReqSignUp reqSignUp) {
        ApiResponse response = authService.register(reqSignUp);
        if (response.isSuccess()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(getApiToken(reqSignUp.getPhoneNumber(), reqSignUp.getPassword()));
        }
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response.getMessage());
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@Valid @RequestBody ReqSignIn reqSignIn) {
        if (userRepository.findByPhoneNumberOrAdditionalPhone(reqSignIn.getPhoneNumber(), reqSignIn.getPhoneNumber()).isPresent()) {
            User user = userRepository.findByPhoneNumberOrAdditionalPhone(reqSignIn.getPhoneNumber(), reqSignIn.getPhoneNumber()).get();
            if (user.getRoles().size() < 2) {
                return ResponseEntity.ok(new ApiResponse("Kirish huquqingiz yo'q", false));
            } else {
                return ResponseEntity.ok(getApiToken(reqSignIn.getPhoneNumber(), reqSignIn.getPassword()));
            }
        } else {
            return ResponseEntity.ok(new ApiResponse("Bunday foydalanuvchi tizimda mavjud emas!", false));
        }
    }


    public HttpEntity<?> getApiToken(String phoneNumber, String password) {
        Authentication authentication = authenticate.authenticate(
                new UsernamePasswordAuthenticationToken(phoneNumber, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }


}
