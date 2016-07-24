package com.zzheads.analizePD.model;

import javax.persistence.*;

@Entity
public class Country {
        //    code: VARCHAR(3) - this is the primary key, a String with a maximum length of 3 characters
        //    name: VARCHAR(32) - a String with a maximum length of 32 characters
        //    internetUsers: DECIMAL(11,8) - A number with a maximum length of 11 digits and 8 digits of decimal precision
        //    adultLiteracyRate: DECIMAL(11,8) - A number with a maximum length of 11 digits and 8 digits of decimal precision
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;
    @Column
    private String name;
    @Column
    private String internetUsers;
    @Column
    private String adultLiteracyRate;

    public Country () {}

    public Country (CountryBuilder builder) {
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(String internetUsers) {
        this.internetUsers = internetUsers;
    }

    public String getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(String adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }

    public static class CountryBuilder {
        private String code;
        private String name;
        private String internetUsers;
        private String adultLiteracyRate;

        public CountryBuilder (String name) {
            this.name = name;
        }

        public CountryBuilder withInternetUsers (String internetUsers) {
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate (String adultLiteracyRate) {
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build () {
            return new Country(this);
        }
    }
}
