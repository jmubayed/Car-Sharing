package carsharing.data;

import carsharing.model.Car;
import carsharing.model.Company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImp implements CarDao {

    private Database databaseCarsharing;
    private Connection connection;

    public CarDaoImp(Database databaseCarsharing) {
        this.databaseCarsharing = databaseCarsharing;
        this.connection = databaseCarsharing.getConnection();
    }

    @Override
    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS CAR (\n" +
                    "  ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                    "  NAME VARCHAR(255) UNIQUE NOT NULL,\n" +
                    "  COMPANY_ID INT NOT NULL,\n" +
                    "  FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)\n" +
                    ")";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
        //    se.printStackTrace();
        }
    }

    @Override
    public void insertCar(String nameCar, int companyID) {
        try {
            Statement statement = connection.createStatement();
            String sql =  "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + nameCar + "'," + companyID + ")";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
      //      se.printStackTrace();
        }
    }

    @Override
    public List<Car> selectCar(String companyName) {
        List<Car> carList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql =  "SELECT CAR.*\n" +
                    "FROM CAR\n" +
                    "LEFT JOIN CUSTOMER\n" +
                    "ON CAR.ID = CUSTOMER.RENTED_CAR_ID\n" +
                    "INNER JOIN COMPANY\n" +
                    "ON CAR.COMPANY_ID = COMPANY.ID\n" +
                    "WHERE CUSTOMER.RENTED_CAR_ID IS NULL\n" +
                    "AND COMPANY.NAME = '" + companyName + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int company_id = resultSet.getInt("company_id");
                carList.add(new Car(id, name, company_id));
            }
            statement.close();
        } catch (SQLException se) {
     //       se.printStackTrace();
        }
        return carList;
    }

    @Override
    public void deleteTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "DROP TABLE CAR";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
         //   se.printStackTrace();
        }
    }
}