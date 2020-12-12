package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.apppolymergasserver.entity.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
    Page<ProductType> findAllByNameUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameEnContainsIgnoreCase(String nameUz, String nameRu, String nameEn, Pageable pageable);
}
