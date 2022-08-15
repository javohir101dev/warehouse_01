package uz.pevops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.pevops.service.dto.MeasurementDTO;

/**
 * Service Interface for managing {@link uz.pevops.domain.Measurement}.
 */
public interface MeasurementService {
    /**
     * Save a measurement.
     *
     * @param measurementDTO the entity to save.
     * @return the persisted entity.
     */
    MeasurementDTO save(MeasurementDTO measurementDTO);

    /**
     * Updates a measurement.
     *
     * @param measurementDTO the entity to update.
     * @return the persisted entity.
     */
    MeasurementDTO update(MeasurementDTO measurementDTO);

    /**
     * Partially updates a measurement.
     *
     * @param measurementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MeasurementDTO> partialUpdate(MeasurementDTO measurementDTO);

    /**
     * Get all the measurements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MeasurementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" measurement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MeasurementDTO> findOne(Long id);

    /**
     * Delete the "id" measurement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
