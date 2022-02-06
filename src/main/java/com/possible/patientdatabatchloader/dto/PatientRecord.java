package com.possible.patientdatabatchloader.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PatientRecord implements Serializable {

    private String sourceId;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String birthDate;
    private String action;
    private String ssn;
}
