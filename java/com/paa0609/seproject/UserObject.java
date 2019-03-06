package com.paa0609.seproject;

import java.io.Serializable;

public class UserObject implements Serializable {

    private static final long serialVersionUID = -66317794051030257L;

    private String ID;
    private String Name;
    private String phoneNum;
    private String BusNum;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBusNum() {
        return BusNum;
    }

    public void setBusNum(String busNum) {
        BusNum = busNum;
    }
}
