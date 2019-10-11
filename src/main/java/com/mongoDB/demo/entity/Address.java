package com.mongoDB.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String city;
    private String street;

    @Override
    public String toString(){
        return "{" +
                    "\"City\":\"" + city + "\"," +
                    "\"Street\":\"" + street + "\"" +
                "}";
    }
}