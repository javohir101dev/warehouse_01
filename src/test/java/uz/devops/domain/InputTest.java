package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class InputTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Input.class);
        Input input1 = new Input();
        input1.setId(1L);
        Input input2 = new Input();
        input2.setId(input1.getId());
        assertThat(input1).isEqualTo(input2);
        input2.setId(2L);
        assertThat(input1).isNotEqualTo(input2);
        input1.setId(null);
        assertThat(input1).isNotEqualTo(input2);
    }
}
