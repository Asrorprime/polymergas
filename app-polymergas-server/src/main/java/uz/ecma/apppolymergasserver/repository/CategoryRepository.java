package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.apppolymergasserver.entity.Category;
import uz.ecma.apppolymergasserver.entity.Product;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAllByNameUzContainsIgnoreCaseOrNameRuContainsIgnoreCaseOrNameEnContainsIgnoreCase(String nameUz, String nameRu, String nameEn, Pageable pageable);

    Optional<Category> findByNameUzOrNameRuOrNameEn(String nameUz, String nameRu, String nameEn);
}
