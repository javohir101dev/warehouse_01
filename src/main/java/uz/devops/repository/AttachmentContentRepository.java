package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.AttachmentContent;

/**
 * Spring Data JPA repository for the AttachmentContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Long>, JpaSpecificationExecutor<AttachmentContent> {}
