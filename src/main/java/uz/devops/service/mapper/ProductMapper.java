package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Attachment;
import uz.devops.domain.Category;
import uz.devops.domain.Measurement;
import uz.devops.domain.Product;
import uz.devops.service.dto.AttachmentDTO;
import uz.devops.service.dto.CategoryDTO;
import uz.devops.service.dto.MeasurementDTO;
import uz.devops.service.dto.ProductDTO;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "photo", source = "photo", qualifiedByName = "attachmentId")
    @Mapping(target = "measurement", source = "measurement", qualifiedByName = "measurementId")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    ProductDTO toDto(Product s);

    @Named("attachmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AttachmentDTO toDtoAttachmentId(Attachment attachment);

    @Named("measurementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MeasurementDTO toDtoMeasurementId(Measurement measurement);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
