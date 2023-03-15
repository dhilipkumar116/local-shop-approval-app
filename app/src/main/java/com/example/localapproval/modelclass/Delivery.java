package com.example.localapproval.modelclass;

public class Delivery {
    private String username;
    private String password;

    public Delivery(String selfie) {
        this.selfie = selfie;
    }

    private String phno;

    public String getSelfie() {
        return selfie;
    }

    public void setSelfie(String selfie) {
        this.selfie = selfie;
    }

    private String fullname;
    private String selfie;

    public Delivery(String vehicleNum, String vehicleType) {
        this.vehicleNum = vehicleNum;
        this.vehicleType = vehicleType;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    private String vehicleNum,vehicleType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public Delivery(String username, String password, String phno, String fullname, String address, String approval) {
        this.username = username;
        this.password = password;
        this.phno = phno;
        this.fullname = fullname;
        this.address = address;
        this.approval = approval;
    }

    private String address;
    private String approval;

    public Delivery(){

    }
}
