package com.mongoDB.demo;

import com.mongoDB.demo.entity.Address;
import com.mongoDB.demo.entity.Human;
import com.mongoDB.demo.service.HumanService;
import com.mongoDB.demo.testHelper.TestHelper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Main.class)
@SpringBootTest
public class HumanControllerTest {
    @Autowired
    private TestHelper testHelper;
    @Autowired
    private HumanService humanService;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public final void setup() {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void create() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");

        String id =
                given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                        when().post("/human").
                        then().statusCode(SC_OK).extract().path("id");

        humanService.delete(id);
    }

    @Test
    public void create_With_Broken_Data() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/brokenEntity.json");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/human").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create_With_Name_Null() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        jsonObject.put("name", null);

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/human").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create_With_City_Null() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        JSONObject jsonObjectCity = (JSONObject) jsonObject.get("address");
        jsonObjectCity.put("city", null);

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/human").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void create_With_Street_Null() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");
        JSONObject jsonObjectCity = (JSONObject) jsonObject.get("address");
        jsonObjectCity.put("street", null);

        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(jsonObject.toString()).
                when().post("/human").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    public void getAll() {
        when().get("/human").
                then().statusCode(SC_OK);
    }

    @Test
    public void getById() {
        // create
        Human human = humanService.create(new Human("Tom", new Address("City", "Street")));
        String id = human.getId();

        try {
            // get by id
            when().get("/human/" + id).
            then().statusCode(SC_OK).body("id", equalTo(id));
        } finally {
            // delete
            if (humanService.exists(id)) {
                humanService.delete(id);
            }
        }
    }

    @Test
    public void getById_NotFound() {
        when().get("/human/0123456789").
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    public void getBy_Null_Id() {
        when().get("/human/" + nullValue()).
                then().statusCode(SC_NOT_FOUND);
    }

    @Test
    public void update() {
        // create
        Human human = humanService.create(new Human("Tom", new Address("City", "Street")));
        String id = human.getId();

        try {
            // change
            Human changedHuman = humanService.getOne(id);
            Address address = new Address("New City", "New Street");
            changedHuman.setAddress(address);

            // update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(changedHuman).
                    when().
                    put("/human").
                    then().statusCode(SC_OK);

            // check changed
            Human checkHuman = humanService.getOne(id);
            Assert.assertEquals(changedHuman.toString(), checkHuman.toString());
        } finally {
            // delete
            if (humanService.exists(id)) {
                humanService.delete(id);
            }
        }
    }

    @Test
    public void update_With_Null_Id() throws Exception {
        JSONObject jsonObject = testHelper.getJsonObjectFromFile("json/entity.json");

        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                body(jsonObject.toString()).
                when().
                put("/human").
                then().
                statusCode(SC_NOT_FOUND);
    }

    @Test
    public void update_With_Name_Null() {
        // create
        Human human = humanService.create(new Human("Tom", new Address("City", "Street")));

        try {
            // change
            human.setName(null);

            //update
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(human).
                    when().
                    put("/human").
                    then().statusCode(SC_NOT_FOUND);
        } finally {
            // delete
            String id = human.getId();
            if (humanService.exists(id)) {
                humanService.delete(id);
            }
        }
    }

    @Test
    public void update_With_City_Null() {
        // create
        Human human = humanService.create(new Human("Tom", new Address("City", "Street")));

        try {
            // change
            Address address = human.getAddress();
            address.setCity(null);
            human.setAddress(address);

            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    body(human).
                    when().
                    put("/human").
                    then().statusCode(SC_NOT_FOUND);
        } finally {
            // delete
            String id = human.getId();
            if (humanService.exists(id)) {
                humanService.delete(id);
            }
        }
    }

    @Test
    public void update_With_Street_Null() {
        // create
        Human human = humanService.create(new Human("Tom", new Address("City", "Street")));

        try {
            // change
            Address address = human.getAddress();
            address.setStreet(null);
            human.setAddress(address);

            given().contentType(MediaType.APPLICATION_JSON_VALUE).body(human).
                    when().put("/human").
                    then().statusCode(SC_NOT_FOUND);

        } finally {
            // delete
            String id = human.getId();
            if (humanService.exists(id)) {
                humanService.delete(id);
            }
        }
    }

    @Test
    public void delete() {
        // create
        Human human = humanService.create(new Human("Tom", new Address("City", "Street")));
        String id = human.getId();

        try {
            //delete
            given().contentType(MediaType.APPLICATION_JSON_VALUE).
                    when().
                    delete("/human/{id}",id).
                    then().statusCode(SC_OK);
        } finally {
            if (humanService.exists(id)) {
                humanService.delete(id);
            }
        }
    }

    @Test
    public void delete_Id_NotFound() {
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/human/wrongId").
                then().
                statusCode(SC_NOT_FOUND);
    }

    @Test
    public void delete_With_Null_Id() {
        given().contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                delete("/human/" + nullValue()).
                then().
                statusCode(SC_NOT_FOUND);
    }
}
