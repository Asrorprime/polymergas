package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResSelectedProduct {

    private UUID selectedProductId;

    private ResProduct product;

    private ResProductPrice resProductPrice;

    private UUID productPriceId;

    private Double count;

    private Double calculatedPrice;

    private UUID userOrderId;

}
