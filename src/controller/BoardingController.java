package controller;

import dao.BoardingBookingDAO;
import exception.DatabaseException;
import exception.ValidationException;
import model.BoardingBooking;
import model.BoardingRoom;
import model.Customer;
import model.Pet;
import service.BoardingBookingService;
import service.BoardingRoomService;
import service.CustomerService;
import service.PetService;
import util.Validator;
import view.BoardingForm;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

// Boarding screen logic
public class BoardingController extends BaseController<BoardingForm>
{
    private static final String[] STATUSES = {"Booked", "Checked In", "Checked Out", "Cancelled"};
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");
    private static final DecimalFormat RATE = new DecimalFormat("#,##0.##");

    private final CustomerService customerService = new CustomerService();
    private final PetService petService = new PetService();
    private final BoardingRoomService roomService = new BoardingRoomService();
    private final BoardingBookingService bookingService = new BoardingBookingService();

    private List<Customer> customerList = new ArrayList<>();
    private List<Pet> petList = new ArrayList<>();
    private List<Pet> filteredPetList = new ArrayList<>();
    private List<BoardingRoom> availableRooms = new ArrayList<>();
    private List<BoardingRoom> allRooms = new ArrayList<>();
    private List<BoardingBooking> bookings = new ArrayList<>();
    private Map<Integer, String> customerNames;
    private Map<Integer, String> petNames;
    private Map<Integer, String> roomNames;

    public static BoardingForm open()
    {
        return new BoardingController(new BoardingForm()).getView();
    }

    private BoardingController(BoardingForm view)
    {
        super(view);
        attachSidebar("BOARDING");
        wire();
        loadDropdowns();
        refreshAll();
    }

    // Event wiring
    private void wire()
    {
        view.getCmbCustomer().addItemListener(e ->
        {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                int index = view.getCmbCustomer().getSelectedIndex();
                if (index >= 0 && index < customerList.size())
                {
                    filterPets(customerList.get(index).getCustomerId());
                }
            }
        });
        view.getTblBoarding().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.getTblBoarding().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                fillFormFromTable();
            }
        });
        view.getBtnBookRoom().addActionListener(e -> handleBook());
        view.getBtnCheckOut().addActionListener(e -> handleCheckOut());
        view.getBtnDeleteBooking().addActionListener(e -> handleDelete());
        view.getBtnClearBoarding().addActionListener(e -> clearForm());
        focusNext(view.getTxtCheckIn(), view.getTxtCheckOut());
        view.getTxtCheckOut().addActionListener(e ->
        {
            previewTotal();
            view.getTxtNotes().requestFocusInWindow();
        });
        view.getTxtNotes().addActionListener(e -> view.getBtnBookRoom().doClick());
    }

    // Shared error handling
    private void handleError(Exception ex)
    {
        if (ex instanceof ValidationException)
        {
            view.showMessage(ex.getMessage(), false);
            warn(ex.getMessage());
        }
        else if (ex instanceof DatabaseException)
        {
            LOG.log(Level.SEVERE, null, ex);
            view.showMessage(ex.getMessage(), false);
            error(ex.getMessage());
        }
        else
        {
            LOG.log(Level.SEVERE, null, ex);
            view.showMessage("Something went wrong. Please try again.", false);
            error("Something went wrong. Please try again.");
        }
    }

    private void loadDropdowns()
    {
        try
        {
            customerList = customerService.getAllCustomers();
            customerNames = customerList.stream().collect(Collectors.toMap(Customer::getCustomerId, Customer::getFullName, (a, b) -> a));
            view.getCmbCustomer().removeAllItems();
            customerList.forEach(c -> view.getCmbCustomer().addItem(c.getFullName()));

            petList = petService.getAllPets();
            petNames = petList.stream().collect(Collectors.toMap(Pet::getPetId, Pet::getPetName, (a, b) -> a));

            view.getCmbStatus().removeAllItems();
            for (String s : STATUSES)
            {
                view.getCmbStatus().addItem(s);
            }
            if (!customerList.isEmpty())
            {
                filterPets(customerList.get(0).getCustomerId());
            }
            loadRooms();
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    // Reload rooms
    private void loadRooms()
    {
        allRooms = roomService.getAllRooms();
        roomNames = allRooms.stream().collect(Collectors.toMap(BoardingRoom::getRoomId, BoardingRoom::getRoomNumber, (a, b) -> a));
        availableRooms = allRooms.stream().filter(r -> "Available".equalsIgnoreCase(r.getStatus())).collect(Collectors.toList());
        view.getCmbRoom().removeAllItems();
        availableRooms.forEach(r -> view.getCmbRoom().addItem(roomLabel(r)));
    }

    private String roomLabel(BoardingRoom r)
    {
        return r.getRoomNumber() + " - " + r.getRoomType() + " (LKR " + RATE.format(r.getPricePerDay()) + "/day)";
    }

    private void filterPets(int customerId)
    {
        filteredPetList = petList.stream().filter(p -> p.getCustomerId() == customerId).collect(Collectors.toList());
        view.getCmbPet().removeAllItems();
        if (filteredPetList.isEmpty())
        {
            view.getCmbPet().addItem("No pets for this customer");
        }
        else
        {
            filteredPetList.forEach(p -> view.getCmbPet().addItem(p.getPetName()));
        }
    }

    private void refreshAll()
    {
        try
        {
            loadRooms();
            bookings = bookingService.getAllBookings();
            loadTable();
            loadStats();
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    private void loadTable()
    {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Customer", "Pet", "Room", "Check-in", "Check-out", "Days", "Total", "Status"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        bookings.forEach(b -> model.addRow(new Object[]{
            b.getBoardingId(),
            customerNames.getOrDefault(b.getCustomerId(), "Unknown"),
            petNames.getOrDefault(b.getPetId(), "Unknown"),
            roomNames.getOrDefault(b.getRoomId(), "Unknown"),
            b.getCheckIn(),
            b.getCheckOut(),
            b.getNumberOfDays(),
            b.getTotalAmount() == null ? "" : "LKR " + MONEY.format(b.getTotalAmount()),
            b.getStatus()
        }));
        view.setTableModel(model);
    }

    private void loadStats()
    {
        Predicate<BoardingBooking> active = b -> BoardingBookingDAO.isActive(b.getStatus());
        view.setStats(bookings.size(),
                bookings.stream().filter(active).count(),
                allRooms.stream().filter(r -> "Available".equalsIgnoreCase(r.getStatus())).count(),
                allRooms.stream().filter(r -> "Occupied".equalsIgnoreCase(r.getStatus())).count());
    }

    private void handleBook()
    {
        try
        {
            int ci = view.getCmbCustomer().getSelectedIndex();
            int pi = view.getCmbPet().getSelectedIndex();
            int ri = view.getCmbRoom().getSelectedIndex();
            if (ci < 0)
            {
                view.showMessage("Please select a customer.", false);
                return;
            }
            if (filteredPetList.isEmpty() || pi < 0 || pi >= filteredPetList.size())
            {
                view.showMessage("This customer has no registered pets.", false);
                return;
            }
            if (ri < 0 || ri >= availableRooms.size())
            {
                view.showMessage("No available room selected.", false);
                return;
            }
            Date in = Validator.parseDate(view.getTxtCheckIn().getText(), "Check-in");
            Date out = Validator.parseDate(view.getTxtCheckOut().getText(), "Check-out");

            if (!out.after(in))
            {
                throw new ValidationException("Check-out",
                        "Check-out date must be after check-in date.");
            }

            String status = Validator.requireText(
                    String.valueOf(view.getCmbStatus().getSelectedItem()), "Status");
            if (!java.util.Arrays.asList(STATUSES).contains(status))
            {
                throw new ValidationException("Status", "Please select a valid boarding status.");
            }

            String notes = Validator.maxLength(
                    view.getTxtNotes().getText().trim(), 500, "Notes");

            BoardingRoom room = availableRooms.get(ri);

            BoardingBooking b = new BoardingBooking();
            b.setCustomerId(customerList.get(ci).getCustomerId());
            b.setPetId(filteredPetList.get(pi).getPetId());
            b.setRoomId(room.getRoomId());
            b.setCheckIn(in);
            b.setCheckOut(out);
            b.setStatus(status);
            b.setNotes(notes);
            bookingService.bookRoom(b);

            clearForm();
            refreshAll();
            view.showMessage("Room booked: " + b.getNumberOfDays() + " day(s), total LKR "
                    + MONEY.format(b.getTotalAmount()) + ".", true);
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    // Selected booking
    private BoardingBooking selectedBooking()
    {
        int row = view.getTblBoarding().getSelectedRow();
        if (row < 0 || row >= bookings.size())
        {
            info("Select a booking from the table first.");
            return null;
        }
        return bookings.get(row);
    }

    private void handleCheckOut()
    {
        try
        {
            BoardingBooking b = selectedBooking();
            if (b == null)
            {
                return;
            }
            if (confirm("Check out this booking and free the room?"))
            {
                bookingService.checkOut(b.getBoardingId(), b.getRoomId());
                clearForm();
                refreshAll();
                view.showMessage("Checked out. Room is available again.", true);
            }
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    private void handleDelete()
    {
        try
        {
            BoardingBooking b = selectedBooking();
            if (b == null)
            {
                return;
            }
            if (confirm("Delete this booking permanently?"))
            {
                bookingService.deleteBooking(b.getBoardingId());
                clearForm();
                refreshAll();
                view.showMessage("Booking deleted.", true);
            }
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    // Preview total
    private void previewTotal()
    {
        try
        {
            int ri = view.getCmbRoom().getSelectedIndex();
            if (ri < 0 || ri >= availableRooms.size())
            {
                return;
            }
            Date in = Validator.parseDate(view.getTxtCheckIn().getText(), "Check-in");
            Date out = Validator.parseDate(view.getTxtCheckOut().getText(), "Check-out");
            if (!out.after(in))
            {
                view.showMessage("Check-out date must be after check-in date.", false);
                return;
            }
            BigDecimal total = BoardingBookingService.calculateTotal(availableRooms.get(ri), in, out);
            view.showMessage(BoardingBookingService.calculateDays(in, out) + " day(s), total LKR " + MONEY.format(total), true);
        }
        catch (ValidationException ex)
        {
            view.showMessage(ex.getMessage(), false);
        }
    }

    private void clearForm()
    {
        if (!customerList.isEmpty())
        {
            view.getCmbCustomer().setSelectedIndex(0);
            filterPets(customerList.get(0).getCustomerId());
        }
        if (view.getCmbRoom().getItemCount() > 0)
        {
            view.getCmbRoom().setSelectedIndex(0);
        }
        view.getCmbStatus().setSelectedItem("Booked");
        view.getTxtCheckIn().setText("");
        view.getTxtCheckOut().setText("");
        view.getTxtNotes().setText("");
        view.getTblBoarding().clearSelection();
        view.getLblMessage().setText("");
    }

    // Row to form
    private void fillFormFromTable()
    {
        int row = view.getTblBoarding().getSelectedRow();
        if (row < 0 || row >= bookings.size())
        {
            return;
        }
        BoardingBooking b = bookings.get(row);
        for (int i = 0; i < customerList.size(); i++)
        {
            if (customerList.get(i).getCustomerId() == b.getCustomerId())
            {
                view.getCmbCustomer().setSelectedIndex(i);
                filterPets(b.getCustomerId());
                break;
            }
        }
        for (int i = 0; i < filteredPetList.size(); i++)
        {
            if (filteredPetList.get(i).getPetId() == b.getPetId())
            {
                view.getCmbPet().setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < availableRooms.size(); i++)
        {
            if (availableRooms.get(i).getRoomId() == b.getRoomId())
            {
                view.getCmbRoom().setSelectedIndex(i);
                break;
            }
        }
        view.getCmbStatus().setSelectedItem(b.getStatus());
        view.getTxtCheckIn().setText(b.getCheckIn() == null ? "" : b.getCheckIn().toString());
        view.getTxtCheckOut().setText(b.getCheckOut() == null ? "" : b.getCheckOut().toString());
        view.getTxtNotes().setText(b.getNotes() == null ? "" : b.getNotes());
    }
}
