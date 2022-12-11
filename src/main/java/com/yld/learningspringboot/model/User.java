package com.yld.learningspringboot.model;

import java.util.UUID;

// Model class or Database object of n-tier architecture
// this user model is a POJO(plain old java object) or a Bean
public class User {
    private UUID userId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Integer age;
    private String email;

    // TODO: Handle requests in which client tries to create user with all NULL fields
    public User(){
    }

    public User(UUID userId, String firstName, String lastName, Gender gender, Integer age, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }

    public enum Gender{
        MALE,
        FEMALE,
        OTHER
    }
}
