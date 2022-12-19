package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleService.createSchedule(convertToSchedule(scheduleDTO));
        return convertToScheduleDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return convertToScheduleDTOList(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return convertToScheduleDTOList(scheduleService.getScheduleForPet(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return convertToScheduleDTOList(scheduleService.getScheduleForEmployee(employeeId));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        return convertToScheduleDTOList(scheduleService.getScheduleForCustomer(customerId));
    }

    public Schedule convertToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        if (scheduleDTO.getEmployeeIds() != null) {
            List<Employee> employees = scheduleDTO.getEmployeeIds().stream().map(id -> employeeService.getEmployee(id)).collect(Collectors.toList());
            schedule.setEmployees(employees);
        }
        if (scheduleDTO.getPetIds() != null) {
            List<Pet> pets = scheduleDTO.getPetIds().stream().map(id -> petService.getPet(id)).collect(Collectors.toList());
            schedule.setPets(pets);
        }
        return schedule;
    }

    public ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        if (schedule.getEmployees() != null) {
            List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
            scheduleDTO.setEmployeeIds(employeeIds);
        }
        if (schedule.getPets() != null) {
            List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
            scheduleDTO.setPetIds(petIds);
        }
        return scheduleDTO;
    }

    public List<ScheduleDTO> convertToScheduleDTOList(List<Schedule> scheduleList) {
        return scheduleList.stream().map(this::convertToScheduleDTO).collect(Collectors.toList());
    }
}
