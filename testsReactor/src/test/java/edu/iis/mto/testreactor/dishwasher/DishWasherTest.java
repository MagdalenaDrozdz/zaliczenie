package edu.iis.mto.testreactor.dishwasher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.engine.EngineException;
import edu.iis.mto.testreactor.dishwasher.pump.PumpException;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DishWasherTest {

    @Mock
    private WaterPump waterPump;

    @Mock
    private Engine engine;

    @Mock
    private DirtFilter dirtFilter;

    @Mock
    private  Door door;

    private DishWasher dishWasher;

    @BeforeEach
    void setUp(){
         dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
    }

    @Test
    void correct_washer_should_return_success(){
        when(door.closed()).thenReturn(true);
        when(dirtFilter.capacity()).thenReturn(30.0d);

        WashingProgram program = WashingProgram.RINSE;
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withFillLevel(FillLevel.HALF)
                                                                        .withProgram(program)
                                                                        .withTabletsUsed(false)
                                                                        .build();

        RunResult result = dishWasher.start(programConfiguration);
        System.out.println(result.getStatus());
        System.out.println(result.getRunMinutes());

        RunResult expectedResult = RunResult.builder()
                                       .withStatus(Status.SUCCESS)
                                       .withRunMinutes(result.getRunMinutes())
                                       .build();

        System.out.println(expectedResult.getStatus());
        System.out.println(expectedResult.getRunMinutes());


        assertEquals(expectedResult.getStatus(),result.getStatus());

    }


    @Test
    void correct_washer_should_calls_methods() throws PumpException, EngineException {
        WashingProgram program = WashingProgram.RINSE;
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withFillLevel(FillLevel.HALF)
                                                                        .withProgram(program)
                                                                        .withTabletsUsed(false)
                                                                        .build();

        when(door.closed()).thenReturn(true);
        when(dirtFilter.capacity()).thenReturn(30.0d);
        dishWasher.start(programConfiguration);

        verify(waterPump).pour(FillLevel.HALF);
        verify(engine).runProgram(program);
        verify(waterPump).drain();

    }
    @Test void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }


}
