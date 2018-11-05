package com.example.endzhe.rertofit.retrofit;


import com.google.gson.annotations.SerializedName;
//SerializedName позволяет указать имя поля в JSON,
// из которого будет десериализоваться поле в Java-объекте.

//описываем класс эквивалентный JSON объекту
public class RankModel {

    @SerializedName("dob")
    private String dob;

    @SerializedName("country")
    private String country;
    @SerializedName("date")
    private String date;

    @SerializedName("rank")
    private Integer rank;

    @SerializedName("sex")
    private String sex;




    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getRank() {
        return rank;
    }



    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "RankModel{" +
                "rank=" + rank +
                '}';
    }
}