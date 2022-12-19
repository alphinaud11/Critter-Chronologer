package com.udacity.jdnd.course3.critter.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployee(long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        return optionalEmployee.orElseThrow(EmployeeNotFoundException::new);
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = getEmployee(employeeId);
        employee.setDaysAvailable(daysAvailable);
        saveEmployee(employee);
    }

    public List<Employee> findEmployeesForService(EmployeeRequestDTO employeeDTO) {
        // first we filter the employees with the given available day
        DayOfWeek availableDay = employeeDTO.getDate().getDayOfWeek();
        List<Employee> partialResult = employeeRepository.findAllByDaysAvailableContaining(availableDay);

        // now we filter the partial result to get only employees with the required skills
        Set<EmployeeSkill> requiredSkills = employeeDTO.getSkills();
        List<Employee> finalResult = new ArrayList<>();
        for (Employee employee : partialResult) {
            if (employee.getSkills().containsAll(requiredSkills))
                finalResult.add(employee);
        }
        return finalResult;
    }
}
