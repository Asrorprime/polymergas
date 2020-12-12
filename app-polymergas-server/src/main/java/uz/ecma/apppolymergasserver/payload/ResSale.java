package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResSale {
    private UUID id;
    private String titleUz;
    private String titleRu;
    private String titleEn;
    private String startDate;
    private String endDate;
    private String descriptionUz;
    private String descriptionRu;
    private String descriptionEn;
    private boolean active;
    private double ball;
    private List<ResSaleHistory> resSaleHistoryList;
}
