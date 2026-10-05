/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Encapsulation + Abstraction 
public class AccessControl {

    // Collections + Generics 
    private static final Map<String, List<String>> ROLE_ACCESS = new HashMap<>();

    static {
        ROLE_ACCESS.put("ADMIN", Arrays.asList(
            "DASHBOARD", "CUSTOMERS", "PET PATIENTS", "APPOINTMENTS", "STAFF",
            "SERVICES", "TREATMENTS", "GROOMING", "BOARDING", "BILLING", "REPORTS"
        ));
        ROLE_ACCESS.put("MANAGER", Arrays.asList(
            "DASHBOARD", "CUSTOMERS", "PET PATIENTS", "APPOINTMENTS", "STAFF",
            "SERVICES", "TREATMENTS", "GROOMING", "BOARDING", "BILLING", "REPORTS"
        ));
        ROLE_ACCESS.put("VETERINARIAN", Arrays.asList(
            "DASHBOARD", "PET PATIENTS", "APPOINTMENTS", "TREATMENTS"
        ));
        ROLE_ACCESS.put("NURSE", Arrays.asList(
            "DASHBOARD", "PET PATIENTS", "APPOINTMENTS", "TREATMENTS"
        ));
        ROLE_ACCESS.put("GROOMER", Arrays.asList(
            "DASHBOARD", "APPOINTMENTS", "GROOMING"
        ));
        ROLE_ACCESS.put("RECEPTIONIST", Arrays.asList(
            "DASHBOARD", "CUSTOMERS", "PET PATIENTS", "APPOINTMENTS",
            "GROOMING", "BOARDING", "BILLING"
        ));
    }

    // Abstraction 
    public static boolean canAccess(String role, String screenName) {
        if (role == null || screenName == null) return false;
        List<String> allowed = ROLE_ACCESS.get(role.toUpperCase());
        return allowed != null && allowed.contains(screenName.toUpperCase());
    }
}