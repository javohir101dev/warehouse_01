package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Currency;
import uz.devops.domain.Input;
import uz.devops.domain.InputProduct;
import uz.devops.domain.Product;
import uz.devops.service.dto.CurrencyDTO;
import uz.devops.service.dto.InputDTO;
import uz.devops.service.dto.InputProductDTO;
import uz.devops.service.dto.ProductDTO;

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
