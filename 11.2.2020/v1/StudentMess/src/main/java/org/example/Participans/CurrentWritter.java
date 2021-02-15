package org.example.Participans;

import org.example.Person;

public class CurrentWritter {
    private static String name;
    private static String userId;
    private static String lastName;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        CurrentWritter.name = name;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        CurrentWritter.userId = userId;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        CurrentWritter.lastName = lastName;
    }
}
