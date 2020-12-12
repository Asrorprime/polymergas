package uz.ecma.apppolymergasserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedProduct extends AbsEntity {

    private Long chatId;

    private UUID productId;

    private UUID productPriceId;

    private Double count;

    private Double calculatedPrice;

    @ManyToOne
    private UserOrder userOrder;

}
