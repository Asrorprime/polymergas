package uz.ecma.apppolymergasserver.payload;

import lombok.Data;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.List;
import java.util.UUID;

@Data
public class ReqCart {
    private UUID userOrderId;
    private List<ReqSelectedProduct> reqSelectedProducts;
    private UUID userId;
    private Step step;
    private UUID currentUserId;
}
