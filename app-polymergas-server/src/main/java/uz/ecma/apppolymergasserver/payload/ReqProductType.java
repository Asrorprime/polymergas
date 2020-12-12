package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqProductType {
    private Integer productTypeId;
    private String nameUz;
    private String nameRu;
    private String nameEn;

}
