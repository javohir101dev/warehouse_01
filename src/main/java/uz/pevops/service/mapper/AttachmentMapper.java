package uz.pevops.service.mapper;

import org.mapstruct.*;
import uz.pevops.domain.Attachment;
import uz.pevops.service.dto.AttachmentDTO;

/**
 * Mapper for the entity {@link Attachment} and its DTO {@link AttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {}
