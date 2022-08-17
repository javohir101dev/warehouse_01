package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Currency;
import uz.devops.service.dto.CurrencyDTO;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {}
