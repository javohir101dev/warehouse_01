package uz.pevops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.pevops.domain.Output;

/**
 * Spring Data JPA repository for the Output entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutputRepository extends JpaRepository<Output, Long>, JpaSpecificationExecutor<Output> {}
