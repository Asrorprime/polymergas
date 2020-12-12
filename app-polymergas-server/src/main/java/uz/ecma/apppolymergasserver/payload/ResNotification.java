package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResNotification {
    private UUID notificationId;
    private String message;
    private boolean seen;
    private ResUser user;
    private int unreadNotificationsCount;
    private Step messageType;
    private String createdAt;
    private UUID userOrderId;
    private Integer userOrderNumber;
}
