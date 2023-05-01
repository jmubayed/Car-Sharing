package carsharing.data;

import carsharing.model.Car;

import java.util.List;

public interface CarDao {
    public void createTable();
    public void insertCar(String nameCar, int companyID);
    public List<Car> selectCar(String companyName);

    void deleteTable();
}