package com.udacity.jdnd.course3.critter.user.service;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.entity.Customer;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PetRepository petRepository;

    public Customer saveCustomer(Customer customer) {
        Customer saveCustomer = customerRepository.save(customer);
        if (customer.getPets() != null) {
            saveCustomer.getPets().forEach(pet -> savePet(pet, saveCustomer));
        }
        return saveCustomer;
    }

    private void savePet(Pet pet, Customer customer) {
        pet.setOwner(customer);
        petRepository.save(pet);
    }

    public List<Customer> getAllCustomer() {
        return (List<Customer>) customerRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        } else {
            throw new EmployeeNotFoundException(String.format("Employee with id %d wasn't found", id));
        }
    }

    public Set<Employee> findEmployeesBySkills(Set<EmployeeSkill> employeeSkills) {
        Set<Employee> employees = new HashSet<>();

        employeeSkills.stream().forEach(
                employeeSkill -> employees.addAll(employeeRepository.findAllBySkillsContains(employeeSkill))
        );

        return employees;
    }
}
