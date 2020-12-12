package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.ecma.apppolymergasserver.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Notification findByUserIdAndId(UUID userId, UUID id);

    Page<Notification> findAllByUserIdAndSeenFalseOrderByCreatedAt(UUID userId, Pageable pageable);

    List<Notification> findAllByUserIdAndSeenFalseOrderByCreatedAt(UUID userId);

    Page<Notification> findAllByUserIdOrderBySeenAsc(UUID userId, Pageable pageable);
}
