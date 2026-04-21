package ua.university.domain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Faculty implements Serializable {
    private String code;
    private String name;
    private String shortName;
    private Teacher dean;
    private String contacts;
    private List<Department> departments = new ArrayList<>();

    public Faculty(String code, String name, String shortName,
                   Teacher dean, String contacts) {
        this.code = code;
        this.name = name;
        this.shortName = shortName;
        this.dean = dean;
        this.contacts = contacts;
    }
    public Optional<Teacher> getDean() {
        return Optional.ofNullable(dean);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public String getShortName() {
        return shortName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setDean(Teacher dean) {
        this.dean = dean;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + shortName + "), dean: " +
                (dean != null ? dean.getFullName() : "not assigned");
    }
}