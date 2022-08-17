package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class InputProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InputProduct.class);
        InputProduct inputProduct1 = new InputProduct();
        inputProduct1.setId(1L);
        InputProduct inputProduct2 = new InputProduct();
        inputProduct2.setId(inputProduct1.getId());
        assertThat(inputProduct1).isEqualTo(inputProduct2);
        inputProduct2.setId(2L);
        assertThat(inputProduct1).isNotEqualTo(inputProduct2);
        inputProduct1.setId(null);
        assertThat(inputProduct1).isNotEqualTo(inputProduct2);
    }
}
