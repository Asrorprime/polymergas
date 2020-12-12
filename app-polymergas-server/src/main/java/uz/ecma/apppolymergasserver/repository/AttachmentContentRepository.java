package uz.ecma.apppolymergasserver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.ecma.apppolymergasserver.entity.Attachment;
import uz.ecma.apppolymergasserver.entity.AttachmentContent;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, UUID> {
    Optional<AttachmentContent> findByAttachment(Attachment attachment);

    @Transactional
    void deleteByAttachmentId(UUID attachment_id);

    @Query(value = "select content from attachment_content where attachment_id=:attId", nativeQuery = true)
    byte[] getByte(@Param("attId") UUID attId);
}
