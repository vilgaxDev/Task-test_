package com.musala.dronetask.services;

import com.musala.dronetask.dto.DroneDTO;
import com.musala.dronetask.dto.MedicationDTO;
import com.musala.dronetask.enums.Model;
import com.musala.dronetask.enums.State;
import com.musala.dronetask.repositories.DroneRepository;
import com.musala.dronetask.repositories.MedicationRepository;
import com.musala.dronetask.entities.Drone;
import com.musala.dronetask.exceptions.DroneBatteryLow;
import com.musala.dronetask.exceptions.DroneIllegalLoading;
import com.musala.dronetask.exceptions.DroneNotFound;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
class DroneServiceTest {
    DroneRepository droneRepository = Mockito.mock(DroneRepository.class);
    MedicationRepository medicationRepository = Mockito.mock(MedicationRepository.class);

    DroneService droneService;
    enum IDs {
        DroneIdle(1L),
        DroneLoading(2L),
        DroneNotFoundId(3L),
        DroneOthers(4L),
        DroneBatterBelow25Percent(5L);

        public long value;

        private IDs(long id){
            this.value = id;
        }
    };


    @BeforeEach
    public void before(){
        droneService = new DroneService(droneRepository, medicationRepository);
        AtomicLong droneId = new AtomicLong();
        Mockito.when(droneRepository.save(Mockito.any(Drone.class))).thenAnswer(i -> {
            Drone drone = (Drone) i.getArguments()[0];
            drone.setId(droneId.incrementAndGet());
            return drone;
        });
        Mockito.when(droneRepository.findById(Mockito.anyLong())).thenAnswer( i -> {
            if((Long) i.getArguments()[0] == 1L){
                Drone drone = getIdleDrone();
                drone.setId((Long) i.getArguments()[0]);
                return Optional.of(drone);
            }else if((Long) i.getArguments()[0] == 2L){
                Drone drone = getLoadingDrone();
                drone.setId((Long) i.getArguments()[0]);
                return Optional.of(drone);
            }else if((Long) i.getArguments()[0] == 3L){
                return Optional.empty();
            }else if((Long) i.getArguments()[0] == 4L){
                Drone drone = getOtherDrone();
                drone.setId((Long) i.getArguments()[0]);
                return Optional.of(drone);
            }else{
                Drone drone = getBelow25PercentDrone();
                drone.setId((Long) i.getArguments()[0]);
                return Optional.of(drone);
            }
        });
        Mockito.when(droneRepository.findByState(Mockito.any())).thenAnswer(i -> Arrays.asList(getIdleDrone()));

//        Mockito.when(medicationRepository.save()).thenReturn(111L);

    }
    public Drone getIdleDrone(){
        return new Drone("12345678", Model.LightWeight, 500, 100, State.IDLE);
    }
    public Drone getLoadingDrone(){
        return new Drone("12345678", Model.LightWeight, 500, 100, State.LOADING);
    }
    public Drone getOtherDrone(){
        return new Drone("12345678", Model.LightWeight, 500, 100, State.LOADED);
    }
    public Drone getBelow25PercentDrone(){
        return new Drone("12345678", Model.LightWeight, 500, 24, State.IDLE);
    }
    public MedicationDTO getMedicationDTO(){
        return new MedicationDTO("drone1", 500, "drone_100gms", "shorturl.at/fiAEM");
    }
    public DroneDTO getExpectedDrone(){
        return new DroneDTO("12345678", Model.LightWeight, 500, 100, State.IDLE);
    }



    @Test
    void testThat_when_save_it_should_return_drone() {
        DroneDTO expectedDrone = getExpectedDrone();
        DroneDTO register = droneService.register(expectedDrone);
        assertEquals(expectedDrone.getSerialNumber(), register.getSerialNumber());
        Assertions.assertEquals(expectedDrone.getModel(), register.getModel());
        assertEquals(expectedDrone.getWeightLimit(), register.getWeightLimit());
        assertEquals(expectedDrone.getBatteryCapacity(), register.getBatteryCapacity());
        Assertions.assertEquals(State.IDLE, register.getState());
    }

    @Test
    void testThat_when_save_state_should_be_idle() {
        DroneDTO expectedDrone = getExpectedDrone();
        DroneDTO register = droneService.register(expectedDrone);
        Assertions.assertEquals(State.IDLE, register.getState());
    }

    // loadDrone
    @Test
    void testThat_idle_state_drones_can_be_loaded() {
        assertDoesNotThrow(() -> {
            MedicationDTO medicationDTO = droneService.loadDrone(IDs.DroneIdle.value, getMedicationDTO());
            assertNotNull(medicationDTO.getDrone());
        });
    }

    @Test
    void testThat_loading_state_drones_can_be_loaded() {
        assertDoesNotThrow(() -> {
            MedicationDTO medicationDTO = droneService.loadDrone(IDs.DroneLoading.value, getMedicationDTO());
            assertNotNull(medicationDTO.getDrone());
        });
    }

    @Test
    void testThat_exception_is_thrown_when_drone_not_found() {
        assertThrows(DroneNotFound.class, () -> {
            droneService.loadDrone(IDs.DroneNotFoundId.value, getMedicationDTO());
        });
    }

    @Test
    void testThat_only_idle_and_loading_state_drones_can_be_loaded() {
        assertThrows(DroneIllegalLoading.class, () -> {
            droneService.loadDrone(IDs.DroneOthers.value, getMedicationDTO());
        });
    }

    @Test
    void testThat_exception_is_thrown_when_drone_battery_below_25_percent() {
        assertThrows(DroneBatteryLow.class, () -> {
            droneService.loadDrone(IDs.DroneBatterBelow25Percent.value, getMedicationDTO());
        });
    }

    @Test
    void testThat_getAvailableDrones_returns() {
        List<DroneDTO> availableDrones = droneService.getAvailableDrones();
        assertNotNull(availableDrones);
    }

    @Test
    void getDrone() {
        assertDoesNotThrow(() -> {
            DroneDTO expectedDrone = getExpectedDrone();
            DroneDTO register = droneService.getDrone(1L);
            assertEquals(expectedDrone.getSerialNumber(), register.getSerialNumber());
            Assertions.assertEquals(expectedDrone.getModel(), register.getModel());
            assertEquals(expectedDrone.getWeightLimit(), register.getWeightLimit());
            assertEquals(expectedDrone.getBatteryCapacity(), register.getBatteryCapacity());
            Assertions.assertEquals(State.IDLE, register.getState());
        });
    }

    @Test
    void testThat_exception_is_thrown_when_get_drone_not_found() {
        assertThrows(DroneNotFound.class, () -> {
            droneService.getDrone(IDs.DroneNotFoundId.value);
        });
    }

}