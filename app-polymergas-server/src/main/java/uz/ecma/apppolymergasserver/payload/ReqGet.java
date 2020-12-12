package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqGet {
    List<Integer> mainColorIds;
    List<UUID> colorId;
    String search="";
    String sort="";
    int page=0;
    int size=10;
}
