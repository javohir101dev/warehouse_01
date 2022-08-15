package uz.pevops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import uz.pevops.domain.enumeration.Status;

/**
 * A DTO for the {@link uz.pevops.domain.Category} entity.
 */
@Schema(description = "Category")
public class CategoryDTO implements Serializable {

    private Long id;

    /**
     * Category name
     */
    @NotNull
    @Size(min = 2, max = 20)
    @Schema(description = "Category name", required = true)
    private String name;

    private Status status;

    private CategoryDTO categoryParent;

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

    public CategoryDTO getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(CategoryDTO categoryParent) {
        this.categoryParent = categoryParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", categoryParent=" + getCategoryParent() +
            "}";
    }
}
