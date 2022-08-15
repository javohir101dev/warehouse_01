package uz.pevops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.pevops.domain.Input} entity.
 */
@Schema(description = "Input(Contains Many InputProducts)")
public class InputDTO implements Serializable {

    private Long id;

    /**
     * Name of the input(brief description)
     */
    @NotNull
    @Size(min = 2, max = 50)
    @Schema(description = "Name of the input(brief description)", required = true)
    private String name;

    /**
     * Input date
     */
    @NotNull
    @Schema(description = "Input date", required = true)
    private Instant date;

    /**
     * Unique code of the input
     */
    @NotNull
    @Schema(description = "Unique code of the input", required = true)
    private String code;

    private WarehouseDTO warehouse;

    private UsersDTO supplier;

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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public WarehouseDTO getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseDTO warehouse) {
        this.warehouse = warehouse;
    }

    public UsersDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(UsersDTO supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InputDTO)) {
            return false;
        }

        InputDTO inputDTO = (InputDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inputDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", code='" + getCode() + "'" +
            ", warehouse=" + getWarehouse() +
            ", supplier=" + getSupplier() +
            "}";
    }
}
