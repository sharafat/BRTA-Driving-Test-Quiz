package net.incredibles.brtaquiz.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author sharafat
 * @Created 2/15/12 7:18 PM
 */
public class User {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "reg_no", canBeNull = false, indexName = "index_regNo_pinNo")
    private String regNo;

    @DatabaseField(columnName = "pin_no", canBeNull = false, indexName = "index_regNo_pinNo")
    private String pinNo;

    public User() {
    }

    public User(String regNo, String pinNo) {
        this.regNo = regNo;
        this.pinNo = pinNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getPinNo() {
        return pinNo;
    }

    public void setRegNoAndPinNo(String regNo, String pinNo) {
        this.regNo = regNo;
        this.pinNo = pinNo;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", regNo='" + regNo + '\'' +
                ", pinNo='" + pinNo + '\'' +
                '}';
    }
}
