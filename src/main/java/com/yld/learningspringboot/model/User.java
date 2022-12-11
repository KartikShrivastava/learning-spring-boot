package com.yld.learningspringboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

// Model class or Database object of n-tier architecture
// this use r model is a POJO(plain old java object) or a Bean
public class User {
    private final UUID userId;
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final Integer age;
    private final String email;

    // JsonProperty is sort of redundant in latest spring boot version, but its used here
    // just for educational purpose. Note that newer spring mvc handles parameterized constructor automatically
    public User(
            @JsonProperty("userId") UUID userId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("gender") Gender gender,
            @JsonProperty("age") Integer age,
            @JsonProperty("email") String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    // These getter method names are used by Jackson as json key names
    // or we can user JasonProperty annotation to use different names
    @JsonProperty("userId")
    public UUID getUserId() {
        return userId;
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

    // All getter methods(methods starting with get) are converted into json properties
    // by Jackson
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // All getters marked as JsonIgnore are not converted into json properties by Jackson
    @JsonIgnore
    public Integer getYearOfBirth() {
        return LocalDate.now().minusYears(age).getYear();
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

    // Useful in creating a user with already created UUID
    public static User newUser(UUID userUid, User user) {
        return new User(userUid, user.getFirstName(), user.getLastName(),
                user.getGender(), user.getAge(), user.getEmail());
    }

    public enum Gender{
        MALE,
        FEMALE,
        OTHER
    }
}
