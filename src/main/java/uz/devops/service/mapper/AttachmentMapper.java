package uz.devops.service.mapper;

import org.mapstruct.*;
import uz.devops.domain.Attachment;
import uz.devops.service.dto.AttachmentDTO;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {}
