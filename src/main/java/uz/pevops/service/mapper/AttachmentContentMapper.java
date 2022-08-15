package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Attachment;
import uz.pevops.domain.AttachmentContent;
import uz.pevops.service.dto.AttachmentContentDTO;
import uz.pevops.service.dto.AttachmentDTO;

/**
 * Mapper for the entity {@link AttachmentContent} and its DTO {@link AttachmentContentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttachmentContentMapper extends EntityMapper<AttachmentContentDTO, AttachmentContent> {
    @Mapping(target = "attachment", source = "attachment", qualifiedByName = "attachmentId")
    AttachmentContentDTO toDto(AttachmentContent s);

    @Named("attachmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AttachmentDTO toDtoAttachmentId(Attachment attachment);
}
