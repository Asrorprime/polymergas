package uz.ecma.apppolymergasserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.enums.Step;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrderHistory extends AbsEntity {

    @ManyToOne
    private UserOrder userOrder;

    @Enumerated(value = EnumType.STRING)
    private Step currentStep;

    private String message;

    private UUID changedAdminId;

}
