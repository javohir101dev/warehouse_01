package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class OutputProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutputProductDTO.class);
        OutputProductDTO outputProductDTO1 = new OutputProductDTO();
        outputProductDTO1.setId(1L);
        OutputProductDTO outputProductDTO2 = new OutputProductDTO();
        assertThat(outputProductDTO1).isNotEqualTo(outputProductDTO2);
        outputProductDTO2.setId(outputProductDTO1.getId());
        assertThat(outputProductDTO1).isEqualTo(outputProductDTO2);
        outputProductDTO2.setId(2L);
        assertThat(outputProductDTO1).isNotEqualTo(outputProductDTO2);
        outputProductDTO1.setId(null);
        assertThat(outputProductDTO1).isNotEqualTo(outputProductDTO2);
    }
}
