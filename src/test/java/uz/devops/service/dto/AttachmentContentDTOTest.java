package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class AttachmentContentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentContentDTO.class);
        AttachmentContentDTO attachmentContentDTO1 = new AttachmentContentDTO();
        attachmentContentDTO1.setId(1L);
        AttachmentContentDTO attachmentContentDTO2 = new AttachmentContentDTO();
        assertThat(attachmentContentDTO1).isNotEqualTo(attachmentContentDTO2);
        attachmentContentDTO2.setId(attachmentContentDTO1.getId());
        assertThat(attachmentContentDTO1).isEqualTo(attachmentContentDTO2);
        attachmentContentDTO2.setId(2L);
        assertThat(attachmentContentDTO1).isNotEqualTo(attachmentContentDTO2);
        attachmentContentDTO1.setId(null);
        assertThat(attachmentContentDTO1).isNotEqualTo(attachmentContentDTO2);
    }
}
