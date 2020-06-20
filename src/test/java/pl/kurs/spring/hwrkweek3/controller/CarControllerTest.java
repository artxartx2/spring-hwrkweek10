package pl.kurs.spring.hwrkweek3.controller;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.spring.hwrkweek3.model.Car;
import pl.kurs.spring.hwrkweek3.service.CarService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CarService carService;

    private List<Car> carList = new ArrayList<>();

    @BeforeEach
    void prepareCarListForTests() {
        carList.clear();
        carList.add(new Car(1L, "Audi", "A6", "Black"));
        carList.add(new Car(2L, "Volkswagen", "Polo", "Red"));
        carList.add(new Car(3L, "Toyota", "Avensis", "White"));
        carList.add(new Car(4L, "Toyota", "Camry", "Black"));
        carList.add(new Car(5L, "Ford", "Focus", "Black"));

        ReflectionTestUtils.setField(carService, "carList", carList);
    }

    @Test
    void should_getCars() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(carList.size())));
    }

    @Test
    void shold_getCarsByColor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/color/{color}", "Black"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
    }

    @Test
    void shold_not_getCarsByColor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/color/{color}", "Unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_getCarById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark", Is.is("Toyota")));
    }

    @Test
    void should_not_getCarById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_addCar() throws Exception {
        int sizeOfCarList = carList.size();
        mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": \"6\"," +
                        "\"mark\" : \"Suzuki\"," +
                        "\"model\" : \"Swift\"," +
                        "\"color\" : \"Silver\"" +
                        "}"))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(sizeOfCarList + 1)));
    }

    @Test
    void should_modCar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": \"5\"," +
                        "\"mark\" : \"Opel\"," +
                        "\"model\" : \"Astra\"," +
                        "\"color\" : \"Orange\"" +
                        "}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark", Is.is("Opel")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Is.is("Astra")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Is.is("Orange")));
    }

    @Test
    void schould_modCarElement() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": \"5\"," +
                        "\"color\" : \"Orange\"" +
                        "}"))
                .andExpect(status().isOk());

        //Test if only color value was changed , another field should be unchanged
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark", Is.is("Ford")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Is.is("Focus")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Is.is("Orange")));
    }

    @Test
    void should_removeCarById() throws Exception {
        int sizeOfCarList = carList.size();
        mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", 1))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(sizeOfCarList - 1)));
    }

    @Test
    void should_not_removeCarById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", 10))
                .andExpect(status().isNotFound());

        assertFalse(carService.getCarById(10).isPresent());
    }
}
