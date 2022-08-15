package uz.pevops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputProductMapperTest {

    private InputProductMapper inputProductMapper;

    @BeforeEach
    public void setUp() {
        inputProductMapper = new InputProductMapperImpl();
    }
}
