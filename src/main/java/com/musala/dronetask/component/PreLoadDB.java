package com.musala.dronetask.component;

import com.musala.dronetask.entities.Drone;
import com.musala.dronetask.enums.Model;
import com.musala.dronetask.enums.State;
import com.musala.dronetask.repositories.DroneRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PreLoadDB {
    private DroneRepository droneRepository;

    public PreLoadDB(DroneRepository droneRepository){
        this.droneRepository = droneRepository;
    }


    @PostConstruct
    private void postConstruct() {
        //add 10 drones
        for(int i = 0; i < 10; i++){
            droneRepository.save(new Drone("1234567890" + i, Model.LightWeight, 500, 100, State.IDLE));
        }
    }
    
}
