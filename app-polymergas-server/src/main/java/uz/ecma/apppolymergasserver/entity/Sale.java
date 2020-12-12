package uz.ecma.apppolymergasserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sale extends AbsEntity {

    private String titleUz;

    private String titleRu;

    private String titleEn;

    private Timestamp startDate;

    private Timestamp endDate;

    private String descriptionUz;

    private String descriptionRu;

    private String descriptionEn;

    private boolean active;

    private double ball;

    @OneToMany(fetch = FetchType.LAZY)
    private List<SaleHistory> saleHistories;

}
