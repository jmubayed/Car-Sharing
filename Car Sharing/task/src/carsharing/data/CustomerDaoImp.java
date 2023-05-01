package carsharing.data;

import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerDaoImp implements CustomerDao{

    private Database databaseCarsharing;
    private Connection connection;

    public CustomerDaoImp(Database databaseCarsharing) {
        this.databaseCarsharing = databaseCarsharing;
        this.connection = databaseCarsharing.getConnection();
    }

    @Override
    public void createTable() {
            try {
                Statement statement = connection.createStatement();
                String sql =  "CREATE TABLE IF NOT EXISTS CUSTOMER (\n" +
                        "  ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                        "  NAME VARCHAR(255) UNIQUE NOT NULL,\n" +
                        "  RENTED_CAR_ID INT,\n" +
                        "  FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)\n" +
                        ")";
                statement.executeUpdate(sql);
                statement.close();
            } catch (SQLException se) {
               se.printStackTrace();
            }

    }

    @Override
    public void insertCustomer(String nameCustomer) {
        try {
            Statement statement = connection.createStatement();
            String sql =  "INSERT INTO CUSTOMER (name) VALUES ('" + nameCustomer + "')";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
           se.printStackTrace();
        }
    }

    @Override
    public List<Customer> selectCustomer() {
        List<Customer> customerList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql =  "SELECT * FROM CUSTOMER";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int rented_car_id = resultSet.getInt("rented_car_id");
                customerList.add(new Customer(id, name, rented_car_id));
            }
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return customerList;
    }

    @Override
    public boolean returnRentedCar(String customerName) {
        boolean result = false;
        int rented_car_id = 0;
        try {
            Statement statement = connection.createStatement();
            String sql =  "SELECT * FROM CUSTOMER WHERE NAME = '" + customerName + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                rented_car_id = resultSet.getInt("rented_car_id");
            }
            if(rented_car_id != 0){
                String sql2 =  "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE NAME = '" + customerName + "'";
                statement.executeUpdate(sql2);
                result = true;
            }
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }

    @Override
    public void updateRentedCarId(String customerName, String carName) {
        try {
            Statement statement = connection.createStatement();
            String sql =  "UPDATE CUSTOMER \n" +
                    "SET RENTED_CAR_ID = (SELECT CAR.ID FROM CAR \n" +
                    "                     INNER JOIN COMPANY ON CAR.COMPANY_ID = COMPANY.ID \n" +
                    "                     WHERE CAR.NAME = '" + carName + "'" + ") WHERE NAME = '" + customerName + "'";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    @Override
    public boolean checkIfCustomerAlreadyRentACar(String customerName) {
        int rented_car_id = 0;
        try {
            Statement statement = connection.createStatement();
            String sql =  "SELECT * FROM CUSTOMER WHERE NAME = '" + customerName + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                rented_car_id = resultSet.getInt("rented_car_id");
            }
            statement.close();
        } catch (SQLException se) {
           se.printStackTrace();
        }
        return rented_car_id != 0;
    }

    @Override
    public List<String> getCustomerRentedCar(String customerName) {
        List<String> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql =  "SELECT CAR.NAME AS CAR_NAME, COMPANY.NAME AS COMPANY_NAME\n" +
                    "FROM CUSTOMER\n" +
                    "LEFT JOIN CAR ON CUSTOMER.RENTED_CAR_ID = CAR.ID\n" +
                    "LEFT JOIN COMPANY ON CAR.COMPANY_ID = COMPANY.ID\n" +
                    "WHERE CUSTOMER.NAME = '" + customerName + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                String carName = resultSet.getString("CAR_NAME");
                String companyName = resultSet.getString("COMPANY_NAME");
                if(carName != null && companyName != null){
                    result.add(carName);
                    result.add(companyName);
                }
            }
            statement.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return result;
    }

    @Override
    public void deleteTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "DROP TABLE CUSTOMER";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
               se.printStackTrace();
        }
    }
}
