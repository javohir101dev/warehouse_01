package uz.pevops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link uz.pevops.domain.AttachmentContent} entity. This class is used
 * in {@link uz.pevops.web.rest.AttachmentContentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attachment-contents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AttachmentContentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter attachmentId;

    private Boolean distinct;

    public AttachmentContentCriteria() {}

    public AttachmentContentCriteria(AttachmentContentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.attachmentId = other.attachmentId == null ? null : other.attachmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AttachmentContentCriteria copy() {
        return new AttachmentContentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public LongFilter attachmentId() {
        if (attachmentId == null) {
            attachmentId = new LongFilter();
        }
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttachmentContentCriteria that = (AttachmentContentCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(attachmentId, that.attachmentId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attachmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentContentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (attachmentId != null ? "attachmentId=" + attachmentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
