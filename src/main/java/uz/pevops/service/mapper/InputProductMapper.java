package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Currency;
import uz.pevops.domain.Input;
import uz.pevops.domain.InputProduct;
import uz.pevops.domain.Product;
import uz.pevops.service.dto.CurrencyDTO;
import uz.pevops.service.dto.InputDTO;
import uz.pevops.service.dto.InputProductDTO;
import uz.pevops.service.dto.ProductDTO;

/**
 * Mapper for the entity {@link InputProduct} and its DTO {@link InputProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface InputProductMapper extends EntityMapper<InputProductDTO, InputProduct> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "input", source = "input", qualifiedByName = "inputId")
    @Mapping(target = "currency", source = "currency", qualifiedByName = "currencyId")
    InputProductDTO toDto(InputProduct s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("inputId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InputDTO toDtoInputId(Input input);

    @Named("currencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurrencyDTO toDtoCurrencyId(Currency currency);
}
