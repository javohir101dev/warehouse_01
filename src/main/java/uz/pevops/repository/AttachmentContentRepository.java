package uz.pevops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.pevops.domain.AttachmentContent;

/**
 * Spring Data JPA repository for the AttachmentContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Long>, JpaSpecificationExecutor<AttachmentContent> {}
