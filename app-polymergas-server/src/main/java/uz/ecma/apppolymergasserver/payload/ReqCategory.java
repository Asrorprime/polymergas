package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

@Data
public class ReqCategory {
    private Integer categoryId;
    private String nameUz;
    private String nameRu;
    private String nameEn;
}
