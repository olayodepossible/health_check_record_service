package com.possible.patientdatabatchloader.repository;

import com.possible.patientdatabatchloader.model.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientRecord, Long>{

}
