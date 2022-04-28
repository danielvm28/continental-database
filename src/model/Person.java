package model;

import java.time.LocalDate;

public class Person implements Comparable<Person>{
    // Attributes
    private int code;
    private String fullName;
    private String gender;
    private LocalDate birthDate;
    private double height;
    private String nationality;
    // Photo missing

    public Person(String fullName, String gender, LocalDate birthDate, double height, String nationality, int code) {
        this.fullName = fullName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.height = height;
        this.nationality = nationality;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    // Compares according to the full name and code
    @Override
    public int compareTo(Person B) {
        Person A = this;

        int fullNameOutput = 0;
        int codeOutput = 0;

        fullNameOutput = A.fullName.compareTo(B.fullName);

        if (fullNameOutput == 0) {
            codeOutput = A.code - B.code;

            return codeOutput;
        } else {
            return fullNameOutput;
        }
    }

    public String toString() {
        return fullName + " " + code;
    }
}
