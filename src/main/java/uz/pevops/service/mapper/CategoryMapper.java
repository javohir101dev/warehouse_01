package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Category;
import uz.pevops.service.dto.CategoryDTO;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "categoryParent", source = "categoryParent", qualifiedByName = "categoryId")
    CategoryDTO toDto(Category s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
