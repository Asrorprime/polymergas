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
public class ProductPrice extends AbsEntity {

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "productPrices",cascade = CascadeType.REMOVE)
    private List<Product> products;
    private double price;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Quantity quantity;
    private boolean haveProduct;

}
