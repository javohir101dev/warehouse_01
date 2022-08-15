package uz.pevops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.pevops.domain.Output} entity.
 */
@Schema(description = "Output(Contains Many OutputProducts)")
public class OutputDTO implements Serializable {

    private Long id;

    /**
     * Name of the output(brief description)
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Schema(description = "Name of the output(brief description)", required = true)
    private String name;

    /**
     * Output date
     */
    @NotNull
    @Schema(description = "Output date", required = true)
    private Instant date;

    /**
     * Unique code of the output
     */
    @NotNull
    @Schema(description = "Unique code of the output", required = true)
    private String code;

    private WarehouseDTO warehouse;

    private UsersDTO client;

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

    public UsersDTO getClient() {
        return client;
    }

    public void setClient(UsersDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutputDTO)) {
            return false;
        }

        OutputDTO outputDTO = (OutputDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, outputDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OutputDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", code='" + getCode() + "'" +
            ", warehouse=" + getWarehouse() +
            ", client=" + getClient() +
            "}";
    }
}
