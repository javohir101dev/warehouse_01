package uz.pevops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.pevops.service.dto.InputProductDTO;

/**
 * Service Interface for managing {@link uz.pevops.domain.InputProduct}.
 */
public interface InputProductService {
    /**
     * Save a inputProduct.
     *
     * @param inputProductDTO the entity to save.
     * @return the persisted entity.
     */
    InputProductDTO save(InputProductDTO inputProductDTO);

    /**
     * Updates a inputProduct.
     *
     * @param inputProductDTO the entity to update.
     * @return the persisted entity.
     */
    InputProductDTO update(InputProductDTO inputProductDTO);

    /**
     * Partially updates a inputProduct.
     *
     * @param inputProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InputProductDTO> partialUpdate(InputProductDTO inputProductDTO);

    /**
     * Get all the inputProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InputProductDTO> findAll(Pageable pageable);

    /**
     * Get the "id" inputProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InputProductDTO> findOne(Long id);

    /**
     * Delete the "id" inputProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
