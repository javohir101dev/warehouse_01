package uz.devops.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.devops.web.rest.TestUtil;

class OutputProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutputProduct.class);
        OutputProduct outputProduct1 = new OutputProduct();
        outputProduct1.setId(1L);
        OutputProduct outputProduct2 = new OutputProduct();
        outputProduct2.setId(outputProduct1.getId());
        assertThat(outputProduct1).isEqualTo(outputProduct2);
        outputProduct2.setId(2L);
        assertThat(outputProduct1).isNotEqualTo(outputProduct2);
        outputProduct1.setId(null);
        assertThat(outputProduct1).isNotEqualTo(outputProduct2);
    }
}
