package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Input;
import uz.pevops.domain.Users;
import uz.pevops.domain.Warehouse;
import uz.pevops.service.dto.InputDTO;
import uz.pevops.service.dto.UsersDTO;
import uz.pevops.service.dto.WarehouseDTO;

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
