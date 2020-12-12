package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqMarketing {
    private UUID id;
    private String titleUz;
    private String titleRu;
    private String titleEn;
    private String textUz;
    private String textRu;
    private String textEn;
    private UUID photoId;
    private String time;
    private String photoUrl;

}
