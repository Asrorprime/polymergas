package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqSaleHistory {
    private UUID id;

    private double ball;

    private UUID saleUserId;

    private UUID saleId;

    public ReqSaleHistory(double ball, UUID saleUserId, UUID saleId) {
        this.ball = ball;
        this.saleUserId = saleUserId;
        this.saleId = saleId;
    }


}
