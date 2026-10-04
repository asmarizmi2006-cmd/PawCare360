/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.TreatmentDAO;
import model.Treatment;

import java.util.List;

public class TreatmentService {

    private final TreatmentDAO treatmentDAO = new TreatmentDAO();

    public void addTreatment(Treatment t) {
        validate(t);
        treatmentDAO.addTreatment(t);
    }

    public List<Treatment> getAllTreatments() {
        return treatmentDAO.getAllTreatments();
    }

    public void updateTreatment(Treatment t) {
        validate(t);
        treatmentDAO.updateTreatment(t);
    }

    public void deleteTreatment(int treatmentId) {
        treatmentDAO.deleteTreatment(treatmentId);
    }

    private void validate(Treatment t) {
        if (t.getAppointmentId() <= 0) {
            throw new IllegalArgumentException("Please select a valid appointment.");
        }
        if (t.getDiagnosis() == null || t.getDiagnosis().trim().isEmpty()) {
            throw new IllegalArgumentException("Diagnosis is required.");
        }
        if (t.getTreatmentDate() == null) {
            throw new IllegalArgumentException("Treatment date is required.");
        }
    }
}
