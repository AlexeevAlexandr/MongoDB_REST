package com.mongoDB.demo.service;

import com.mongoDB.demo.entity.Human;
import com.mongoDB.demo.exception.HumanException;
import com.mongoDB.demo.repository.HumanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HumanServiceImpl implements HumanService{

    final private HumanRepository humanRepository;

    public HumanServiceImpl(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    @Override
    public List<Human> getAll() {
        List<Human> humans;
        try {
            log.info("Attempt to read all data");
            humans = humanRepository.findAll();
            log.info("Attempt to read all data - successfully");
            return humans;
        } catch (Exception e){
            log.debug("Can not read all data \n" + e.getMessage());
            return null;
        }
    }

    @Override
    public Human getOne(String id) {
        log.info("Attempt to find an entity by id");
        if (humanRepository.exists(id)) {
            try {
                Human human = humanRepository.findOne(id);
                log.info("Attempt to find an entity by id - successfully");
                return human;
            } catch (Exception e) {
                log.debug("Can not read data \n" + e.getMessage());
                return null;
            }
        } else {
            log.info("Entity by id not found");
            throw new HumanException(String.format("Entity with id '%s'not found", id));
        }
    }

    @Override
    public Human create(Human human) {
        try {
            log.info("Attempt to create an entity");
            human = humanRepository.save(human);
            log.info("Attempt to create an entity - successfully");
            return human;
        } catch (Exception e) {
            log.debug("Can not create an entity \n" + e.getMessage());
            throw new HumanException("Can not create entity");
        }
    }

    @Override
    public Human update(Human human) {
        log.info("Attempt to update an entity");
        String id = human.getId();
        if (humanRepository.exists(id)) {
            try {
                human = humanRepository.save(human);
                log.info("Attempt to update an entity - successfully");
            } catch (Exception e) {
                log.debug("Can not update an entity \n" + e.getMessage());
                return null;
            }
            return human;
        } else {
            log.info("Can not update an entity, id not found");
            throw new HumanException(String.format("Can not update, entity with id '%s' not found", id));
        }
    }

    @Override
    public void delete(String id) {
        log.info("Attempt to delete an entity by id");
        if (humanRepository.exists(id)) {
            try {
            humanRepository.delete(id);
            log.info("Attempt to delete an entity by id - successfully");
            } catch (Exception e) {
                log.debug("Can not delete an entity \n" + e.getMessage());
            }
        } else {
            log.info("Entity not found");
            throw new HumanException(String.format("Entity with id '%s' not found", id));
        }
    }

    @Override
    public boolean exists(String id) {
        try {
            return humanRepository.exists(id);
        } catch (Exception e) {
            log.debug("Can not check an entity if exist\n" + e.getMessage());
            return false;
        }
    }
}