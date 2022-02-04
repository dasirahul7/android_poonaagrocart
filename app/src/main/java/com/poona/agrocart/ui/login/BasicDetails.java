package com.poona.agrocart.ui.login;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class BasicDetails
{
    String mobileNumber;
    String otp;
    String userName;
    String emailId;
    String userId;
    String city;
    String area;
    String password;
    String countryCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isValidMobileNumber(){
        if(TextUtils.isEmpty(this.mobileNumber)) {
            return 0;
        }
        else if(!TextUtils.isEmpty(this.mobileNumber) && this.mobileNumber.length()<10){
            return 1;
        }
        else{
            return -1;
        }
    }

    public int isValidOtp(){
        if(TextUtils.isEmpty(this.otp)) {
            return 0;
        }
        else if(!TextUtils.isEmpty(this.otp) && this.otp.length() < 4) {
            return 1;
        } else {
            return -1;
        }
    }

    public int isValidUserName(){
        if(TextUtils.isEmpty(this.userName)){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int isValidEmailId() {
        if(TextUtils.isEmpty(this.emailId)){
            return 0;
        }
        else if(!TextUtils.isEmpty(this.emailId) && !isValidEmailId(this.emailId)) {
            return 1;
        } else {
            return -1;
        }
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public int isValidCity()
    {
        if(TextUtils.isEmpty(this.city)){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int isValidArea(){
        if(TextUtils.isEmpty(this.area)){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int isValidCountryCode(){
        if(TextUtils.isEmpty(this.countryCode)){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int isValidPassword(){
        if(TextUtils.isEmpty(this.password)){
            return 0;
        }
        else{
            return -1;
        }
    }
}
