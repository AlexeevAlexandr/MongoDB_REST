package com.mongoDB.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "human")
public class Human {
    @Id
    private String id;
    private String name;
    private Address address;

    public Human(String name, Address address){
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString(){
        return "{" +
                    "\"id\":\"" + id + "\"," +
                    "\"name\":\"" + name + "\"," +
                    "\"address\":" + address +
                "}";
    }
}