package uz.pevops.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Attachment(file) content
 */
@Entity
@Table(name = "attachment_content")
public class AttachmentContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Max size is 10 MB for file content
     */
    @Lob
    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    @NotNull
    @Column(name = "bytes_content_type", nullable = false)
    private String bytesContentType;

    /**
     * Attachment(file) info
     */
    @OneToOne
    @JoinColumn(unique = true)
    private Attachment attachment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AttachmentContent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public AttachmentContent bytes(byte[] bytes) {
        this.setBytes(bytes);
        return this;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getBytesContentType() {
        return this.bytesContentType;
    }

    public AttachmentContent bytesContentType(String bytesContentType) {
        this.bytesContentType = bytesContentType;
        return this;
    }

    public void setBytesContentType(String bytesContentType) {
        this.bytesContentType = bytesContentType;
    }

    public Attachment getAttachment() {
        return this.attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public AttachmentContent attachment(Attachment attachment) {
        this.setAttachment(attachment);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentContent)) {
            return false;
        }
        return id != null && id.equals(((AttachmentContent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentContent{" +
            "id=" + getId() +
            ", bytes='" + getBytes() + "'" +
            ", bytesContentType='" + getBytesContentType() + "'" +
            "}";
    }
}
