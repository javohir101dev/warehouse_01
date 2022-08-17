package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Output;
import uz.devops.domain.Users;
import uz.devops.domain.Warehouse;
import uz.devops.service.dto.OutputDTO;
import uz.devops.service.dto.UsersDTO;
import uz.devops.service.dto.WarehouseDTO;

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
