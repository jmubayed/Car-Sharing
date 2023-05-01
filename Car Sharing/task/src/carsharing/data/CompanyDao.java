package carsharing.data;

import carsharing.model.Company;
import java.util.List;

public interface CompanyDao {
    public void createTable();
    public void insertCompany(String name);
    public List<Company> selectCompany();

    void deleteTable();
}