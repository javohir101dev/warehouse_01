package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.Output;

/**
 * Spring Data JPA repository for the Output entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutputRepository extends JpaRepository<Output, Long>, JpaSpecificationExecutor<Output> {}
