package com.musala.dronetask.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.musala.dronetask.enums.Model;
import com.musala.dronetask.enums.State;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity()
@Table(name = "drones")
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique=true)
    @NotBlank(message = "Serial Number is mandatory")
    private String serialNumber;

    @Column()
    @Enumerated(EnumType.STRING)
    private Model model;

    @Column()
    private int weightLimit;

    @Column()
    @Range(min=0, max=100)
    private int batteryCapacity;

    @Column()
    @Enumerated(EnumType.STRING)
    private State state;

    @JsonManagedReference
    @OneToMany(mappedBy = "drone", fetch = FetchType.EAGER)
    private List<Medication> medications;

    public Drone(){}

    public Drone(String serialNumber, Model model, int weightLimit, int batteryCapacity, State state) {
      
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
        this.serialNumber = serialNumber;
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}
