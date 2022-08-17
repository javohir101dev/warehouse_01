package uz.devops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.devops.service.dto.InputDTO;

/**
 * Service Interface for managing {@link uz.devops.domain.Input}.
 */
public interface InputService {
    /**
     * Save a input.
     *
     * @param inputDTO the entity to save.
     * @return the persisted entity.
     */
    InputDTO save(InputDTO inputDTO);

    /**
     * Updates a input.
     *
     * @param inputDTO the entity to update.
     * @return the persisted entity.
     */
    InputDTO update(InputDTO inputDTO);

    /**
     * Partially updates a input.
     *
     * @param inputDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InputDTO> partialUpdate(InputDTO inputDTO);

    /**
     * Get all the inputs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InputDTO> findAll(Pageable pageable);

    /**
     * Get the "id" input.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InputDTO> findOne(Long id);

    /**
     * Delete the "id" input.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
