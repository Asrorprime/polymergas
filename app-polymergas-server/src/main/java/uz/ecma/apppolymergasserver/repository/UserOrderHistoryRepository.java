package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.apppolymergasserver.entity.UserOrder;
import uz.ecma.apppolymergasserver.entity.UserOrderHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserOrderHistoryRepository extends JpaRepository<UserOrderHistory, UUID> {


    List<UserOrderHistory> findAllByUserOrder_UserId(UUID userId);

    Optional<UserOrderHistory> findByUserOrderId(UUID id);

    Optional<UserOrderHistory> findByUserOrder_UniqueOrder_Number(Integer id);
}
