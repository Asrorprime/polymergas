package uz.ecma.apppolymergasserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ReqSignUp;
import uz.ecma.apppolymergasserver.repository.RoleRepository;
import uz.ecma.apppolymergasserver.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumberOrAdditionalPhone(phoneNumber,phoneNumber).orElseThrow(() -> new UsernameNotFoundException(phoneNumber));
    }


    public UserDetails loadUserById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User id not found: " + userId));
    }


    public ApiResponse register(ReqSignUp reqSignUp) {
        Optional<User> optionalUser = userRepository.findByPhoneNumberOrAdditionalPhone(reqSignUp.getPhoneNumber(),reqSignUp.getPhoneNumber());
        if (optionalUser.isPresent()) {
            return new ApiResponse("Bunday telefon raqam tizimda mavjud", false);
        } else {
            User user = new User(
                    reqSignUp.getPhoneNumber(),
                    passwordEncoder.encode(reqSignUp.getPassword()),
                    reqSignUp.getFullName(),
                    roleRepository.findAllById(reqSignUp.getRolesId()));
            userRepository.save(user);
            return new ApiResponse("Muvoffaqiyatli yaratildi!", true);
        }
    }


}
