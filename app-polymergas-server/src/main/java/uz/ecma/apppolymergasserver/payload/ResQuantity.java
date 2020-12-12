package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.enums.QuantityType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResQuantity {
    private Integer id;
    private String value;
    private QuantityType quantityType;
}
