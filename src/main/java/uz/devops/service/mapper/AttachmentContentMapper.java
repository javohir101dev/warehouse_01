package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Attachment;
import uz.devops.domain.AttachmentContent;
import uz.devops.service.dto.AttachmentContentDTO;
import uz.devops.service.dto.AttachmentDTO;

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
