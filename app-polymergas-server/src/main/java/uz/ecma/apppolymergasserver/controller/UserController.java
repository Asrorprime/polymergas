package uz.ecma.apppolymergasserver.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.payload.ReqPassword;
import uz.ecma.apppolymergasserver.repository.UserRepository;
import uz.ecma.apppolymergasserver.security.AuthService;
import uz.ecma.apppolymergasserver.security.CurrentUser;
import uz.ecma.apppolymergasserver.service.UserService;
import uz.ecma.apppolymergasserver.utils.AppConstants;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    final UserRepository userRepository;
    final UserService userService;
    final PasswordEncoder passwordEncoder;
    final AuthService authService;

    public UserController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }


    @GetMapping("/me")
    public HttpEntity<?> getUser(@CurrentUser User user) {
        return ResponseEntity.ok(new ApiResponseModel(true, user));
    }

    @PutMapping("/changePassword")
    public HttpEntity<?> editPassword(@RequestBody ReqPassword reqPassword, @CurrentUser User user) {
        return ResponseEntity.ok().body(userService.changePassword(reqPassword, user));
    }

    @PutMapping("/changePhoneNumber")
    public HttpEntity<?> changePhoneNumber(@RequestBody ReqPassword reqPassword, @CurrentUser User user) {
        return ResponseEntity.ok().body(userService.changePhoneNumber(reqPassword, user));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/addAdditionalPhone/{userId}")
    public ApiResponse addAdditionalPhone(@PathVariable UUID userId,@RequestParam(value = "additionalPhone")String additionalPhone) {
        return userService.addAdditionalPhone(additionalPhone,userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/disableUser/{id}")
    public ApiResponse disableUser(@PathVariable UUID id, @RequestParam(value = "enable", defaultValue = "true") boolean enable) {
        return userService.disableUser(id, enable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/addToAdmin/{id}")
    public ApiResponse addToAdmin(@PathVariable UUID id, @RequestParam(value = "isAdmin", defaultValue = "true") boolean isAdmin) {
        return userService.addToAdmin(id, isAdmin);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public HttpEntity<?> getUsersBySearch(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "sortType", defaultValue = "all") String sortType,
            @RequestParam(value = "searchName", defaultValue = "") String searchName,
            @RequestParam(value = "admins", defaultValue = "false") boolean admins

    ) {
        return ResponseEntity.ok(userService.getUsersBySearch(page, size, searchName, sortType, admins));
    }

    @PostMapping("/searchuser")
    public HttpEntity<?> searchUser(@RequestBody Map<String, Object> attributes) {
        String search = (String) attributes.get("search");
        return ResponseEntity.ok().body(userRepository.findAllByFullNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(search, search).stream().map(userService::getResUser).collect(Collectors.toList()));
    }

}
