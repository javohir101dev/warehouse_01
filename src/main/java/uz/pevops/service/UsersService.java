package uz.pevops.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.pevops.service.dto.UsersDTO;

/**
 * Service Interface for managing {@link uz.pevops.domain.Users}.
 */
public interface UsersService {
    /**
     * Save a users.
     *
     * @param usersDTO the entity to save.
     * @return the persisted entity.
     */
    UsersDTO save(UsersDTO usersDTO);

    /**
     * Updates a users.
     *
     * @param usersDTO the entity to update.
     * @return the persisted entity.
     */
    UsersDTO update(UsersDTO usersDTO);

    /**
     * Partially updates a users.
     *
     * @param usersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UsersDTO> partialUpdate(UsersDTO usersDTO);

    /**
     * Get all the users.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UsersDTO> findAll(Pageable pageable);

    /**
     * Get all the users with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UsersDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" users.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UsersDTO> findOne(Long id);

    /**
     * Delete the "id" users.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
