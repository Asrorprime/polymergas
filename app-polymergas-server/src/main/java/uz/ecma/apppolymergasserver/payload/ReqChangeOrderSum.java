package uz.ecma.apppolymergasserver.payload;

import lombok.Data;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.UUID;

@Data
public class ReqChangeOrderSum {
    private UUID userOrderHistoryId;

    private UUID userOrderId;

    private Step currentStep;

    private String message;

    private UUID adminId;

    private boolean payed;

    private Double price;

    private boolean priceAdd;
}
