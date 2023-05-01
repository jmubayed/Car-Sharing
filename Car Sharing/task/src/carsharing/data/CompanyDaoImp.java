package carsharing.data;

import carsharing.model.Company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImp implements CompanyDao {

    private Database databaseCarsharing;
    private Connection connection;

    public CompanyDaoImp(Database databaseCarsharing) {
        this.databaseCarsharing = databaseCarsharing;
        this.connection = databaseCarsharing.getConnection();
    }

    @Override
    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    " name VARCHAR(255) UNIQUE NOT NULL," +
                    "PRIMARY KEY (id))";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
          //  se.printStackTrace();
        }
    }

    @Override
    public void insertCompany(String nameCompany) {
        try {
            Statement statement = connection.createStatement();
            String sql =  "INSERT INTO COMPANY (name) VALUES ('" + nameCompany + "')";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
          //  se.printStackTrace();
        }
    }

    @Override
    public List<Company> selectCompany() {
        List<Company> companyList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql =  "SELECT * FROM COMPANY";
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                companyList.add(new Company(id, name));
            }
            statement.close();
        } catch (SQLException se) {
         //   se.printStackTrace();
        }
        return companyList;
    }

    @Override
    public void deleteTable() {
        try {
            Statement statement = connection.createStatement();
            String sql =  "DROP TABLE COMPANY";
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException se) {
         //   se.printStackTrace();
        }
    }
}