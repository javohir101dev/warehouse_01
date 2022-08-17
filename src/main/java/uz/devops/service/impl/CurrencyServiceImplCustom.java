package uz.devops.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.domain.Currency;
import uz.devops.repository.CurrencyRepositoryCustom;
import uz.devops.service.CurrencyServiceCustom;
import uz.devops.service.dto.CurrencyDTO;
import uz.devops.service.mapper.CurrencyMapper;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Currency}.
 */
@Service
@Transactional
public class CurrencyServiceImplCustom implements CurrencyServiceCustom {

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceImplCustom.class);

    private final CurrencyRepositoryCustom currencyRepository;

    private final CurrencyMapper currencyMapper;

    public CurrencyServiceImplCustom(CurrencyRepositoryCustom currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public CurrencyDTO save(CurrencyDTO currencyDTO) {
        log.debug("Request to save Currency : {}", currencyDTO);
        boolean exists = currencyRepository.existsByName(currencyDTO.getName());
        if (exists){
            log.debug("This Currency: {}  is already exists", currencyDTO);
            return null;
        }
        Currency currency = currencyMapper.toEntity(currencyDTO);
        currency = currencyRepository.save(currency);
        return currencyMapper.toDto(currency);
    }
}
