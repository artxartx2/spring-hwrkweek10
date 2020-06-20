package pl.kurs.spring.hwrkweek3.service;

import pl.kurs.spring.hwrkweek3.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {

    List<Car> getCars();
    List<Car> getCarsByColor(String color);
    Optional<Car> getCarById(long id);
    boolean addCar(Car newCar);
    boolean modCar(Car modCar);
    boolean modCarElement(Car modCar);
    boolean removeCarById(long id);

}
