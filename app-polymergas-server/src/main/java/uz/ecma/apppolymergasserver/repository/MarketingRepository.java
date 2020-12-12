package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.ecma.apppolymergasserver.entity.Marketing;

import java.util.UUID;

public interface MarketingRepository extends JpaRepository<Marketing, UUID> {





}
