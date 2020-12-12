package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class ReqProduct {
    private UUID id;
    private String name;
    private String descriptionUz;
    private String descriptionRu;
    private String descriptionEn;
    private Integer categoryId;
//    private String provider;
    private Integer productTypeId;
    private List<ReqProductPrice> reqProductPrices;
    private List<UUID> photosId;
//    private Timestamp expirationDate;
//    private Timestamp productionDate;
    private String madeIn;

}
