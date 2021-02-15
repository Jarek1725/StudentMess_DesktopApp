package org.example;

public class Person {
    String user_id;
    int is_teacher;
    int class_id;
    String nrTel;
    String email;
    String firstName;
    String lastName;

    public Person(String user_id, int is_teacher, int class_id, String nrTel, String email, String firstName, String lastName) {
        this.user_id = user_id;
        this.is_teacher = is_teacher;
        this.class_id = class_id;
        this.nrTel = nrTel;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setIs_teacher(int is_teacher) {
        this.is_teacher = is_teacher;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public void setNrTel(String nrTel) {
        this.nrTel = nrTel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getIs_teacher() {
        return is_teacher;
    }

    public int getClass_id() {
        return class_id;
    }

    public String getNrTel() {
        return nrTel;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "user_id=" + user_id +
                ", is_teacher=" + is_teacher +
                ", class_id=" + class_id +
                ", nrTel='" + nrTel + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
