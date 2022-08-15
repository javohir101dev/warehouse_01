package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Currency;
import uz.pevops.domain.Output;
import uz.pevops.domain.OutputProduct;
import uz.pevops.domain.Product;
import uz.pevops.service.dto.CurrencyDTO;
import uz.pevops.service.dto.OutputDTO;
import uz.pevops.service.dto.OutputProductDTO;
import uz.pevops.service.dto.ProductDTO;

/**
 * Mapper for the entity {@link OutputProduct} and its DTO {@link OutputProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface OutputProductMapper extends EntityMapper<OutputProductDTO, OutputProduct> {
    @Mapping(target = "output", source = "output", qualifiedByName = "outputId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "currency", source = "currency", qualifiedByName = "currencyId")
    OutputProductDTO toDto(OutputProduct s);

    @Named("outputId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OutputDTO toDtoOutputId(Output output);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("currencyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurrencyDTO toDtoCurrencyId(Currency currency);
}
