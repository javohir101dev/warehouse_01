package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class OutputDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutputDTO.class);
        OutputDTO outputDTO1 = new OutputDTO();
        outputDTO1.setId(1L);
        OutputDTO outputDTO2 = new OutputDTO();
        assertThat(outputDTO1).isNotEqualTo(outputDTO2);
        outputDTO2.setId(outputDTO1.getId());
        assertThat(outputDTO1).isEqualTo(outputDTO2);
        outputDTO2.setId(2L);
        assertThat(outputDTO1).isNotEqualTo(outputDTO2);
        outputDTO1.setId(null);
        assertThat(outputDTO1).isNotEqualTo(outputDTO2);
    }
}
