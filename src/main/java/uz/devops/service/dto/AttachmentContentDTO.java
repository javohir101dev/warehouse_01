package uz.devops.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.devops.domain.AttachmentContent} entity.
 */
@Schema(description = "Attachment(file) content")
public class AttachmentContentDTO implements Serializable {

    private Long id;

    /**
     * Max size is 10 MB for file content
     */

    @Schema(description = "Max size is 10 MB for file content", required = true)
    @Lob
    private byte[] bytes;

    private String bytesContentType;
    private AttachmentDTO attachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getBytesContentType() {
        return bytesContentType;
    }

    public void setBytesContentType(String bytesContentType) {
        this.bytesContentType = bytesContentType;
    }

    public AttachmentDTO getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentDTO attachment) {
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentContentDTO)) {
            return false;
        }

        AttachmentContentDTO attachmentContentDTO = (AttachmentContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attachmentContentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentContentDTO{" +
            "id=" + getId() +
            ", bytes='" + getBytes() + "'" +
            ", attachment=" + getAttachment() +
            "}";
    }
}
