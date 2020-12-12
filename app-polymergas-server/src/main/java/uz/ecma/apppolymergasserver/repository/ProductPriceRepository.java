package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.apppolymergasserver.entity.Product;
import uz.ecma.apppolymergasserver.entity.ProductPrice;
import uz.ecma.apppolymergasserver.entity.Quantity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
    Optional<ProductPrice> findByQuantity(Quantity quantity);

    List<ProductPrice> findAllByHaveProductIsTrueOrderByCreatedAt();

//    List<ProductPrice> findAllByProducts_IdAndHaveProductIsTrueOrderByCreatedAtDesc(UUID products_id);
//
//    List<ProductPrice> findAllByIdAndHaveProductIsTrue(List<UUID> ids);

    Optional<ProductPrice> findByIdAndHaveProductIsTrue(UUID id);

    Optional<ProductPrice> findByPriceAndProducts_Id(double price, UUID productId);
}
