/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Pet;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PetDAO 
{
    // CREATE
    public boolean addPet(Pet pet) 
    {
        String sql = "INSERT INTO pets "
                + "(customer_id, pet_name, species, breed, gender, "
                + "date_of_birth, weight, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setInt(1, pet.getCustomerId());
            statement.setString(2, pet.getPetName());
            statement.setString(3, pet.getSpecies());
            statement.setString(4, pet.getBreed());
            statement.setString(5, pet.getGender());

            if (pet.getDateOfBirth() == null
                    || pet.getDateOfBirth().trim().isEmpty()) 
            {
                statement.setNull(
                        6,
                        java.sql.Types.DATE
                );
            } 
            else 
            {
                statement.setDate(
                        6,
                        java.sql.Date.valueOf(
                                pet.getDateOfBirth()
                        )
                );
            }

            if (pet.getWeight() > 0) 
            {
                statement.setDouble(7, pet.getWeight());
            } 
            else 
            {
                statement.setNull(7, java.sql.Types.DECIMAL);
            }

            statement.setString(8, pet.getNotes());

            return statement.executeUpdate() > 0;

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    public List<Pet> getAllPets() 
    {
        List<Pet> pets = new ArrayList<>();

        String sql = "SELECT * FROM pets";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) 
        {
            while (resultSet.next()) 
            {
                Pet pet = new Pet();

                pet.setPetId(
                        resultSet.getInt("pet_id")
                );

                pet.setCustomerId(
                        resultSet.getInt("customer_id")
                );

                pet.setPetName(
                        resultSet.getString("pet_name")
                );

                pet.setSpecies(
                        resultSet.getString("species")
                );

                pet.setBreed(
                        resultSet.getString("breed")
                );

                pet.setGender(
                        resultSet.getString("gender")
                );

                java.sql.Date date =
                        resultSet.getDate("date_of_birth");

                if (date != null) 
                {
                    pet.setDateOfBirth(
                            date.toString()
                    );
                } 
                else
                {
                    pet.setDateOfBirth("");
                }

                pet.setWeight(
                        resultSet.getDouble("weight")
                );

                pet.setNotes(
                        resultSet.getString("notes")
                );

                pets.add(pet);
            }

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return pets;
    }

    // UPDATE
    public boolean updatePet(Pet pet)
    {
           
        String sql = "UPDATE pets SET "
                + "customer_id = ?, "
                + "pet_name = ?, "
                + "species = ?, "
                + "breed = ?, "
                + "gender = ?, "
                + "date_of_birth = ?, "
                + "weight = ?, "
                + "notes = ? "
                + "WHERE pet_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) 
        {
            statement.setInt(1, pet.getCustomerId());
            statement.setString(2, pet.getPetName());
            statement.setString(3, pet.getSpecies());
            statement.setString(4, pet.getBreed());
            statement.setString(5, pet.getGender());

            if (pet.getDateOfBirth() == null
                    || pet.getDateOfBirth().trim().isEmpty()) 
            {

                statement.setNull(
                        6,
                        java.sql.Types.DATE
                );
            } 
            else 
            {
                statement.setDate(
                        6,
                        java.sql.Date.valueOf(
                                pet.getDateOfBirth()
                        )
                );
            }

            if (pet.getWeight() > 0) 
            {
                statement.setDouble(7, pet.getWeight());
            } 
            else
            {
                statement.setNull(7, java.sql.Types.DECIMAL);
            }

            statement.setString(8, pet.getNotes());
            statement.setInt(9, pet.getPetId());

            return statement.executeUpdate() > 0;

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deletePet(int petId) 
    {

        String sql = "DELETE FROM pets WHERE pet_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) 
        {
            statement.setInt(1, petId);

            return statement.executeUpdate() > 0;

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }
}