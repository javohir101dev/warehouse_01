package uz.pevops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.pevops.service.dto.AttachmentContentDTO;

/**
 * Service Interface for managing {@link uz.pevops.domain.AttachmentContent}.
 */
public interface AttachmentContentService {
    /**
     * Save a attachmentContent.
     *
     * @param attachmentContentDTO the entity to save.
     * @return the persisted entity.
     */
    AttachmentContentDTO save(AttachmentContentDTO attachmentContentDTO);

    /**
     * Updates a attachmentContent.
     *
     * @param attachmentContentDTO the entity to update.
     * @return the persisted entity.
     */
    AttachmentContentDTO update(AttachmentContentDTO attachmentContentDTO);

    /**
     * Partially updates a attachmentContent.
     *
     * @param attachmentContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttachmentContentDTO> partialUpdate(AttachmentContentDTO attachmentContentDTO);

    /**
     * Get all the attachmentContents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttachmentContentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" attachmentContent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttachmentContentDTO> findOne(Long id);

    /**
     * Delete the "id" attachmentContent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
