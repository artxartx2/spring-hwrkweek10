package pl.kurs.spring.hwrkweek3.service;

import org.springframework.stereotype.Service;
import pl.kurs.spring.hwrkweek3.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    private List<Car> carList;

    public CarServiceImpl() {
        this.carList = new ArrayList<>();
//        carList.add(new Car(1L, "Toyota", "Avensis", "White"));
//        carList.add(new Car(2L, "Toyota", "Camry", "Black"));
//        carList.add(new Car(3L, "Ford", "Focus", "Black"));
    }

    @Override
    public List<Car> getCars() {
        return carList;
    }

    @Override
    public List<Car> getCarsByColor(String color) {
        return carList.stream().filter(Car -> Car.getColor().equalsIgnoreCase(color)).collect(Collectors.toList());
    }

    @Override
    public Optional<Car> getCarById(long id) {
        Optional<Car> car = carList.stream().filter(Car -> Car.getId() == id).findFirst();
        return car;
    }

    @Override
    public boolean addCar(Car newCar) {
        return carList.add(newCar);
    }

    @Override
    public boolean modCar(Car modCar) {
        Optional<Car> first = carList.stream().filter(Car -> Car.getId() == modCar.getId()).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            carList.add(modCar);
            return carList.add(modCar);
        }
        return false;
    }

    @Override
    public boolean modCarElement(Car modCar) {
        Optional<Car> car = carList.stream().filter(Car -> Car.getId() == modCar.getId()).findFirst();
        if (car.isPresent()) {
            if (modCar.getMark() != null) {
                car.get().setMark(modCar.getMark());
            }
            if (modCar.getModel() != null) {
                car.get().setModel(modCar.getModel());
            }
            if (modCar.getColor() != null) {
                car.get().setColor(modCar.getColor());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCarById(long id) {
        Optional<Car> car = carList.stream().filter(Car -> Car.getId() == id).findFirst();
        if (car.isPresent()) {
            carList.remove(car.get());
            return true;
        }
        return false;
    }


}
