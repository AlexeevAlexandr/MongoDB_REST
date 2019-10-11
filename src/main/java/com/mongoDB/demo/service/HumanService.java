package com.mongoDB.demo.service;

import com.mongoDB.demo.entity.Human;

import java.util.List;

public interface HumanService {

    List<Human> getAll();

    Human getOne(String id);

    Human create(Human human);

    Human update(Human human);

    void delete(String id);

    boolean exists(String id);
}