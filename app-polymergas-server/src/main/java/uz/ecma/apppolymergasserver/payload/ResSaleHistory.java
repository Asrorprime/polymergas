package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResSaleHistory {
    private UUID id;

    private double ball;

    private ResUser saleUser;

    private UUID saleId;

    private String createdAt;
}
