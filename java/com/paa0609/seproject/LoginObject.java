package com.paa0609.seproject;

import java.io.Serializable;

public class LoginObject implements Serializable {

    private static final long serialVersionUID = -6631779405103025795L;

    private String ID, busNum, phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }
}
