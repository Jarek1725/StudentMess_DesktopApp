package org.example.Participans;

public class LastPoeple {
    private final String firstName;
    private final String lastName;
    private final String userId;
    private final String lastMsg;

    public LastPoeple(String firstName, String lastName, String userId, String lastMsg) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.lastMsg = lastMsg;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getLastMsg() {
        return lastMsg;
    }
}
