package com.hotel.hotel_booking_app.model;

public class User {
//    public Integer id;
    public String firstname;
    public String surname;
    public String email;
    public String phone;
    public String linkAvatar;
    public String birthDay;
    public Integer gender;
    public Integer position;
    public Integer salary;
    public String city;
    public String district;
    public String ward;
    public String address;

    public User() {}

    public User(User otherUser) {
        if (otherUser != null) {
//            this.id = otherUser.id;
            this.firstname = otherUser.firstname;
            this.surname = otherUser.surname;
            this.email = otherUser.email;
            this.phone = otherUser.phone;
            this.linkAvatar = otherUser.linkAvatar;
            this.birthDay = otherUser.birthDay;
            this.gender = otherUser.gender;
            this.position = otherUser.position;
            this.salary = otherUser.salary;
            this.city = otherUser.city;
            this.district = otherUser.district;
            this.ward = otherUser.ward;
            this.address = otherUser.address;
        }
    }


    public User(String firstname, String surname, String email, String phone, String linkAvatar,
                String birthDay, Integer gender, Integer position, Integer salary,
                String city, String district, String ward, String address) {
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.linkAvatar = linkAvatar;
        this.birthDay = birthDay;
        this.gender = gender;
        this.position = position;
        this.salary = salary;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.address = address;
    }

    // Getters và Setters
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class SignInInput {
        public String email;
        public String password;

        public SignInInput(String email, String password) {
            this.email = email != null ? email : "";
            this.password = password != null ? password : "";
        }
    }
    public static class SignInOutput {
        public String firstname;
        public String surname;
        public String linkAvatar;
        public Integer position;
        public String token;

    }
    public static class SignUpInput {
        public String firstname;
        public String surname;
        public String email;
        public String password;

        public SignUpInput(String firstname, String surname,String email, String password) {
            this.firstname = firstname;
            this.surname = surname;
            this.email = email;
            this.password = password;
        }
    }

    public static class FormChangePassword {
        public String oldPassword;
        public String newPassword;

        public FormChangePassword(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }
    }
}
