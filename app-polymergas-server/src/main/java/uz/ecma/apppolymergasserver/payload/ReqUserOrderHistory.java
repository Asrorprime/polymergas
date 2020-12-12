package uz.ecma.apppolymergasserver.payload;

import lombok.Data;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.UUID;

@Data
public class ReqUserOrderHistory {

    private UUID userOrderHistoryId;

    private UUID userOrderId;

    private Step currentStep;

    private String message;

    private UUID adminId;
}
