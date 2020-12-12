package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.apppolymergasserver.entity.Quantity;

public interface QuantityRepository extends JpaRepository<Quantity, Integer> {
}
