package uz.ecma.apppolymergasserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.enums.Step;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification extends AbsEntity {
    @Column(columnDefinition = "TEXT")
    private String message;

    private UUID userOrderId;

    @Enumerated(EnumType.STRING)
    private Step messageType;

    private boolean seen;

    private UUID userId;

}
