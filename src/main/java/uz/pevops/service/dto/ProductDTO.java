package uz.pevops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import uz.pevops.domain.enumeration.Status;

/**
 * A DTO for the {@link uz.pevops.domain.Product} entity.
 */
@Schema(description = "Product")
public class ProductDTO implements Serializable {

    private Long id;

    /**
     * Name of product
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Schema(description = "Name of product", required = true)
    private String name;

    @NotNull
    private Status status;

    /**
     * Unoque code of product
     */
    @NotNull
    @Schema(description = "Unoque code of product", required = true)
    private String code;

    private AttachmentDTO photo;

    private MeasurementDTO measurement;

    private CategoryDTO category;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AttachmentDTO getPhoto() {
        return photo;
    }

    public void setPhoto(AttachmentDTO photo) {
        this.photo = photo;
    }

    public MeasurementDTO getMeasurement() {
        return measurement;
    }

    public void setMeasurement(MeasurementDTO measurement) {
        this.measurement = measurement;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", code='" + getCode() + "'" +
            ", photo=" + getPhoto() +
            ", measurement=" + getMeasurement() +
            ", category=" + getCategory() +
            "}";
    }
}
