package pl.kurs.spring.hwrkweek3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.spring.hwrkweek3.model.Car;
import pl.kurs.spring.hwrkweek3.service.CarServiceImpl;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "cars",produces = { MediaType.APPLICATION_JSON_VALUE } )
public class CarController {


    private CarServiceImpl carServiceImpl;

    public CarController( CarServiceImpl carServiceImpl) {
        this.carServiceImpl = carServiceImpl;
    }

    @GetMapping()
    ResponseEntity<List<Car>> getCars(){
        List<Car> carList = carServiceImpl.getCars();
        if (!carList.isEmpty()){
            return new ResponseEntity<>(carList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/color/{color}")
    ResponseEntity<List<Car>> getCarsByColor(@PathVariable String color){
        List<Car> carList = carServiceImpl.getCarsByColor(color);
        if (!carList.isEmpty()){
            return new ResponseEntity<>(carList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Car>  getCarById(@PathVariable long id){
        Optional<Car> car = carServiceImpl.getCarById(id);
        if(car.isPresent()) {
            return new ResponseEntity<>(car.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity addCar(@RequestBody Car car){
        boolean add = carServiceImpl.addCar(car);
        if (add){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity((HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @PutMapping
    public ResponseEntity modCar(@RequestBody Car modCar){
        boolean result = carServiceImpl.modCar(modCar);
        if(result) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PatchMapping()
    public ResponseEntity modCarElement(@RequestBody Car modCar){

        boolean result  = carServiceImpl.modCarElement(modCar);
        if(result) {

            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Car>  removeCarById(@PathVariable long id){
        boolean result = carServiceImpl.removeCarById(id);
        if(result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }









}
