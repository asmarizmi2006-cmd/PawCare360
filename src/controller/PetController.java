package controller;

import exception.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Customer;
import model.Pet;
import service.CustomerService;
import service.PetService;
import util.Validator;
import view.PetForm;

// Pet screen controller
public class PetController extends BaseController<PetForm>
{
    private static final Pattern LEADING_ID = Pattern.compile("^\\s*(\\d+)\\b.*$");

    private final PetService petService = new PetService();
    private final CustomerService customerService = new CustomerService();
    private final Map<Integer, String> ownerLabels = new HashMap<>();

    public PetController(PetForm view)
    {
        super(view);
        attachSidebar("PET PATIENTS");
        wire();
        guard(this::loadAll);
    }

    // Factory entry
    public static PetForm open()
    {
        PetForm v = new PetForm();
        new PetController(v);
        return v;
    }

    // Register listeners
    private void wire()
    {
        view.getBtnAdd().addActionListener(e -> guard(this::savePet));
        view.getBtnUpdate().addActionListener(e -> guard(this::updatePet));
        view.getBtnDelete().addActionListener(e -> guard(this::deletePet));
        view.getBtnClear().addActionListener(e ->
        {
            view.clearForm();
            view.getTxtPetName().requestFocusInWindow();
        });
        view.getBtnNew().addActionListener(e ->
        {
            view.clearForm();
            view.getTxtPetName().requestFocusInWindow();
        });
        view.getTblPets().getSelectionModel().addListSelectionListener(e ->
        {
            if (!e.getValueIsAdjusting())
            {
                fillFromRow();
            }
        });
        focusNext(view.getTxtPetName(), view.getTxtBreed());
        focusNext(view.getTxtBreed(), view.getTxtDateOfBirth());
        focusNext(view.getTxtDateOfBirth(), view.getTxtWeight());
        focusNext(view.getTxtWeight(), view.getTxtNotes());
    }

    // Load everything
    private void loadAll()
    {
        loadOwners();
        loadPets();
    }

    // Owner choices
    private void loadOwners()
    {
        ownerLabels.clear();
        List<String> labels = new ArrayList<>();
        List<Customer> customers = customerService.getAllCustomers();
        if (customers != null)
        {
            for (Customer c : customers)
            {
                String label = c.getCustomerId() + " - " + c.getFullName();
                ownerLabels.put(c.getCustomerId(), label);
                labels.add(label);
            }
        }
        view.setOwners(labels);
    }

    // Load table
    private void loadPets()
    {
        List<Pet> pets = petService.getAllPets();
        List<Object[]> rows = new ArrayList<>();
        if (pets != null)
        {
            for (Pet p : pets)
            {
                if (p == null)
                {
                    continue;
                }
                rows.add(new Object[]
                {
                    p.getPetId(), safe(p.getPetName()), safe(p.getSpecies()), safe(p.getBreed()),
                    safe(p.getGender()), p.getCustomerId(), safe(p.getDateOfBirth()),
                    p.getWeight(), safe(p.getNotes())
                });
            }
        }
        view.setRows(rows);
        updateCounts(rows);
        view.clearForm();
    }

    // Row to form
    private void fillFromRow()
    {
        int r = view.getTblPets().getSelectedRow();
        if (r < 0)
        {
            view.setEditEnabled(false);
            return;
        }
        String owner = view.cellText(r, 5);
        try
        {
            owner = ownerLabels.getOrDefault(Integer.parseInt(owner.trim()), owner);
        }
        catch (NumberFormatException ignored)
        {
            // keep raw
        }
        view.fillForm(view.cellText(r, 0), view.cellText(r, 1), view.cellText(r, 2),
                view.cellText(r, 3), view.cellText(r, 4), owner, view.cellText(r, 6),
                view.cellText(r, 7), view.cellText(r, 8));
    }

    // Species counts
    private void updateCounts(List<Object[]> rows)
    {
        int dogs = 0, cats = 0, rabbits = 0, birds = 0, other = 0;
        for (Object[] r : rows)
        {
            switch (String.valueOf(r[2]).trim().toLowerCase())
            {
                case "dog" -> dogs++;
                case "cat" -> cats++;
                case "rabbit" -> rabbits++;
                case "bird" -> birds++;
                default -> other++;
            }
        }
        view.setCounts(dogs, cats, rabbits, birds, other);
    }

    private void savePet()
    {
        Pet pet = readPet(false);
        if (petService.addPet(pet))
        {
            info("Pet registered successfully.");
            loadAll();
            view.getTxtPetName().requestFocusInWindow();
        }
        else
        {
            warn("The pet could not be saved.");
        }
    }

    private void updatePet()
    {
        if (view.getTblPets().getSelectedRow() < 0)
        {
            throw new ValidationException("Please select a pet record from the table first.");
        }
        Pet pet = readPet(true);
        if (!confirm("Are you sure you want to update this pet record?"))
        {
            return;
        }
        if (petService.updatePet(pet))
        {
            info("Pet record updated successfully.");
            loadAll();
            view.getTxtPetName().requestFocusInWindow();
        }
        else
        {
            warn("The pet record could not be updated.");
        }
    }

    private void deletePet()
    {
        int petId = positiveInt(view.getPetIdText(), "Invalid patient ID.");
        if (!confirm("Are you sure you want to delete this pet record?\nPet ID: " + petId))
        {
            return;
        }
        if (petService.deletePet(petId))
        {
            info("Pet record deleted successfully.");
            loadAll();
            view.getTxtPetName().requestFocusInWindow();
        }
        else
        {
            warn("The pet record could not be deleted.");
        }
    }

    // Build and validate
    private Pet readPet(boolean updating)
    {
        int petId = updating ? positiveInt(view.getPetIdText(), "Invalid patient ID.") : 0;
        Matcher m = LEADING_ID.matcher(view.getOwnerText());
        int customerId = positiveInt(m.matches() ? m.group(1) : "",
                "Please select or enter a valid Customer ID.");
        // Validate the pet record before creating the model.
        String name = Validator.requireName(view.getPetName(), "Pet name");
        String species = Validator.requireText(view.getSpecies(), "Species");
        String breed = Validator.maxLength(view.getBreed().trim(), 60, "Breed");
        String gender = Validator.requireText(view.getGender(), "Gender");
        String dob = view.getDob().trim();
        if (!dob.isEmpty())
        {
            Validator.parseDate(dob, "Date of birth");
            if (LocalDate.parse(dob).isAfter(LocalDate.now()))
            {
                throw new ValidationException("Date of birth", "Date of birth cannot be in the future.");
            }
        }
        double weight = parseWeight(view.getWeightText());
        String notes = Validator.maxLength(view.getNotes().trim(), 500, "Notes");

        return new Pet(petId, customerId, name, species, breed, gender,
                dob, weight, notes);
    }

    private double parseWeight(String text)
    {
        if (text.isEmpty())
        {
            return 0.0;
        }
        double w;
        try
        {
            w = Double.parseDouble(text);
        }
        catch (NumberFormatException ex)
        {
            throw new ValidationException("Weight must be a valid number. Example: 12.5");
        }
        if (Double.isNaN(w) || Double.isInfinite(w))
        {
            throw new ValidationException("Weight must be a valid number. Example: 12.5");
        }
        if (w < 0)
        {
            throw new ValidationException("Weight cannot be negative.");
        }
        return w;
    }

    private int positiveInt(String text, String message)
    {
        try
        {
            int v = Integer.parseInt(text.trim());
            if (v > 0)
            {
                return v;
            }
        }
        catch (NumberFormatException ignored)
        {
            // fall through
        }
        throw new ValidationException(message);
    }

    private String safe(String s)
    {
        return s == null ? "" : s;
    }
}
