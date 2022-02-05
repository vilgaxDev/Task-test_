package com.musala.dronetask.repositories;

import com.musala.dronetask.entities.DroneBatteryAudit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneBatteryAuditRepository extends CrudRepository<DroneBatteryAudit, Long> {
}
