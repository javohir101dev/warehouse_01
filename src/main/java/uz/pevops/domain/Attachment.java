package uz.pevops.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * AttachmentInfo(file)
 */
@Entity
@Table(name = "attachment")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * File name
     */
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    /**
     * File size
     */
    @NotNull
    @Column(name = "size_attachment", nullable = false)
    private Long sizeAttachment;

    /**
     * File format, content type
     */
    @NotNull
    @Column(name = "content_type", nullable = false)
    private String contentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Attachment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSizeAttachment() {
        return this.sizeAttachment;
    }

    public Attachment sizeAttachment(Long sizeAttachment) {
        this.setSizeAttachment(sizeAttachment);
        return this;
    }

    public void setSizeAttachment(Long sizeAttachment) {
        this.sizeAttachment = sizeAttachment;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Attachment contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return id != null && id.equals(((Attachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sizeAttachment=" + getSizeAttachment() +
            ", contentType='" + getContentType() + "'" +
            "}";
    }
}
