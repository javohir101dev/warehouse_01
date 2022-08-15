package uz.pevops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OutputMapperTest {

    private OutputMapper outputMapper;

    @BeforeEach
    public void setUp() {
        outputMapper = new OutputMapperImpl();
    }
}
