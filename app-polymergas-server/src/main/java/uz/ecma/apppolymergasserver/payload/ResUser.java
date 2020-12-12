package uz.ecma.apppolymergasserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUser {
    private UUID id;
    private String phoneNumber;
    private String additionalPhone;
    private String fullName;
    private boolean enabled;
    private ResUploadFile resUploadFile;
    private List<Integer> rolesId;
}
