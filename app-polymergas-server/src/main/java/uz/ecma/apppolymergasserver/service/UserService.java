package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ecma.apppolymergasserver.entity.Attachment;
import uz.ecma.apppolymergasserver.entity.Role;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.entity.enums.RoleName;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.*;
import uz.ecma.apppolymergasserver.repository.AttachmentRepository;
import uz.ecma.apppolymergasserver.repository.RoleRepository;
import uz.ecma.apppolymergasserver.repository.UserRepository;
import uz.ecma.apppolymergasserver.security.JwtTokenProvider;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MessageSource messageSource;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AttachmentRepository attachmentRepository;

    public ResUploadFile getResUploadFile(Attachment attachment) {
        return new ResUploadFile(
                attachment.getId(),
                attachment.getName(),
                "/api/file/" + attachment.getId(),
                attachment.getContentType(),
                attachment.getSize()
        );
    }

    public ResUser getResUser(User user) {
        return new ResUser(
                user.getId(),
                user.getPhoneNumber(),
                user.getAdditionalPhone(),
                user.getFullName(),
                user.isEnabled(),
                user.getPhoto() == null ? null : getResUploadFile(user.getPhoto()),
                user.getRoles().stream().map(Role::getId).collect(Collectors.toList())
        );
    }

    public ResUser getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found", "id", id));
        return getResUser(user);
    }

    public ApiResponse changePassword(ReqPassword request, User user) {
        if (request.getPassword().equals(request.getPrePassword())) {
            if (checkPassword(request.getOldPassword(), user)) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                userRepository.save(user);
                String jwt = jwtTokenProvider.generateToken1(user);
                return new ApiResponse(jwt, true);
            } else {
                return new ApiResponse("Eski parol xato", false);
            }
        } else {
            return new ApiResponse("Yangi va tasdiqlovchi parol mos emas", false);
        }
    }

    public ApiResponse changePhoneNumber(ReqPassword request, User user) {
        if (checkPassword(request.getOldPassword(), user)) {
            if (user.getPhoneNumber().equals(request.getPhoneNumber())) {
                if (!userRepository.findByPhoneNumberOrAdditionalPhone(request.getNewPhoneNumber(), request.getNewPhoneNumber()).isPresent()) {
                    user.setPhoneNumber(request.getNewPhoneNumber());
                    userRepository.save(user);
                    return new ApiResponse("Foydalanuvchi telefon raqami nomi o'zgartirildi!", true);
                } else {
                    return new ApiResponse("Bunday telefon raqami band", false);
                }
            } else {
                return new ApiResponse("Eski raqam xato", false);
            }
        } else {
            return new ApiResponse("Parol xato", false);
        }

    }

    public ApiResponse addAdditionalPhone(String additionalPhone, UUID userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!", "id", userId));
            if (!userRepository.findByPhoneNumberOrAdditionalPhone(additionalPhone, additionalPhone).isPresent()) {
                user.setAdditionalPhone(additionalPhone);
                userRepository.save(user);
                return new ApiResponse("Foydalanuvchi telefon raqami o'zgartirildi!", true);
            } else {
                return new ApiResponse("Bunday telefon raqam tizimda mavjud!", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    private Boolean checkPassword(String oldPassword, User user) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public ApiResponse disableUser(UUID id, boolean enable) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found!", "id", id));
            user.setEnabled(enable);
            userRepository.save(user);
            return new ApiResponse("Muvoffaqiyatli o'zgartirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponse addToAdmin(UUID id, boolean isAdmin) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found!", "id", id));
            if (isAdmin) {
                user.getRoles().add(roleRepository.findByName(RoleName.ROLE_ADMIN).get());
            } else {
                user.getRoles().remove(roleRepository.findByName(RoleName.ROLE_ADMIN).get());
            }
            userRepository.save(user);
            return new ApiResponse("Muvoffaqiyatli o'zgartirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }


    public ApiResponseModel getUsersBySearch(int page, int size, String searchName, String sortType, boolean admins) {
        List<User> users = null;
        PageImpl<ResUser> userPage = null;
        if (admins) {
            switch (sortType) {
                case "enabled":
                    users = userRepository.findAllByEnabledIsTrueAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(searchName, page, size);
                    userPage = new PageImpl<ResUser>(
                            users.stream().map(this::getResUser).collect(Collectors.toList()),
                            CommonUtils.getPageable(page, size),
                            userRepository.findAllByEnabledIsTrueAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsCount(searchName));
                    break;
                case "unEnabled":
                    users = userRepository.findAllByEnabledIsFalseAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(searchName, page, size);
                    userPage = new PageImpl<ResUser>(
                            users.stream().map(this::getResUser).collect(Collectors.toList()),
                            CommonUtils.getPageable(page, size),
                            userRepository.findAllByEnabledIsFalseAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneCount(searchName));
                    break;
                default:
                    users = userRepository.findAllByRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(searchName, page, size);
                    userPage = new PageImpl<ResUser>(
                            users.stream().map(this::getResUser).collect(Collectors.toList()),
                            CommonUtils.getPageable(page, size),
                            userRepository.findAllByRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneCount(searchName));
                    break;
            }
            return new ApiResponseModel(true, "Administratorlar", userPage);
        } else {
            switch (sortType) {
                case "enabled":
                    users = userRepository.findAllByEnabledIsTrueAndRolesNotInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(searchName, page, size);
                    userPage = new PageImpl<ResUser>(
                            users.stream().map(this::getResUser).collect(Collectors.toList()),
                            CommonUtils.getPageable(page, size),
                            userRepository.findAllByEnabledIsTrueAndRolesNotInAndPhoneNumberCount(searchName)
                    );
                    break;
                case "unEnabled":
                    users = userRepository.findAllByEnabledIsFalseAndRolesNotInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(searchName, page, size);
                    userPage = new PageImpl<ResUser>(
                            users.stream().map(this::getResUser).collect(Collectors.toList()),
                            CommonUtils.getPageable(page, size),
                            userRepository.findAllByEnabledIsFalseAndRolesNotInAndPhoneNumberCount(searchName)
                    );
                    break;
                default:
                    users = userRepository.findAllByRolesNotInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(searchName, page, size);
                    userPage = new PageImpl<ResUser>(
                            users.stream().map(this::getResUser).collect(Collectors.toList()),
                            CommonUtils.getPageable(page, size),
                            userRepository.findAllByRolesNotInAndPhoneNumberOrderByCreatedAtDescCount(searchName)
                    );
                    break;
            }
            return new ApiResponseModel(true, "Foydalanuvchilar", userPage);
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll().stream().filter(user -> user.getRoles().size() < 2).collect(Collectors.toList());
    }

    public List<User> getAdmins() {
        return userRepository.findAll().stream().filter(user -> user.getRoles().size() > 1).collect(Collectors.toList());
    }

}
