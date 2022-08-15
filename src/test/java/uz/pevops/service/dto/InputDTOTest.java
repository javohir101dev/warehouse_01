package uz.pevops.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.pevops.web.rest.TestUtil;

class InputDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputDTO.class);
        InputDTO inputDTO1 = new InputDTO();
        inputDTO1.setId(1L);
        InputDTO inputDTO2 = new InputDTO();
        assertThat(inputDTO1).isNotEqualTo(inputDTO2);
        inputDTO2.setId(inputDTO1.getId());
        assertThat(inputDTO1).isEqualTo(inputDTO2);
        inputDTO2.setId(2L);
        assertThat(inputDTO1).isNotEqualTo(inputDTO2);
        inputDTO1.setId(null);
        assertThat(inputDTO1).isNotEqualTo(inputDTO2);
    }
}
