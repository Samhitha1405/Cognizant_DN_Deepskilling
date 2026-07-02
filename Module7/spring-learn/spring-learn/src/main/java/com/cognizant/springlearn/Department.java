package com.cognizant.springlearn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Department {
    private static final Logger LOGGER = LoggerFactory.getLogger(Department.class);

    private String code;
    private String name;

    public Department() {
        LOGGER.debug("Inside Department Constructor.");
    }

    public String getCode() {
        LOGGER.debug("getCode");
        return code;
    }

    public void setCode(String code) {
        LOGGER.debug("setCode: {}", code);
        this.code = code;
    }

    public String getName() {
        LOGGER.debug("getName");
        return name;
    }

    public void setName(String name) {
        LOGGER.debug("setName: {}", name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department [code=" + code + ", name=" + name + "]";
    }
}