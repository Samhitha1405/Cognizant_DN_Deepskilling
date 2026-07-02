package com.cognizant.springlearn.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.cognizant.springlearn.Employee;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;

@Repository
public class EmployeeDao {
    private static List<Employee> EMPLOYEE_LIST;

    @SuppressWarnings("unchecked")
    public EmployeeDao() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("employee.xml");
        EMPLOYEE_LIST = (List<Employee>) context.getBean("employeeList");
    }

    public List<Employee> getAllEmployees() {
        return EMPLOYEE_LIST;
    }

    public Employee getEmployee(String id) {
        return EMPLOYEE_LIST.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public void updateEmployee(Employee employee) {
        Employee existing = getEmployee(employee.getId());
        existing.setFirstName(employee.getFirstName());
        existing.setLastName(employee.getLastName());
        existing.setDepartment(employee.getDepartment());
    }

    public void deleteEmployee(String id) {
        Employee existing = getEmployee(id);
        EMPLOYEE_LIST.remove(existing);
    }
}