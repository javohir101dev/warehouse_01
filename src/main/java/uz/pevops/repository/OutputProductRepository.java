package uz.pevops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.pevops.domain.OutputProduct;

/**
 * Spring Data JPA repository for the OutputProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutputProductRepository extends JpaRepository<OutputProduct, Long>, JpaSpecificationExecutor<OutputProduct> {}
