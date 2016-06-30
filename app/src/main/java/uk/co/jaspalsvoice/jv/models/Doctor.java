package uk.co.jaspalsvoice.jv.models;

import android.content.ContentValues;

import uk.co.jaspalsvoice.jv.db.DbOpenHelper;

/**
 * Created by Ana on 3/21/2016.
 */
public class Doctor {

    private String uuid;
    private String id;
    private String type;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhone;

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(DbOpenHelper.COLUMN_MT_UUID, getUuid());
        cv.put(DbOpenHelper.COLUMN_MT_ID, getId());
        cv.put(DbOpenHelper.COLUMN_MT_DOCTOR_TYPE, getType());
        cv.put(DbOpenHelper.COLUMN_MT_NAME, getName());
        cv.put(DbOpenHelper.COLUMN_MT_HOSPITAL_NAME, getHospitalName());
        cv.put(DbOpenHelper.COLUMN_MT_HOSPITAL_ADDRESS, getHospitalAddress());
        cv.put(DbOpenHelper.COLUMN_MT_HOSPITAL_PHONE, getHospitalPhone());
        cv.put(DbOpenHelper.COLUMN_MT_ADDRESS, getAddress());
        cv.put(DbOpenHelper.COLUMN_MT_PHONE, getPhone());
        cv.put(DbOpenHelper.COLUMN_MT_EMAIL, getEmail());
        return cv;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  /*  public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
*/
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getHospitalPhone() {
        return hospitalPhone;
    }

    public void setHospitalPhone(String hospitalPhone) {
        this.hospitalPhone = hospitalPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
