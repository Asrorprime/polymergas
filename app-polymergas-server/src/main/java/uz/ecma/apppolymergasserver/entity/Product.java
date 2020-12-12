package uz.ecma.apppolymergasserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbsEntity {

    private String name;

    private String descriptionUz;

    private String descriptionRu;

    private String descriptionEn;

    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    private ProductType productType;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<ProductPrice> productPrices;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Attachment> photos;

//    private String provider;
//
//    private Timestamp expirationDate;
//
//    private Timestamp productionDate;

    private String madeIn;
}
