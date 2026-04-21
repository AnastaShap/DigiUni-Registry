package ua.university.dto;

import java.io.Serializable;

public record ContactInfo(Email email, PhoneNumber phoneNumber) implements Serializable {}
