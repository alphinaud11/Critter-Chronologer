package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CustomerService customerService;

    public Pet savePet(long ownerId, Pet pet) {
        // get owner with given id and set owner in Pet, then save Pet
        Customer owner = customerService.getCustomer(ownerId);
        pet.setOwner(owner);
        Pet savedPet = petRepository.save(pet);
        // now we need to add savedPet to the list of pets in owner then update owner (save method updates when entity has id)
        List<Pet> ownerPets = owner.getPets();
        if (ownerPets == null)
            ownerPets = new ArrayList<>();
        ownerPets.add(savedPet);
        owner.setPets(ownerPets);
        customerService.saveCustomer(owner);
        return savedPet;
    }

    public Pet getPet(long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        return optionalPet.orElseThrow(PetNotFoundException::new);
    }

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(long ownerId) {
        return customerService.getCustomer(ownerId).getPets();
    }
}
