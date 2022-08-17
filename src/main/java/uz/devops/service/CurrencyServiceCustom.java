package uz.devops.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.devops.service.dto.CurrencyDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link uz.devops.domain.Currency}.
 */
public interface CurrencyServiceCustom {
    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save.
     * @return the persisted entity.
     */
    CurrencyDTO save(CurrencyDTO currencyDTO);

}
