package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ecma.apppolymergasserver.entity.Sale;
import uz.ecma.apppolymergasserver.entity.SaleHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaleHistoryRepository extends JpaRepository<SaleHistory, UUID> {
    Page<SaleHistory> findAllBySaleUser_FullNameContainsIgnoreCaseOrSaleUser_PhoneNumberOrSale_TitleUzContainsIgnoreCase(String saleUser_fullName, String saleUser_phoneNumber, String sale_titleUz, Pageable pageable);


    List<SaleHistory> findAllBySaleUser_IdOrderByCreatedAtDesc(UUID saleUser_id);

    @Query(value = "select sum(ball),cast(sale_user_id as varchar) from sale_history where sale_id=:saleId group by sale_user_id order by sum(ball) desc", nativeQuery = true)
    Page<Object[]> findAllByaaa(UUID saleId, Pageable pageable);

}
