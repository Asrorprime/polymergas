package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ReqSignUp {
    private UUID id;

    private String phoneNumber;

    private String password;

    private String fullName;

    private String additionalPhone;

    private UUID photoId;

    private List<Integer> rolesId;
}
