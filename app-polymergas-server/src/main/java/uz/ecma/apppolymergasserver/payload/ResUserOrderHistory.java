package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUserOrderHistory {

    private UUID userOrderHistoryId;

    private UUID resUserOrderId;

    private Step currentStep;

    private String message;

    private String createdAt;

    private ResUser admin;
}
