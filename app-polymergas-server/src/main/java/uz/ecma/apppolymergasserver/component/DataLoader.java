package uz.ecma.apppolymergasserver.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.repository.*;

import java.util.Optional;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UserOrderHistoryRepository userOrderHistoryRepository;
    @Autowired
    UserOrderRepository userOrderRepository;
    @Autowired
    SelectedProductRepository selectedProductRepository;


    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            userRepository.save(new User(
                    "12345678",
                    passwordEncoder.encode("12345678"),
                    "Super Admin",
                    roleRepository.findAll()
            ));
        }
//        Optional<User> byId = userRepository.findById(UUID.fromString("40500d66-e33d-44ba-b7a4-de9b88db8300"));
//        User user = byId.get();
//        user.setChatId(Long.valueOf(150131517));
//        userRepository.save(user);

    }
}
