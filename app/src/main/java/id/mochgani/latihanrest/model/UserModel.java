package id.mochgani.latihanrest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import id.mochgani.latihanrest.entity.User;

public class UserModel {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<User> listUser;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getListDataUser() {
        return listUser;
    }

    public void setListDataKontak(List<User> listUser) {
        this.listUser = listUser;
    }
}
