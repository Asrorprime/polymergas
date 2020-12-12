package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import uz.ecma.apppolymergasserver.entity.SelectedProduct;

import java.util.List;
import java.util.UUID;

public interface SelectedProductRepository extends JpaRepository<SelectedProduct, UUID> {
    public List<SelectedProduct> findAllByChatId(Long chatId);

    @Modifying
    @Transactional
    void deleteAllByChatId(Long chatId);


    @Modifying
    @Transactional
    void deleteAllByChatIdAndProductId(Long chatId, UUID productId);

}
