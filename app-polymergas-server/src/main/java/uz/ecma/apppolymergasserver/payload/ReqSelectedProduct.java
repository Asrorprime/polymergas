package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class ReqSelectedProduct {
    private UUID selectedProductId;

    private UUID productId;

    private UUID productPriceId;

    private Double count;

    private Double calculatedPrice;

    private UUID userOrderId;
}
