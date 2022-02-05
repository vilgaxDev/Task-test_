package com.musala.dronetask.repositories;

import com.musala.dronetask.enums.State;
import com.musala.dronetask.entities.Drone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends CrudRepository<Drone, Long> {
    List<Drone> findByState(State state);
}
