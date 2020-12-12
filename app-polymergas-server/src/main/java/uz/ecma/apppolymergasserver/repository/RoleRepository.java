package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.ecma.apppolymergasserver.entity.Role;
import uz.ecma.apppolymergasserver.entity.enums.RoleName;
import uz.ecma.apppolymergasserver.projection.CustomRole;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "role",collectionResourceRel = "list", excerptProjection = CustomRole.class)
public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findAllByName(RoleName name);

    Optional<Role> findByName(RoleName name);

    List<Role> findAllByNameIn(List<RoleName> names);


}
