package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PetService petService;

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomer(long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.orElseThrow(CustomerNotFoundException::new);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getOwnerByPet(long petId) {
        return petService.getPet(petId).getOwner();
    }
}
