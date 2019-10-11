package com.mongoDB.demo.repository;

import com.mongoDB.demo.entity.Human;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanRepository extends MongoRepository<Human, String> {
}