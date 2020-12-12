package uz.ecma.apppolymergasserver.payload;

import lombok.Data;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.List;
import java.util.UUID;

@Data
public class ReqUserOrder {

    private UUID userOrderId;

    private UUID userId;

    private List<ReqSelectedProduct> reqSelectedProducts;

    private List<ReqUserOrderHistory> reqUserOrderHistories;

    private Step orderStep;

}
