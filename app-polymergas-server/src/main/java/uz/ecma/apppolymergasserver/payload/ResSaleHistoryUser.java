package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResSaleHistoryUser {

    private UUID saleId;

    private double userBall;

    private ResUser resUser;

}
