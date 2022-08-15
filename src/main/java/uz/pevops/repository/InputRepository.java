package uz.pevops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.pevops.domain.Input;

/**
 * Spring Data JPA repository for the Input entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InputRepository extends JpaRepository<Input, Long>, JpaSpecificationExecutor<Input> {}
