package uz.ecma.apppolymergasserver.payload;

import lombok.Data;

@Data
public class ReqPassword {
    private String phoneNumber;
    private String newPhoneNumber;
    private String oldPassword;
    private String password;
    private String prePassword;
}
