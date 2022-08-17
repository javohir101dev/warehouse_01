package uz.devops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.devops.domain.Currency;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Spring Data JPA repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepositoryCustom extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {
    boolean existsByName(@NotNull @Size(min = 2, max = 5) String name);

}
