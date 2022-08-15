package uz.pevops.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.pevops.web.rest.TestUtil;

class AttachmentContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentContent.class);
        AttachmentContent attachmentContent1 = new AttachmentContent();
        attachmentContent1.setId(1L);
        AttachmentContent attachmentContent2 = new AttachmentContent();
        attachmentContent2.setId(attachmentContent1.getId());
        assertThat(attachmentContent1).isEqualTo(attachmentContent2);
        attachmentContent2.setId(2L);
        assertThat(attachmentContent1).isNotEqualTo(attachmentContent2);
        attachmentContent1.setId(null);
        assertThat(attachmentContent1).isNotEqualTo(attachmentContent2);
    }
}
