package com.zzheads.analizePD.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Country {
        //    code: VARCHAR(3) - this is the primary key, a String with a maximum length of 3 characters
        //    name: VARCHAR(32) - a String with a maximum length of 32 characters
        //    internetUsers: DECIMAL(11,8) - A number with a maximum length of 11 digits and 8 digits of decimal precision
        //    adultLiteracyRate: DECIMAL(11,8) - A number with a maximum length of 11 digits and 8 digits of decimal precision
    @Id
    private String code;
    @Column
    private String name;
    @Column
    private BigDecimal internetUsers;
    @Column
    private BigDecimal adultLiteracyRate;

    public Country () {}

    public Country (CountryBuilder builder) {
        //this.code = builder.code;
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

    public BigDecimal getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(BigDecimal internetUsers) {
        this.internetUsers = internetUsers;
    }

    public BigDecimal getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(BigDecimal adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    @Override
    public String toString() {
        String div = " | ";
        String percent = "%";
        String codeStr = String.format("%3s", code);
        String nameStr = String.format("%-32s", name);
        String internetUsersStr = "           -  ";
        String adultLiteracyRateStr = "     -  ";
        if (internetUsers!=null) internetUsersStr = String.format("%13.2f", internetUsers)+percent;
        if (adultLiteracyRate!=null) adultLiteracyRateStr = String.format("%7.2f", adultLiteracyRate)+percent;

        return div+codeStr+div+nameStr+div+internetUsersStr+div+adultLiteracyRateStr+div;
    }

    public static class CountryBuilder {
        private String code;
        private String name;
        private BigDecimal internetUsers;
        private BigDecimal adultLiteracyRate;

        public CountryBuilder (String name) {
            this.name = name;
            code = name.substring(0,3).toUpperCase();
        }

        public CountryBuilder withInternetUsers (BigDecimal internetUsers) {
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate (BigDecimal adultLiteracyRate) {
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build () {
            return new Country(this);
        }
    }
}
