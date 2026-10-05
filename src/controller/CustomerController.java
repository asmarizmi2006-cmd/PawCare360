package controller;

import exception.ValidationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import model.Customer;
import service.CustomerService;
import view.CustomerForm;

// Customer screen controller
public class CustomerController extends BaseController<CustomerForm> {

    private final CustomerService customerService = new CustomerService();

    public CustomerController(CustomerForm view) {
        super(view);

        attachSidebar("CUSTOMERS");

        wire();

        guard(this::loadCustomers);
    }

    // Factory entry
    public static CustomerForm open() {

        CustomerForm v = new CustomerForm();

        new CustomerController(v);

        return v;
    }

   
    // REGISTER LISTENERS
   
    private void wire() {

        // ADD
        view.getBtnAdd().addActionListener(
                e -> guard(this::addCustomer)
        );

        // UPDATE
        view.getBtnUpdate().addActionListener(
                e -> guard(this::updateCustomer)
        );

        // DELETE
        view.getBtnDelete().addActionListener(
                e -> guard(this::deleteCustomer)
        );

        // CLEAR
        view.getBtnClear().addActionListener(
                e -> view.clearForm()
        );

        // NEW CUSTOMER
        view.getBtnNew().addActionListener(e -> {

            view.clearForm();

            view.getTxtFullName().requestFocus();
        });

        // TABLE ROW CLICK
        view.getTable().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                fillFromRow();
            }
        });

        // TAB / ENTER NAVIGATION
        focusNext(
                view.getTxtFullName(),
                view.getTxtPhone()
        );

        focusNext(
                view.getTxtPhone(),
                view.getTxtEmail()
        );

        focusNext(
                view.getTxtEmail(),
                view.getTxtAddress()
        );

        // Press ENTER in address = Add
        view.getTxtAddress().addActionListener(
                e -> view.getBtnAdd().doClick()
        );
    }

    
    // FILL FORM FROM SELECTED TABLE ROW

    private void fillFromRow() {

        int r = view.getTable().getSelectedRow();

        if (r < 0) {
            return;
        }

        view.fillForm(
                view.cellText(r, 0),
                view.cellText(r, 1),
                view.cellText(r, 2),
                view.cellText(r, 3),
                view.cellText(r, 4),
                view.cellText(r, 5)
        );
    }

    
    // ADD CUSTOMER
    
    private void addCustomer() {

        // Validate all customer fields
        validateInput();

        // Convert form data into Customer object
        Customer c = readCustomer();

        // Save customer
        if (customerService.addCustomer(c)) {

            info("Customer added successfully!");

            view.clearForm();

            loadCustomers();

        } else {

            error("Failed to add customer.");
        }
    }

    
    // UPDATE CUSTOMER
    
    private void updateCustomer() {

        String customerId = view.getCustomerIdText();

        // Customer ID required
        if (customerId.isEmpty()) {

            throw new ValidationException(
                    "Customer ID",
                    "Please select a customer to update."
            );
        }

        // Customer ID must be a number
        int id;

        try {

            id = Integer.parseInt(customerId);

        } catch (NumberFormatException e) {

            throw new ValidationException(
                    "Customer ID",
                    "Invalid customer ID."
            );
        }

        // Validate remaining fields
        validateInput();

        // Create Customer object
        Customer c = readCustomer();

        c.setCustomerId(id);

        // Update customer
        if (customerService.updateCustomer(c)) {

            info("Customer updated successfully!");

            view.clearForm();

            loadCustomers();

        } else {

            error("Customer update failed.");
        }
    }


    // DELETE CUSTOMER
    
    private void deleteCustomer() {

        String customerId = view.getCustomerIdText();

        // Customer ID required
        if (customerId.isEmpty()) {

            throw new ValidationException(
                    "Customer ID",
                    "Please select a customer to delete."
            );
        }

        int id;

        // Validate Customer ID
        try {

            id = Integer.parseInt(customerId);

        } catch (NumberFormatException e) {

            throw new ValidationException(
                    "Customer ID",
                    "Invalid customer ID."
            );
        }

        // Confirmation
        if (!confirm(
                "Are you sure you want to delete this customer?"
        )) {

            return;
        }

        // Delete customer
        if (customerService.deleteCustomer(id)) {

            info("Customer deleted successfully!");

            view.clearForm();

            loadCustomers();

        } else {

            error("Customer could not be deleted.");
        }
    }

    
    // FORM DATA → CUSTOMER MODEL
    
    private Customer readCustomer() {

        Customer c = new Customer();

        c.setFullName(
                view.getFullNameText()
        );

        c.setPhone(
                view.getPhoneText()
        );

        c.setEmail(
                view.getEmailText()
        );

        c.setAddress(
                view.getAddressText()
        );

        c.setStatus(
                view.getStatusText()
        );

        return c;
    }

    
    // LOAD CUSTOMERS
    
    private void loadCustomers() {

        List<Customer> customers =
                customerService.getAllCustomers();

        List<Object[]> rows =
                new ArrayList<>();

        int active = 0;

        for (Customer c : customers) {

            rows.add(
                    new Object[]{
                        c.getCustomerId(),
                        c.getFullName(),
                        c.getPhone(),
                        c.getEmail(),
                        c.getAddress(),
                        c.getStatus()
                    }
            );

            if ("Active".equalsIgnoreCase(
                    c.getStatus())) {

                active++;
            }
        }

        // Display table
        view.setRows(rows);

        // Display statistics
        view.setStats(
                customers.size(),
                active,
                customers.size() - active
        );
    }

    
    // CUSTOMER INPUT VALIDATION
    private void validateInput()
            throws ValidationException {

        String name =
                view.getFullNameText();

        String phone =
                view.getPhoneText();

        String email =
                view.getEmailText();

        String address =
                view.getAddressText();

        String status =
                view.getStatusText();

        
        // FULL NAME
        

        if (name.isEmpty()) {

            fail(
                    "Full Name",
                    "Please enter the customer's full name.",
                    view.getTxtFullName()
            );
        }

        if (!name.matches("[a-zA-Z .']+")) {

            fail(
                    "Full Name",
                    "Full name can contain only letters, "
                    + "spaces, dots and apostrophes.",
                    view.getTxtFullName()
            );
        }

        
        // PHONE
        
        if (phone.isEmpty()) {

            fail(
                    "Phone",
                    "Please enter the customer's phone number.",
                    view.getTxtPhone()
            );
        }

        if (!phone.matches(
                "^(07\\d{8}|\\+947\\d{8})$")) {

            fail(
                    "Phone",
                    "Please enter a valid Sri Lankan mobile number.\n"
                    + "Example: 0771234567",
                    view.getTxtPhone()
            );
        }

        
        // EMAIL
        if (email.isEmpty()) {

            fail(
                    "Email",
                    "Please enter the customer's email address.",
                    view.getTxtEmail()
            );
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            fail(
                    "Email",
                    "Please enter a valid email address.\n"
                    + "Example: john@gmail.com",
                    view.getTxtEmail()
            );
        }

        
        // ADDRESS
        if (address.isEmpty()) {

            fail(
                    "Address",
                    "Please enter the customer's address.",
                    view.getTxtAddress()
            );
        }

        if (address.length() < 5) {

            fail(
                    "Address",
                    "Please enter a complete address.",
                    view.getTxtAddress()
            );
        }

        
        // STATUS
        

        if (status == null
                || status.trim().isEmpty()
                || status.trim().equals(" ")) {

            fail(
                    "Status",
                    "Please select customer status.",
                    view.getCmbStatus()
            );
        }
    }


    // VALIDATION FAILURE
    private void fail(
            String fieldName,
            String message,
            JComponent field) {

        // Move cursor to invalid field
        field.requestFocus();

        // Throw user-defined exception
        throw new ValidationException(
                fieldName,
                message
        );
    }
}