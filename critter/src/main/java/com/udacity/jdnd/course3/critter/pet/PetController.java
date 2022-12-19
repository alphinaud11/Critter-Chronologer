package com.udacity.jdnd.course3.critter.pet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        long ownerId = petDTO.getOwnerId();
        Pet savedPet = petService.savePet(ownerId, convertToPet(petDTO));
        return convertToPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        return convertToPetDTO(petService.getPet(petId));
    }

    @GetMapping
    public List<PetDTO> getPets(){
        return convertToPetDTOList(petService.getPets());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        return convertToPetDTOList(petService.getPetsByOwner(ownerId));
    }

    public Pet convertToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }

    public PetDTO convertToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner() != null)
            petDTO.setOwnerId(pet.getOwner().getId());
        return petDTO;
    }

    public List<PetDTO> convertToPetDTOList(List<Pet> petList) {
        return petList.stream().map(this::convertToPetDTO).collect(Collectors.toList());
    }
}
