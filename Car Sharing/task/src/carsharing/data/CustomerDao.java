package carsharing.data;

import carsharing.model.Car;
import carsharing.model.Customer;

import java.util.HashMap;
import java.util.List;

public interface CustomerDao {
    public void createTable();
    public void insertCustomer(String nameCustomer);
    public List<Customer> selectCustomer();
    public boolean returnRentedCar(String customerName);

    public void updateRentedCarId(String customerName, String carName);

    public boolean checkIfCustomerAlreadyRentACar(String customerName);
    public List<String> getCustomerRentedCar(String customerName);

    void deleteTable();
}