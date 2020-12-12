package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.apppolymergasserver.entity.Generated;

import java.util.UUID;

public interface GeneratedRepository extends JpaRepository<Generated, UUID> {

}