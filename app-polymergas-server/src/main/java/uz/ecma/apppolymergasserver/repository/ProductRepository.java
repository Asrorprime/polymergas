package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ecma.apppolymergasserver.entity.Product;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCase(String name, String descriptionUz, String descriptionRu, String descriptionEn, String madeIn, Pageable pageable);

    Page<Product> findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCaseOrderByCreatedAtDesc(String name, String descriptionUz, String descriptionRu, String descriptionEn, String madeIn, Pageable pageable);

    Page<Product> findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCaseOrderByCreatedAtAsc(String name, String descriptionUz, String descriptionRu, String descriptionEn, String madeIn, Pageable pageable);

    Page<Product> findAllByNameContainsIgnoreCaseOrDescriptionUzContainsIgnoreCaseOrDescriptionRuContainsIgnoreCaseOrDescriptionEnContainsIgnoreCaseOrMadeInContainsIgnoreCaseOrderByName(String name, String descriptionUz, String descriptionRu, String descriptionEn, String madeIn, Pageable pageable);

    Page<Product> findAllByNameContainsIgnoreCaseAndProductPrices_HaveProductIsTrueOrderByCreatedAtDesc(String name, Pageable pageable);


    List<Product> findAllByProductPrices_HaveProductIsTrueOrderByCreatedAtDesc();

    Page<Product> findAllByNameContainsIgnoreCaseAndProductPrices_HaveProductIsFalseOrderByCreatedAtDesc(String name, Pageable pageable);

    @Query(value = "select distinct * from product right join  product_product_prices ppp on product.id = ppp.products_id right join product_price pp on ppp.product_prices_id = pp.id where pp.have_product=true", nativeQuery = true)
    Page<Product> findAllByProductPrices_HaveProduct(String searchName, boolean productPrices_haveProduct, Pageable pageable);


    @Query(value = "select  * from product p  left join (select product_id,count(*) as count_products from selected_product group by  product_id order by  count_products desc) as sumOrder on sumOrder.product_id=p.id where  count_products IS NOT NULL order by count_products desc", nativeQuery = true)
    Page<Product> findAllPopularProducts(Pageable pageable);

    Integer countAllByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);

    @Query(value = "select cast(product_id as varchar)  ,count(product_id),product.description_uz,product.description_ru,product.description_en,product.name from selected_product  inner join product on product.id=product_id where  user_order_id in (select  id from user_order where id in (WITH summary AS ( SELECT p.id, p.user_order_id, p.current_step,ROW_NUMBER() OVER(PARTITION BY p.user_order_id ORDER BY p.created_at DESC) AS rk  FROM user_order_history p)  SELECT s.user_order_id  FROM summary s  WHERE s.rk = 1  and s.current_step='CLOSED' ) )group by  product.id,selected_product.product_id limit 10", nativeQuery = true)
    List<Object[]> top10Product();

    List<Product> findAllByCategoryId(Integer categoryId);

    Optional<Product> findByName(String productName);

//    Optional<Product> findByNameAndProductPrices_HaveProductIsTrue(String productName);

}
