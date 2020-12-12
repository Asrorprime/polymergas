package uz.ecma.apppolymergasserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.ecma.apppolymergasserver.entity.User;
import uz.ecma.apppolymergasserver.payload.ApiResponse;
import uz.ecma.apppolymergasserver.payload.ApiResponseModel;
import uz.ecma.apppolymergasserver.repository.NotificationRepository;
import uz.ecma.apppolymergasserver.security.CurrentUser;
import uz.ecma.apppolymergasserver.service.NotificationService;
import uz.ecma.apppolymergasserver.utils.AppConstants;

import java.util.UUID;

@RestController
@RequestMapping("api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationRepository notificationRepository;


    @GetMapping("/{id}")
    public ApiResponseModel getNotification(@PathVariable UUID id) {
        return notificationService.getNotification(id);
    }

    @GetMapping("/unReadNotifications")
    public ApiResponseModel getUserUnreadNotifications(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                       @CurrentUser User user, @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return notificationService.getUserUnreadNotifications(user, page, size);
    }

    @GetMapping
    public ApiResponseModel getUserAllNotifications(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                    @CurrentUser User user, @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return notificationService.getUserAllNotifications(user, page, size);
    }

    @GetMapping("/readNotification/{id}")
    public ApiResponse readNotification(@PathVariable UUID id, @CurrentUser User user) {
        return notificationService.readNotification(id, user);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteNotification(@PathVariable UUID id) {
        return notificationService.deleteNotification(id);
    }
}
