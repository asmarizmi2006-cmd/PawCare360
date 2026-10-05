/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.ValidationException;
import dao.PetDAO;
import model.Pet;

import java.util.List;

public class PetService 
{
    private final PetDAO petDAO;

    public PetService() 
    {
        petDAO = new PetDAO();
    }

    // CREATE
    public boolean addPet(Pet pet) 
    {
        if (pet.getCustomerId() <= 0) 
        {
            throw new ValidationException(
                    "Please select a customer."
            );
        }
        if (pet.getPetName() == null
                || pet.getPetName().trim().isEmpty()) 
        {
            throw new ValidationException(
                    "Pet name is required."
            );
        }

        if (pet.getSpecies() == null
                || pet.getSpecies().trim().isEmpty()) 
        {
            throw new ValidationException(
                    "Species is required."
            );
        }

        if (pet.getWeight() < 0) 
        {
            throw new ValidationException(
                    "Weight cannot be negative."
            );
        }

        return petDAO.addPet(pet);
    }

    // READ
    public List<Pet> getAllPets() 
    {
        return petDAO.getAllPets();
    }

    // UPDATE
    public boolean updatePet(Pet pet) 
    {
        if (pet.getPetId() <= 0) 
        {
            throw new ValidationException(
                    "Invalid pet ID."
            );
        }

        if (pet.getCustomerId() <= 0) 
        {
            throw new ValidationException(
                    "Please select a customer."
            );
        }

        if (pet.getPetName() == null
                || pet.getPetName().trim().isEmpty()) 
        {
            throw new ValidationException(
                    "Pet name is required."
            );
        }

        if (pet.getSpecies() == null
                || pet.getSpecies().trim().isEmpty()) 
        {
            throw new ValidationException(
                    "Species is required."
            );
        }

        if (pet.getWeight() < 0) 
        {
            throw new ValidationException(
                    "Weight cannot be negative."
            );
        }

        return petDAO.updatePet(pet);
    }

    // DELETE
    public boolean deletePet(int petId) 
    {
        if (petId <= 0) 
        {
            throw new ValidationException(
                    "Invalid pet ID."
            );
        }
        return petDAO.deletePet(petId);
    }
}
