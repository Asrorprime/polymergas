package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ReqProductPrice {
    private UUID id;
    private List<UUID> productIds;
    private double price;
    private ReqQuantity reqQuantity;
    private boolean haveProduct;
}
