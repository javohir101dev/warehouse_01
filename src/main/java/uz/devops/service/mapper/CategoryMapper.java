package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Category;
import uz.devops.service.dto.CategoryDTO;

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
