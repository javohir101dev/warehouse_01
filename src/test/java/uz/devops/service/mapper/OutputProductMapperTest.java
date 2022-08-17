package uz.devops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutputProductMapperTest {

    private OutputProductMapper outputProductMapper;

    @BeforeEach
    public void setUp() {
        outputProductMapper = new OutputProductMapperImpl();
    }
}
