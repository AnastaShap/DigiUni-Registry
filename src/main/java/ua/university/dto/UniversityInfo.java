package ua.university.dto;

///  зберігання глобальних констант про університет.
public record UniversityInfo(
        String fullName,
        String shortName,
        String city,
        String address) {}
// Приклад: new UniversityInfo("Національний університет «Києво-Могилянська академія»", "НаУКМА", "Київ", "вул. Сковороди, 2");