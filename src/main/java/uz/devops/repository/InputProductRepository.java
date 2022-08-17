package uz.devops.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.devops.domain.InputProduct;

/**
 * Spring Data JPA repository for the InputProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InputProductRepository extends JpaRepository<InputProduct, Long>, JpaSpecificationExecutor<InputProduct> {}
