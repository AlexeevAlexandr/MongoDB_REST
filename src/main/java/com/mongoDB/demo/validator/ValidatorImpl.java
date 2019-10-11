package com.mongoDB.demo.validator;

import com.mongoDB.demo.entity.Human;
import com.mongoDB.demo.exception.HumanException;
import org.springframework.stereotype.Service;

@Service
public class ValidatorImpl implements Validator{

    @Override
    public void isEmpty(Human human){
        if (human.getName() == null || human.getName().isEmpty()){
            throw new HumanException("Name can not be empty");
        }

        if (human.getAddress().getCity() == null || human.getAddress().getCity().isEmpty()){
            throw new HumanException("City can not be empty");
        }

        if (human.getAddress().getStreet() == null || human.getAddress().getStreet().isEmpty()){
            throw new HumanException("Street can not be empty");
        }
    }
}
