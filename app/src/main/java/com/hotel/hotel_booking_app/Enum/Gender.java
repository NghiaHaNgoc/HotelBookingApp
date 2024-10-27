package com.hotel.hotel_booking_app.Enum;

public enum Gender {
    MALE(1),
    FEMALE(2);

    private final int value;

    Gender(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Gender fromValue(int value) {
        for (Gender gender : Gender.values()) {
            if (gender.value == value) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender value: " + value);
    }
}
