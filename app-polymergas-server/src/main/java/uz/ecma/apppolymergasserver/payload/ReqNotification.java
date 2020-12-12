package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ReqNotification {
    private UUID id;
    private String message;
    private boolean seen;
    private UUID userId;
}
