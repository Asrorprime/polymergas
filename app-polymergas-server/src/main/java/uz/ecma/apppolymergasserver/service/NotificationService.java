package uz.ecma.apppolymergasserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import uz.ecma.apppolymergasserver.entity.Notification;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.entity.enums.Step;
import uz.ecma.apppolymergasserver.exception.ResourceNotFoundException;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.payload.ResNotification;
import uz.ecma.apppolymergasserver.repository.NotificationRepository;
import uz.ecma.apppolymergasserver.repository.UserOrderRepository;
import uz.ecma.apppolymergasserver.repository.UserRepository;
import uz.ecma.apppolymergasserver.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserOrderRepository userOrderRepository;

    public void createNotification(String message, UUID userId, UUID userOrderId, Step messageType) {
        Notification notification = new Notification();
        notification.setUserOrderId(userOrderId);
        notification.setMessageType(messageType);
        notification.setMessage(message);
        notification.setSeen(false);
        notification.setUserId(userId);
        notificationRepository.save(notification);
    }

    public void createNotification(String message, Set<UUID> usersId, UUID userOrderId, Step messageType) {
        (new Thread(new Runnable() {
            public void run() {
                usersId.forEach(userId -> {
                    Notification notification = new Notification();
                    notification.setUserOrderId(userOrderId);
                    notification.setMessageType(messageType);
                    notification.setMessage(message);
                    notification.setSeen(false);
                    notification.setUserId(userId);
                    notificationRepository.save(notification);
                });
            }
        })).start();
    }

    public ApiResponse readNotification(UUID id, User user) {
        try {
            Notification notification = notificationRepository.findByUserIdAndId(user.getId(), id);
            notification.setSeen(true);
            notificationRepository.save(notification);
            return new ApiResponse("Xabar oʻqildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik!", false);
        }
    }

    public ApiResponseModel getUserUnreadNotifications(User user, int page, int size) {
        Page<Notification> notifications = notificationRepository.findAllByUserIdAndSeenFalseOrderByCreatedAt(user.getId(), CommonUtils.getPageable(page, size));
        Page<ResNotification> pageable = new PageImpl<>(
                notifications.getContent().stream().map(this::getResNotification).collect(Collectors.toList()),
                notifications.getPageable(),
                notifications.getTotalElements()
        );
        return new ApiResponseModel(true, "O'qilmagan bildirishnomalarim", pageable);


    }

    public ApiResponseModel getUserAllNotifications(User user, int page, int size) {
        Page<Notification> allByUserOrderByCreatedAt = notificationRepository.findAllByUserIdOrderBySeenAsc(user.getId(), CommonUtils.getPageable(page, size));
        Page<ResNotification> pageable = new PageImpl<>(
                allByUserOrderByCreatedAt.getContent().stream().map(this::getResNotification).collect(Collectors.toList()),
                allByUserOrderByCreatedAt.getPageable(),
                allByUserOrderByCreatedAt.getTotalElements()
        );
        return new ApiResponseModel(true, "Barcha bildirishnomalarim!", pageable);
    }

    public ResNotification getResNotification(Notification notification) {
        return new ResNotification(
                notification.getId(),
                notification.getMessage(),
                notification.isSeen(),
                notification.getUserId() != null ? userService.getUser(notification.getUserId()) : null,
                notificationRepository.findAllByUserIdAndSeenFalseOrderByCreatedAt(userService.getUser(notification.getUserId()).getId()).size(),
                notification.getMessageType(),
                notification.getCreatedAt() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm").format(notification.getCreatedAt()) : null,
                notification.getUserOrderId(),
                notification.getUserOrderId()==null ? null : userOrderRepository.findById(notification.getUserOrderId()).get().getUniqueOrder().getNumber()

        );
    }

    public ApiResponseModel getNotification(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("notification", "id", id));
        notification.setSeen(true);
        Notification save = notificationRepository.save(notification);
        return new ApiResponseModel(true, "Notification", getResNotification(save));
    }

    public ApiResponse deleteNotification(UUID id) {
        try {
            notificationRepository.deleteById(id);
            return new ApiResponse("Xabar oʻchirildi!", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse("Xatolik", false);
        }
    }
}
