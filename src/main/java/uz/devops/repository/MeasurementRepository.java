package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.Measurement;

/**
 * Spring Data JPA repository for the Measurement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long>, JpaSpecificationExecutor<Measurement> {}
