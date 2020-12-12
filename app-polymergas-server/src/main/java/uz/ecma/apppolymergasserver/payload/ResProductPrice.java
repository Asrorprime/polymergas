package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResProductPrice {
    private UUID productPriceId;
    private double price;
    private ResQuantity resQuantity;
    private boolean haveProduct;

}
