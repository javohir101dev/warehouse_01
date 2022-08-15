package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Currency;
import uz.pevops.service.dto.CurrencyDTO;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {}
