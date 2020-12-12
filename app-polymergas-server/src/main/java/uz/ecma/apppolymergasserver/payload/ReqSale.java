package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class ReqSale {
    private UUID id;
    private String titleUz;
    private String titleRu;
    private String titleEn;
    private Timestamp startDate;
    private Timestamp endDate;
    private String descriptionUz;
    private String descriptionRu;
    private String descriptionEn;
    private boolean active;
    private String ball;
}
