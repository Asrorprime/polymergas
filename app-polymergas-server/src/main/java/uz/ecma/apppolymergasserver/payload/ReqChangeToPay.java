package uz.ecma.apppolymergasserver.payload;

import lombok.Data;
import org.glassfish.grizzly.http.util.TimeStamp;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.UUID;

@Data
public class ReqChangeToPay {
    private UUID userOrderHistoryId;

    private UUID userOrderId;

    private Step currentStep;

    private String message;

    private UUID adminId;

    private boolean payed;

    private TimeStamp payDate;
}
