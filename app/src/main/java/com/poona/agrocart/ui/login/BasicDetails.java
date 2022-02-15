package com.poona.agrocart.ui.login;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class BasicDetails {
    String mobileNumber;
    String otp;
    String userName;
    String emailId;
    String userId;
    String id;
    String city;
    String area;
    String pinCode;
    String password;
    String countryCode;

    String apartmentName;
    String houseNumber;
    String landmark;
    String street;
    String latitude;
    String longitude;
    String mapAddress;

    String name;
    String addressType;
    String alternateMobileNumber;
    String state;
    String gender;
    String dob;

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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(String mapAddress) {
        this.mapAddress = mapAddress;
    }

    public String getAlternateMobileNumber() {
        return alternateMobileNumber;
    }

    public void setAlternateMobileNumber(String alternateMobileNumber) {
        this.alternateMobileNumber = alternateMobileNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int isValidMobileNumber(){
        if(TextUtils.isEmpty(this.mobileNumber)) {
            return 0;
        } else if(!TextUtils.isEmpty(this.mobileNumber) && this.mobileNumber.length()<10){
            return 1;
        } else { return -1; }
    }

    public int isValidAlternateMobileNumber(){
        /*if(TextUtils.isEmpty(this.alternateMobileNumber)) {
            return 0;
        } else*/ if(!TextUtils.isEmpty(this.alternateMobileNumber) && this.alternateMobileNumber.length()<10) {
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidOtp(){
        if(TextUtils.isEmpty(this.otp)) {
            return 0;
        } else if(!TextUtils.isEmpty(this.otp) && this.otp.length() < 4) {
            return 1;
        } else {
            return -1;
        }
    }

    public int isValidUserName(){
        if(TextUtils.isEmpty(this.userName)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidEmailId() {
        if(TextUtils.isEmpty(this.emailId)){
            return 0;
        } else if(!TextUtils.isEmpty(this.emailId) && !isValidEmailId(this.emailId)) {
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

    public int isValidCity() {
        if(TextUtils.isEmpty(this.city) || this.city.equals("0")){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidArea(){
        if(TextUtils.isEmpty(this.area) || this.area.equals("0")){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidPinCode(){
        if(TextUtils.isEmpty(this.pinCode)){
            return 0;
        } else if(!TextUtils.isEmpty(this.pinCode) && this.pinCode.length() < 6) {
            return 1;
        } else {
            return -1;
        }
    }

    public int isValidCountryCode(){
        if(TextUtils.isEmpty(this.countryCode)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidPassword(){
        if(TextUtils.isEmpty(this.password)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidName(){
        if(TextUtils.isEmpty(this.name)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidAddressType(){
        if(TextUtils.isEmpty(this.addressType)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidApartmentName(){
        if(TextUtils.isEmpty(this.apartmentName)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidHouseNumber(){
        if(TextUtils.isEmpty(this.houseNumber)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidLandmark(){
        if(TextUtils.isEmpty(this.landmark)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidStreet(){
        if(TextUtils.isEmpty(this.street)){
            return 0;
        } else {
            return -1;
        }
    }

    public int isValidState(){
        if(TextUtils.isEmpty(this.state) || this.state.equals("0")){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int isValidGender(){
        if(TextUtils.isEmpty(this.gender)){
            return 0;
        }
        else{
            return -1;
        }
    }

    public int isValidDob(){
        if(TextUtils.isEmpty(this.dob)){
            return 0;
        }
        else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}