package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResCategory {
    private Integer categoryId;
    private String nameUz;
    private String nameRu;
    private String nameEn;
}
