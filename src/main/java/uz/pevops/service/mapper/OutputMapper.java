package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Output;
import uz.pevops.domain.Users;
import uz.pevops.domain.Warehouse;
import uz.pevops.service.dto.OutputDTO;
import uz.pevops.service.dto.UsersDTO;
import uz.pevops.service.dto.WarehouseDTO;

/**
 * Mapper for the entity {@link Output} and its DTO {@link OutputDTO}.
 */
@Mapper(componentModel = "spring")
public interface OutputMapper extends EntityMapper<OutputDTO, Output> {
    @Mapping(target = "warehouse", source = "warehouse", qualifiedByName = "warehouseId")
    @Mapping(target = "client", source = "client", qualifiedByName = "usersId")
    OutputDTO toDto(Output s);

    @Named("warehouseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WarehouseDTO toDtoWarehouseId(Warehouse warehouse);

    @Named("usersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsersDTO toDtoUsersId(Users users);
}
