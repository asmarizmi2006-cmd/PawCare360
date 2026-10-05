package service;

import dao.TreatmentDAO;
import model.Treatment;
import util.Validator;

import java.util.List;

// Treatment business rules
public class TreatmentService
{
    private final TreatmentDAO treatmentDAO = new TreatmentDAO();

    public void addTreatment(Treatment t)
    {
        validate(t);
        treatmentDAO.addTreatment(t);
    }

    public List<Treatment> getAllTreatments()
    {
        return treatmentDAO.getAllTreatments();
    }

    public void updateTreatment(Treatment t)
    {
        Validator.requireSelected(t.getTreatmentId(), "Treatment");
        validate(t);
        treatmentDAO.updateTreatment(t);
    }

    public void deleteTreatment(int treatmentId)
    {
        Validator.requireSelected(treatmentId, "Treatment");
        treatmentDAO.deleteTreatment(treatmentId);
    }

    // Input checks
    private void validate(Treatment t)
    {
        Validator.requireSelected(t.getAppointmentId(), "Appointment");
        Validator.requireSelected(t.getStaffId() == null ? 0 : t.getStaffId(), "Staff");
        t.setDiagnosis(Validator.requireText(t.getDiagnosis(), "Diagnosis"));
        Validator.maxLength(t.getDiagnosis(), 255, "Diagnosis");
        if (t.getTreatmentDate() == null)
        {
            throw new exception.ValidationException("Treatment date", "Treatment date is required.");
        }
        Validator.maxLength(t.getTreatmentDetails(), 1000, "Treatment details");
        Validator.maxLength(t.getNotes(), 500, "Notes");
    }
}
