package id.mochgani.latihanrest.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mochgani on 04/12/18.
 */

public class User {
    @SerializedName("id_user")
    private String idUser;
    @SerializedName("nama")
    private String nama;
    @SerializedName("telepon")
    private String telepon;
    @SerializedName("username")
    private String userName;

    public User(){}

    public User(String idUser, String nama, String telepon, String userName) {
        this.idUser = idUser;
        this.nama = nama;
        this.telepon = telepon;
        this.userName = userName;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
