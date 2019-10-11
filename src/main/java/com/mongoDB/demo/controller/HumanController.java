package com.mongoDB.demo.controller;

import com.mongoDB.demo.entity.Human;
import com.mongoDB.demo.exception.HumanException;
import com.mongoDB.demo.service.HumanService;
import com.mongoDB.demo.validator.Validator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/human")
public class HumanController {

    final private HumanService humanService;
    final private Validator validator;

    public HumanController(HumanService humanService, Validator validator) {
        this.humanService = humanService;
        this.validator = validator;
    }

    @GetMapping (value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Human> getAll(){
        return humanService.getAll();
    }

    @GetMapping (value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Human getOne(@PathVariable String id){
        if (id == null){
            throw new HumanException("Id can not be null");
        }
        return humanService.getOne(id);
    }

    @PostMapping (value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Human create(@RequestBody Human human){
        validator.isEmpty(human);
        return humanService.create(human);
    }

    @PutMapping (value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Human update(@RequestBody Human human){
        if (human.getId() == null){
            throw new HumanException("Id can not be null");
        }
        validator.isEmpty(human);
        return humanService.update(human);
    }

    @DeleteMapping  (value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public void delete(@PathVariable String id){
        if (id == null){
            throw new HumanException("Id can not be null");
        }
        humanService.delete(id);
    }
}
