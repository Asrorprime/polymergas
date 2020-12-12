package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.ecma.apppolymergasserver.entity.UserOrder;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserOrderRepository extends JpaRepository<UserOrder, UUID> {
    //    @Query(value = "select  * from user_order uo where id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or (select full_name  from users where id=uo.user_id)like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) order by created_at limit :size offset :page*:size", nativeQuery = true)
    @Query(value = "select distinct on (uo.id) * ,sum(sp.count*pp.price),sum(sp.count),us.full_name from user_order uo\n" +
            "inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id order by  uo.id, uo.created_at limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusCreatedAtAsc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select  * ,sum(sp.count*pp.price),sum(sp.count),us.full_name from user_order uo\n" +
            "inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id order by uo.created_at limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusCreatedAtDesc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select  * ,sum(sp.count*pp.price) as prices,sum(sp.count) as counts,us.full_name as user_names from user_order uo\n" +
            "            inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "            where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id,user_names order by user_names limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusUserNameAsc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select  * ,sum(sp.count*pp.price) as prices,sum(sp.count) as counts,us.full_name as user_names from user_order uo\n" +
            "            inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "            where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id,user_names order by user_names desc limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusUserNameDesc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select  * ,sum(sp.count*pp.price) as prices,sum(sp.count) as counts,us.full_name as user_names from user_order uo\n" +
            "            inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "            where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id,user_names order by prices  limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusPriceAsc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select * ,sum(sp.count*pp.price) as prices,sum(sp.count) as counts,us.full_name as user_names from user_order uo\n" +
            "            inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "            where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id,user_names order by prices desc  limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusPriceDesc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select  * ,sum(sp.count*pp.price) as prices,sum(sp.count) as counts,us.full_name as user_names from user_order uo\n" +
            "            inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "            where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id,user_names order by counts limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusCountAsc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select  * ,sum(sp.count*pp.price) as prices,sum(sp.count) as counts,us.full_name as user_names from user_order uo\n" +
            "            inner join selected_product sp on sp.user_order_id=uo.id  inner join  product_price pp on sp.product_price_id = pp.id  inner join users us on uo.user_id = us.id\n" +
            "            where uo.id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step) and ( (select phone_number  from users where id=uo.user_id) like concat('%', :search, '%') or lower((select users.full_name  from users where id=uo.user_id))like concat('%', :search, '%') or (select count(*) from product where id in (select product_id from selected_product sp where sp.user_order_id=uo.id) and name like concat('%', :search, '%')) >0) group by uo.id,uo.created_at,sp.id,pp.id,us.id,user_names order by counts desc  limit :size offset :page*:size", nativeQuery = true)
    List<UserOrder> findAllByStatusCountDesc(String search, String step, @Param(value = "page") int page, @Param(value = "size") int size);

    @Query(value = "select  count(*) from user_order where id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step)", nativeQuery = true)
    Integer findAllByStatusCount(String step);

    @Query(value = "select  count(*) from user_order where id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step ) and created_at<=:time2 and created_at>=:time1 ", nativeQuery = true)
    Integer getUserOrderCount(Timestamp time1, Timestamp time2, String step);

    @Query(value = "select sum(sp.count*pp.price) from selected_product sp inner join product_price pp on sp.product_price_id = pp.id where  user_order_id in (select  id from user_order where id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step=:step ) ) and sp.created_at<=:time2 and sp.created_at>=:time1", nativeQuery = true)
    Double getUserOrderSum(Timestamp time1, Timestamp time2, String step);

    @Query(value = "select us_or.id, us_or.created_at, us_or.created_by_id, us_or.updated_at, us_or.updated_by_id, order_step, us_or.payed, user_id from user_order us_or JOIN users u on us_or.user_id = u.id where us_or.user_id=u.id and lower(phone_number) like concat('%', :searchName, '%') or lower(additional_phone) like concat('%', :searchName, '%')", nativeQuery = true)
    Page<UserOrder> findAllByPhoneNumber(String searchName, Pageable pageable);

    List<UserOrder> findAllByUserId(UUID userId);

    Optional<UserOrder> findByUniqueOrder_Number(Integer id);

}
