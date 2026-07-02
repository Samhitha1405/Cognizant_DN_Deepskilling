package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Employee {
    private static final Logger LOGGER = LoggerFactory.getLogger(Employee.class);

    @NotNull
    private String id;

    @NotNull
    @Size(min = 2, message = "First name should be at least 2 characters")
    private String firstName;

    @NotNull
    @Size(min = 2, message = "Last name should be at least 2 characters")
    private String lastName;

    @Valid
    @NotNull
    private Department department;

    public Employee() {
        LOGGER.debug("Inside Employee Constructor.");
    }

    public String getId() {
        LOGGER.debug("getId");
        return id;
    }

    public void setId(String id) {
        LOGGER.debug("setId: {}", id);
        this.id = id;
    }

    public String getFirstName() {
        LOGGER.debug("getFirstName");
        return firstName;
    }

    public void setFirstName(String firstName) {
        LOGGER.debug("setFirstName: {}", firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        LOGGER.debug("getLastName");
        return lastName;
    }

    public void setLastName(String lastName) {
        LOGGER.debug("setLastName: {}", lastName);
        this.lastName = lastName;
    }

    public Department getDepartment() {
        LOGGER.debug("getDepartment");
        return department;
    }

    public void setDepartment(Department department) {
        LOGGER.debug("setDepartment: {}", department);
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", firstName=" + firstName
                + ", lastName=" + lastName + ", department=" + department + "]";
    }
}