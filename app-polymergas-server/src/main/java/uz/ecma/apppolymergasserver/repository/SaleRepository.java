package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ecma.apppolymergasserver.entity.Sale;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {


    @Query(value = "select * from sale as s where s.active=:active and (((s.start_date<=:time1 and s.end_date>= :time1) or (s.start_date<=:time2 and s.end_date>=:time2)) or ((:time1<=s.start_date and :time2 >=s.start_date) or (:time1<=s.end_date and :time2 >=s.end_date))) and (lower(s.title_uz) like concat('%',:searchName,'%') or lower(s.title_ru) like concat('%' ,:searchName,'%') or lower(s.description) like concat('%' ,:searchName,'%')) order by s.created_at desc", nativeQuery = true)
    Page<Sale> findAllByActiveAndSearchNameContainsIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByCreatedAtDesc(boolean active, String searchName, Timestamp time1, Timestamp time2, Pageable pageableForNative);

    @Query(value = "select * from sale as s where (((s.start_date<=:time1 and s.end_date>= :time1) or (s.start_date<=:time2 and s.end_date>=:time2)) or ((:time1<=s.start_date and :time2 >=s.start_date) or (:time1<=s.end_date and :time2 >=s.end_date))) and (lower(s.title_uz) like concat('%',:searchName,'%')or lower(s.title_ru) like concat('%' ,:searchName,'%') or lower(s.description) like concat('%' ,:searchName,'%')) order by s.created_at desc ", nativeQuery = true)
    Page<Sale> findAllBySearchNameContainsIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByCreatedAtDesc(String searchName, Timestamp time1, Timestamp time2, Pageable pageableForNative);

    @Query(value = "select * from sale as s where s.active=:active and (lower(s.title_uz) like concat('%',:searchName,'%')or lower(s.title_ru) like concat('%' ,:searchName,'%') or lower(s.description) like concat('%' ,:searchName,'%')) order by s.created_at desc ", nativeQuery = true)
    Page<Sale> findAllByActiveAndSearchNameContainsIgnoreCaseOrderByCreatedAtDesc(boolean active, String searchName, Pageable pageableForNative);

    @Query(value = "select * from sale as s where (lower(s.title_uz) like concat('%',:searchName,'%')or lower(s.title_ru) like concat('%' ,:searchName,'%') or lower(s.description) like concat('%' ,:searchName,'%')) order by s.created_at desc ", nativeQuery = true)
    Page<Sale> findAllBySearchNameContainsIgnoreCaseOrderByCreatedAtDesc(String searchName, Pageable pageableForNative);

    @Query(value = "SELECT * FROM sale s WHERE :time BETWEEN s.start_date AND s.end_date and s.active=true", nativeQuery = true)
    Optional<Sale> findSaleByPayedDateBetweenAndEndAndActiveTrue(Timestamp time);


}
