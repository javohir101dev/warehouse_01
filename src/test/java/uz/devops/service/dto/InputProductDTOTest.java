package uz.devops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class InputProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputProductDTO.class);
        InputProductDTO inputProductDTO1 = new InputProductDTO();
        inputProductDTO1.setId(1L);
        InputProductDTO inputProductDTO2 = new InputProductDTO();
        assertThat(inputProductDTO1).isNotEqualTo(inputProductDTO2);
        inputProductDTO2.setId(inputProductDTO1.getId());
        assertThat(inputProductDTO1).isEqualTo(inputProductDTO2);
        inputProductDTO2.setId(2L);
        assertThat(inputProductDTO1).isNotEqualTo(inputProductDTO2);
        inputProductDTO1.setId(null);
        assertThat(inputProductDTO1).isNotEqualTo(inputProductDTO2);
    }
}
