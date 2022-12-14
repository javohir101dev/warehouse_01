package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class OutputTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Output.class);
        Output output1 = new Output();
        output1.setId(1L);
        Output output2 = new Output();
        output2.setId(output1.getId());
        assertThat(output1).isEqualTo(output2);
        output2.setId(2L);
        assertThat(output1).isNotEqualTo(output2);
        output1.setId(null);
        assertThat(output1).isNotEqualTo(output2);
    }
}
