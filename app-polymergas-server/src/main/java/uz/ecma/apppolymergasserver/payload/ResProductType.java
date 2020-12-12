package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResProductType {

    private Integer productTypeId;
    private String nameUz;
    private String nameRu;
    private String nameEn;

}
