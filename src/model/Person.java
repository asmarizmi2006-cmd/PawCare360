package model;

// Base person
public abstract class Person
{
    private String fullName;
    private String phone;
    private String email;
    private String status;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Subclass label
    public abstract String getPersonType();

    // Active check
    public boolean isActive()
    {
        return "Active".equalsIgnoreCase(status);
    }

    @Override
    public String toString()
    {
        return getPersonType() + ": " + fullName;
    }
}
