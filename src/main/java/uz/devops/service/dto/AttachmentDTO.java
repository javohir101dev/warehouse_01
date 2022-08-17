package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.devops.domain.Attachment} entity.
 */
@Schema(description = "AttachmentInfo(file)")
public class AttachmentDTO implements Serializable {

    private Long id;

    /**
     * File name
     */
    @NotNull
    @Size(min = 1, max = 20)
    @Schema(description = "File name", required = true)
    private String name;

    /**
     * File size
     */
    @NotNull
    @Schema(description = "File size", required = true)
    private Long sizeAttachment;

    /**
     * File format, content type
     */
    @NotNull
    @Schema(description = "File format, content type", required = true)
    private String contentType;

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

    public Long getSizeAttachment() {
        return sizeAttachment;
    }

    public void setSizeAttachment(Long sizeAttachment) {
        this.sizeAttachment = sizeAttachment;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentDTO)) {
            return false;
        }

        AttachmentDTO attachmentDTO = (AttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sizeAttachment=" + getSizeAttachment() +
            ", contentType='" + getContentType() + "'" +
            "}";
    }
}
