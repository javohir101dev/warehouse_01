package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import uz.devops.domain.enumeration.Status;

/**
 * A DTO for the {@link uz.devops.domain.Warehouse} entity.
 */
@Schema(description = "Warehouse(Storehouse)")
public class WarehouseDTO implements Serializable {

    private Long id;

    /**
     * Name of the warehouse
     */
    @NotNull
    @Schema(description = "Name of the warehouse", required = true)
    private String name;

    @NotNull
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarehouseDTO)) {
            return false;
        }

        WarehouseDTO warehouseDTO = (WarehouseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, warehouseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarehouseDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
