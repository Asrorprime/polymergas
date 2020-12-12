package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.enums.Step;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUserOrder {

    private UUID userOrderId;

    private Integer uniqueId;

    private ResUser ResUser;

    private List<ResSelectedProduct> resSelectedProducts;

    private List<ResUserOrderHistory> resUserOrderHistories;

    private Step orderStep;

    private boolean payed;

    private boolean changedPrice;

    private Double totalPrice;
}
