package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Input;
import uz.devops.domain.Users;
import uz.devops.domain.Warehouse;
import uz.devops.service.dto.InputDTO;
import uz.devops.service.dto.UsersDTO;
import uz.devops.service.dto.WarehouseDTO;

/**
 * Mapper for the entity {@link Input} and its DTO {@link InputDTO}.
 */
@Mapper(componentModel = "spring")
public interface InputMapper extends EntityMapper<InputDTO, Input> {
    @Mapping(target = "warehouse", source = "warehouse", qualifiedByName = "warehouseId")
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "usersId")
    InputDTO toDto(Input s);

    @Named("warehouseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WarehouseDTO toDtoWarehouseId(Warehouse warehouse);

    @Named("usersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsersDTO toDtoUsersId(Users users);
}
