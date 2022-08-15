package uz.pevops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttachmentContentMapperTest {

    private AttachmentContentMapper attachmentContentMapper;

    @BeforeEach
    public void setUp() {
        attachmentContentMapper = new AttachmentContentMapperImpl();
    }
}
