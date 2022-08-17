package uz.devops.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeasurementMapperTest {

    private MeasurementMapper measurementMapper;

    @BeforeEach
    public void setUp() {
        measurementMapper = new MeasurementMapperImpl();
    }
}
