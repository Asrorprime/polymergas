package uz.ecma.apppolymergasserver.payload;

import lombok.Data;
import uz.ecma.apppolymergasserver.entity.enums.QuantityType;

@Data
public class ReqQuantity {
    private Integer id;
    private String value;
    private QuantityType quantityType;
}
