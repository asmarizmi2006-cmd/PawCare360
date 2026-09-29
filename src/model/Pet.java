/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Pet 
{

    private int petId;
    private int customerId;
    private String petName;
    private String species;
    private String breed;
    private String gender;
    private String dateOfBirth;
    private double weight;
    private String notes;

    public Pet() {
    }

    public Pet(int petId, int customerId, String petName,
               String species, String breed, String gender,
               String dateOfBirth, double weight, String notes) 
    {
        this.petId = petId;
        this.customerId = customerId;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.weight = weight;
        this.notes = notes;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}