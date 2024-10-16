package com.hotel.hotel_booking_app.model;

public class User {
//    public Integer id;
    public String firstname;
    public String surname;
    public String linkAvatar;
    public String birthDay;

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
}
