package ua.university.domain;

// StudentSummary - DTO for print(output)
public record StudentSummary(
        String fullName,
        int course,
        String group,
        int age
) {}