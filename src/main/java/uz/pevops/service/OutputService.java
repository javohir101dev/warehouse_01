package uz.pevops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.pevops.service.dto.OutputDTO;

/**
 * Service Interface for managing {@link uz.pevops.domain.Output}.
 */
public interface OutputService {
    /**
     * Save a output.
     *
     * @param outputDTO the entity to save.
     * @return the persisted entity.
     */
    OutputDTO save(OutputDTO outputDTO);

    /**
     * Updates a output.
     *
     * @param outputDTO the entity to update.
     * @return the persisted entity.
     */
    OutputDTO update(OutputDTO outputDTO);

    /**
     * Partially updates a output.
     *
     * @param outputDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OutputDTO> partialUpdate(OutputDTO outputDTO);

    /**
     * Get all the outputs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OutputDTO> findAll(Pageable pageable);

    /**
     * Get the "id" output.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OutputDTO> findOne(Long id);

    /**
     * Delete the "id" output.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
