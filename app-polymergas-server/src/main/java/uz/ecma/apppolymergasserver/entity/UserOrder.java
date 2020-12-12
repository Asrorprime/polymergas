package uz.ecma.apppolymergasserver.entity;

import com.sun.istack.NotNull;
import lombok.*;

import uz.ecma.apppolymergasserver.entity.enums.Step;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrder extends AbsEntity {

    @NotNull
    private UUID userId;
    @OneToOne
    private Generated uniqueOrder;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userOrder")
    private List<SelectedProduct> selectedProducts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userOrder")
    private List<UserOrderHistory> userOrderHistory;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Step orderStep;
    @NotNull
    boolean payed;
    @NotNull
    boolean changedPrice;

}
