package controller;

import exception.DatabaseException;
import exception.ValidationException;
import model.Medicine;
import model.TreatmentMedicine;
import service.MedicineService;
import service.PrescriptionService;
import util.Validator;
import view.PrescriptionDialog;

import javax.swing.JOptionPane;
import java.awt.Frame;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// Prescription dialog logic
public class PrescriptionController
{
    private static final Logger LOG = Logger.getLogger(PrescriptionController.class.getName());

    private final PrescriptionDialog view;
    private final int treatmentId;
    private final PrescriptionService prescriptionService = new PrescriptionService();
    private final MedicineService medicineService = new MedicineService();

    private List<Medicine> activeMedicines = new ArrayList<>();
    private List<Medicine> allMedicines = new ArrayList<>();
    private List<TreatmentMedicine> lines = new ArrayList<>();
    private int selectedMedicineId = 0;

    // Show modal dialog
    public static void open(Frame owner, int treatmentId, String title)
    {
        PrescriptionDialog dialog = new PrescriptionDialog(owner, true);
        dialog.setHeader(title);
        new PrescriptionController(dialog, treatmentId);
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
    }

    public PrescriptionController(PrescriptionDialog view, int treatmentId)
    {
        this.view = view;
        this.treatmentId = treatmentId;

        // PrescriptionDialog is not a BaseController screen, so install
        // the shared ENTER-to-next-field behaviour explicitly here.
        util.UIHelper.installEnterNavigation(view.getContentPane());

        wireEvents();
        loadStock();
        loadLines();
    }

    // Wire events
    private void wireEvents()
    {
        view.getBtnTabPrescription().addActionListener(e -> view.showStockTab(false));
        view.getBtnTabStock().addActionListener(e -> view.showStockTab(true));

        view.getBtnAddMedicine().addActionListener(e -> addLine());
        view.getBtnRemoveLine().addActionListener(e -> removeLine());
        view.getBtnClose().addActionListener(e -> view.dispose());

        view.getBtnAddStock().addActionListener(e -> saveMedicine(true));
        view.getBtnUpdateStock().addActionListener(e -> saveMedicine(false));
        view.getBtnDeleteStock().addActionListener(e -> deleteMedicine());
        view.getBtnClearStock().addActionListener(e -> clearStockForm());

        view.getTblMedicines().addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e)
            {
                fillStockForm();
            }
        });

        view.getTxtQuantity().addActionListener(e -> view.getTxtDosage().requestFocusInWindow());
        view.getTxtDosage().addActionListener(e -> view.getTxtInstructions().requestFocusInWindow());
        view.getTxtInstructions().addActionListener(e -> addLine());
        view.getTxtName().addActionListener(e -> view.getTxtCategory().requestFocusInWindow());
        view.getTxtCategory().addActionListener(e -> view.getTxtPrice().requestFocusInWindow());
        view.getTxtPrice().addActionListener(e -> view.getTxtStock().requestFocusInWindow());
        view.getTxtStock().addActionListener(e -> view.getCmbStatus().requestFocusInWindow());
    }

    // Load stock data
    private void loadStock()
    {
        guard(() ->
        {
            allMedicines = medicineService.getAllMedicines();
            activeMedicines = medicineService.getActiveMedicines();
        });
        view.setMedicineItems(activeMedicines.stream().map(Medicine::toString).collect(Collectors.toList()));
        view.setMedicineRows(allMedicines.stream().map(m -> new Object[]{m.getMedicineId(), m.getMedicineName(),
            m.getCategory(), m.getUnitPrice(), m.getStockQuantity(), m.getStatus()}).collect(Collectors.toList()));
    }

    // Load prescription
    private void loadLines()
    {
        guard(() -> lines = prescriptionService.getByTreatment(treatmentId));
        view.setLines(lines.stream().map(l -> new Object[]{l.getMedicineName(), l.getQuantity(), l.getDosage(),
            l.getInstructions(), l.getUnitPrice(), l.getLineTotal()}).collect(Collectors.toList()));
        BigDecimal total = lines.stream().map(TreatmentMedicine::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        view.setTotal(total.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    // Add a medicine to the prescription after validating every input.
    private void addLine()
    {
        int idx = view.getMedicineIndex();
        if (idx < 0 || idx >= activeMedicines.size())
        {
            message("Please select a medicine first.", "Check input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try
        {
            int medicineId = activeMedicines.get(idx).getMedicineId();
            int quantity = Validator.parsePositiveInt(view.getQuantityText(), "Quantity");
            String dosage = Validator.requireText(view.getDosageText(), "Dosage");
            String instructions = Validator.requireText(view.getInstructionsText(), "Instructions");

            prescriptionService.addMedicine(
                    treatmentId,
                    medicineId,
                    String.valueOf(quantity),
                    Validator.maxLength(dosage, 255, "Dosage"),
                    Validator.maxLength(instructions, 500, "Instructions"));

            view.clearLineInputs();
            loadStock();
            loadLines();
            message("Medicine added.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (ValidationException e)
        {
            message(e.getMessage(), "Check input", JOptionPane.WARNING_MESSAGE);
        }
        catch (DatabaseException e)
        {
            message(e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove line
    private void removeLine()
    {
        int row = view.getSelectedLineRow();
        if (row < 0 || row >= lines.size())
        {
            message("Please select a medicine line first.", "Check input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!confirm("Remove this medicine and restore stock?"))
        {
            return;
        }
        int id = lines.get(row).getId();
        guard(() ->
        {
            prescriptionService.removeLine(id);
            loadStock();
            loadLines();
        });
    }

    // Add or update
    private void saveMedicine(boolean isNew)
    {
        guard(() ->
        {
            Medicine m = new Medicine();
            m.setMedicineName(Validator.maxLength(
                    Validator.requireText(view.getNameText(), "Medicine name"), 100, "Medicine name"));
            m.setCategory(Validator.maxLength(
                    Validator.requireText(view.getCategoryText(), "Category"), 50, "Category"));
            m.setUnitPrice(Validator.parseMoney(view.getPriceText(), "Unit price"));

            String stock = view.getStockText().trim();
            int stockQuantity = Validator.parsePositiveInt(stock, "Stock");
            m.setStockQuantity(stockQuantity);

            String status = Validator.requireText(view.getStatusText(), "Status");
            if (!status.equals("Active") && !status.equals("Inactive"))
            {
                throw new ValidationException("Status", "Please select Active or Inactive.");
            }
            m.setStatus(status);
            if (isNew)
            {
                medicineService.addMedicine(m);
            }
            else
            {
                m.setMedicineId(selectedMedicineId);
                medicineService.updateMedicine(m);
            }
            clearStockForm();
            loadStock();
            message(isNew ? "Medicine added." : "Medicine updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void deleteMedicine()
    {
        if (selectedMedicineId <= 0)
        {
            message("Please select a medicine first.", "Check input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!confirm("Delete this medicine?"))
        {
            return;
        }
        guard(() ->
        {
            medicineService.deleteMedicine(selectedMedicineId);
            clearStockForm();
            loadStock();
        });
    }

    // Row to form
    private void fillStockForm()
    {
        int row = view.getSelectedMedicineRow();
        if (row < 0 || row >= allMedicines.size())
        {
            return;
        }
        Medicine m = allMedicines.get(row);
        selectedMedicineId = m.getMedicineId();
        view.fillStockForm(m.getMedicineName(), m.getCategory() == null ? "" : m.getCategory(),
                m.getUnitPrice() == null ? "" : m.getUnitPrice().toPlainString(),
                String.valueOf(m.getStockQuantity()), m.getStatus());
    }

    private void clearStockForm()
    {
        selectedMedicineId = 0;
        view.clearStockForm();
    }

    // Error handling
    private void guard(Runnable action)
    {
        try
        {
            action.run();
        }
        catch (ValidationException e)
        {
            message(e.getMessage(), "Check input", JOptionPane.WARNING_MESSAGE);
        }
        catch (DatabaseException e)
        {
            message(e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (RuntimeException e)
        {
            LOG.log(Level.SEVERE, "Unexpected error", e);
            message("Something went wrong: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void message(String text, String title, int type)
    {
        JOptionPane.showMessageDialog(view, text, title, type);
    }

    private boolean confirm(String text)
    {
        return JOptionPane.showConfirmDialog(view, text, "Please confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
