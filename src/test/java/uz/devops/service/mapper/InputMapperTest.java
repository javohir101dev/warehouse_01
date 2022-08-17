package uz.devops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputMapperTest {

    private InputMapper inputMapper;

    @BeforeEach
    public void setUp() {
        inputMapper = new InputMapperImpl();
    }
}
