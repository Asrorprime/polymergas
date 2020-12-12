package uz.ecma.apppolymergasserver.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.ecma.apppolymergasserver.entity.Role;

@Projection(name = "customRole", types = {Role.class})
public interface CustomRole {
    Integer getId();

    String getName();

}
