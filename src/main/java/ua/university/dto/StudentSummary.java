package ua.university.dto;
import java.io.Serializable;

// StudentSummary - DTO for print(output)
public record StudentSummary(
        String fullName,
        int course,
        String group,
        int age
) implements Serializable {}