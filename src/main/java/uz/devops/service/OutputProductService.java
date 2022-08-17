package uz.devops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.devops.service.dto.OutputProductDTO;

/**
 * Service Interface for managing {@link uz.devops.domain.OutputProduct}.
 */
public interface OutputProductService {
    /**
     * Save a outputProduct.
     *
     * @param outputProductDTO the entity to save.
     * @return the persisted entity.
     */
    OutputProductDTO save(OutputProductDTO outputProductDTO);

    /**
     * Updates a outputProduct.
     *
     * @param outputProductDTO the entity to update.
     * @return the persisted entity.
     */
    OutputProductDTO update(OutputProductDTO outputProductDTO);

    /**
     * Partially updates a outputProduct.
     *
     * @param outputProductDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OutputProductDTO> partialUpdate(OutputProductDTO outputProductDTO);

    /**
     * Get all the outputProducts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OutputProductDTO> findAll(Pageable pageable);

    /**
     * Get the "id" outputProduct.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OutputProductDTO> findOne(Long id);

    /**
     * Delete the "id" outputProduct.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
