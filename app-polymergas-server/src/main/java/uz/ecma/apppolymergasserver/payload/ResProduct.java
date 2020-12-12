package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResProduct {
    private UUID id;
    private String name;
    private String descriptionUz;
    private String descriptionRu;
    private String descriptionEn;
    private ResCategory resCategory;
    private ResProductType resProductType;
//    private String provider;
    private List<ResProductPrice> resProductPrices;
    private List<ResUploadFile> photos;
//    private String expirationDate;
//    private String productionDate;
    private String madeIn;
    private String createdAt;
    private String createdBy;

}
