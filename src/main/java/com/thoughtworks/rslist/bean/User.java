package com.thoughtworks.rslist.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @NotNull(message = "invalid user")
    @Size(max = 8, message = "invalid user")
    private String userName;
    @Min(value = 18, message = "invalid user")
    @Max(value = 100, message = "invalid user")
    private int age;
    @NotNull(message = "invalid user")
    private String gender;
    @Email(message = "invalid user")
    private String email;
    @NotNull(message = "invalid user")
    @Pattern(regexp = "1\\d{10}", message = "invalid user")
    private String phone;

    public User(String userName, int age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("user_age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @JsonProperty("user_gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("user_email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("user_phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
