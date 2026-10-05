package dao;

import model.Pet;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Pet data access
public class PetDAO extends BaseDAO
{
    // Row mapper
    private Pet map(ResultSet rs) throws SQLException
    {
        Pet p = new Pet();
        p.setPetId(rs.getInt("pet_id"));
        p.setCustomerId(rs.getInt("customer_id"));
        p.setPetName(rs.getString("pet_name"));
        p.setSpecies(rs.getString("species"));
        p.setBreed(rs.getString("breed"));
        p.setGender(rs.getString("gender"));
        Date date = rs.getDate("date_of_birth");
        p.setDateOfBirth(date != null ? date.toString() : "");
        p.setWeight(rs.getDouble("weight"));
        p.setNotes(rs.getString("notes"));
        return p;
    }

    // Blank to null
    private Date toDate(String text)
    {
        return (text == null || text.trim().isEmpty()) ? null : Date.valueOf(text);
    }

    // Zero to null
    private Double toWeight(double weight)
    {
        return weight > 0 ? weight : null;
    }

    // CREATE
    public boolean addPet(Pet pet)
    {
        return executeUpdate("INSERT INTO pets (customer_id, pet_name, species, breed, gender, date_of_birth, weight, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                "add pet", pet.getCustomerId(), pet.getPetName(), pet.getSpecies(), pet.getBreed(),
                pet.getGender(), toDate(pet.getDateOfBirth()), toWeight(pet.getWeight()), pet.getNotes()) > 0;
    }

    // READ
    public List<Pet> getAllPets()
    {
        return queryList("SELECT * FROM pets", "load pets", this::map);
    }

    // UPDATE
    public boolean updatePet(Pet pet)
    {
        return executeUpdate("UPDATE pets SET customer_id = ?, pet_name = ?, species = ?, breed = ?, gender = ?, date_of_birth = ?, weight = ?, notes = ? WHERE pet_id = ?",
                "update pet", pet.getCustomerId(), pet.getPetName(), pet.getSpecies(), pet.getBreed(),
                pet.getGender(), toDate(pet.getDateOfBirth()), toWeight(pet.getWeight()), pet.getNotes(),
                pet.getPetId()) > 0;
    }

    // DELETE
    public boolean deletePet(int petId)
    {
        return executeUpdate("DELETE FROM pets WHERE pet_id = ?", "delete pet", petId) > 0;
    }
}
