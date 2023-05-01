package carsharing;

import carsharing.data.*;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        String databaseFileName = "carsharing";
        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            databaseFileName = args[1];
        }
        Database databaseCarsharing = new Database(databaseFileName);
        CompanyDao companyDaoImp = new CompanyDaoImp(databaseCarsharing);
        CarDao carDaoImp = new CarDaoImp(databaseCarsharing);
        CustomerDao customerDao = new CustomerDaoImp(databaseCarsharing);
//        customerDao.deleteTable();
//        carDaoImp.deleteTable();
//        companyDaoImp.deleteTable();
        companyDaoImp.createTable();
        carDaoImp.createTable();
        customerDao.createTable();
        Scanner scanner = new Scanner(System.in);
        startMenu();
        boolean flagFromCarMenuToMainMenu;
        int option = scanner.nextInt();
        while (option != 0) {
            if (option == 1) {
                secondMenu();
                option = scanner.nextInt();
                while (option != 0) {
                    if (option == 1) {
                        List<Company> companyList = companyDaoImp.selectCompany();
                        if (companyList.isEmpty()) {
                            System.out.println("The company list is empty!");
                        } else {
                            System.out.println("Choose a company:");
                            companyList.forEach(System.out::println);
                            System.out.println("0. Back");
                            System.out.println();
                            flagFromCarMenuToMainMenu = false;
                            while(true){
                                if(flagFromCarMenuToMainMenu){
                                    break;
                                }
                                option = scanner.nextInt();
                                if(option != 0){
                                    int finalOption = option;
                                    List<Company> selectedCompany = companyList.stream().filter(t -> t.getId() == finalOption).collect(Collectors.toList());
                                    if(selectedCompany.size() == 1){
                                        System.out.println("'"+ selectedCompany.get(0).getName() + "' company:");
                                        while (true) {
                                            thirdMenu();
                                            option = scanner.nextInt();
                                            if (option == 1) {
                                                List<Car> carList = carDaoImp.selectCar(selectedCompany.get(0).getName());
                                                if (carList.isEmpty()) {
                                                    System.out.println("The car list is empty!");
                                                }else{
                                                    System.out.println("Car list:");
                                                    for(int i = 1; i <= carList.size(); i++){
                                                        System.out.println(i+". " + carList.get(i-1).getName());
                                                    }
                                                    System.out.println();
                                                }
                                            }else if(option == 2){
                                                System.out.println("Enter the car name:");
                                                String name = scanner.nextLine();
                                                if (name.isEmpty()) {
                                                    name = scanner.nextLine();
                                                }
                                                carDaoImp.insertCar(name, selectedCompany.get(0).getId());
                                                System.out.println("The car was added!");
                                            }else{
                                                flagFromCarMenuToMainMenu = true;
                                                break;
                                            }
                                        }
                                    }
                                }else{
                                    break;
                                }
                            }
                        }
                    } else if (option == 2) {
                        System.out.println("Enter the company name:");
                        String name = scanner.nextLine();
                        if (name.isEmpty()) {
                            name = scanner.nextLine();
                        }
                        companyDaoImp.insertCompany(name);
                        System.out.println("The company was created!");
                    }
                    secondMenu();
                    option = scanner.nextInt();
                }
            }else if(option == 2){
                List<Customer> customerList = customerDao.selectCustomer();
                if(customerList.size() != 0){
                    System.out.println("Customer list:");
                    for(int i = 1; i <= customerList.size(); i++){
                        System.out.println(i+". " + customerList.get(i-1).getName());
                    }
                    System.out.println("0. Back");
                    System.out.println();
                    option = scanner.nextInt();
                    Customer customer = customerList.get(option-1);
                    if(option != 0){
                        while(true){
                            fourthMenu();
                            int optionExit = scanner.nextInt();
                            if(optionExit == 1){
                                if(customerDao.checkIfCustomerAlreadyRentACar(customer.getName())){
                                    System.out.println("You've already rented a car!");
                                    System.out.println();
                                }else{
                                    List<Company> companyList = companyDaoImp.selectCompany();
                                    if(companyList.size() != 0){
                                        System.out.println("Choose a company:");
                                        for(int i = 1; i <= companyList.size(); i++){
                                            System.out.println(i+". " + companyList.get(i-1).getName());
                                        }
                                        System.out.println("0. Back");
                                        System.out.println();
                                        option = scanner.nextInt();
                                        int finalOption1 = option;
                                        List<Company> selectedCompany = companyList.stream().filter(t -> t.getId() == finalOption1).collect(Collectors.toList());
                                        if(option != 0){
                                            List<Car> carList = carDaoImp.selectCar(selectedCompany.get(0).getName());
                                            if (carList.isEmpty()) {
                                                System.out.println("No available cars in the '"+ selectedCompany.get(0).getName() + "' company");
                                            }else{
                                                System.out.println("Choose a car:");
                                                for(int i = 1; i <= carList.size(); i++){
                                                    System.out.println(i+". " + carList.get(i-1).getName());
                                                }
                                                System.out.println("0. Back");
                                                System.out.println();
                                                option = scanner.nextInt();
                                                if(option != 0){
                                                    String carName = carList.get(option - 1).getName();
                                                    customerDao.updateRentedCarId(customer.getName(), carName);
                                                    System.out.println("You rented '" + carName + "'");
                                                    System.out.println();
                                                }
                                            }
                                        }
                                    }else{
                                        System.out.println("The company list is empty!");
                                       // fourthMenu();
                                    }
                                }
                            }else if(optionExit == 2){
                                if(customerDao.returnRentedCar(customer.getName())){
                                    System.out.println("You've returned a rented car!");
                                    System.out.println();
                                }else{
                                    System.out.println("You didn't rent a car!");
                                    System.out.println();
                                }
                            }else if(optionExit == 3){
                                List<String> rentedCars = customerDao.getCustomerRentedCar(customer.getName());
                                if(rentedCars.size() != 0){
                                    System.out.println();
                                    System.out.println("Your rented car:\n" +
                                            ""+ rentedCars.get(0) +"\n" +
                                            "Company:\n" +
                                            "" + rentedCars.get(1) + "");
                                    System.out.println();
                                }else{
                                    System.out.println("You didn't rent a car!");
                                    System.out.println();
                                }
                            }else if (optionExit == 0){
                                break;
                            }
                        }
                    }
                }else{
                    System.out.println("The customer list is empty!");
                    System.out.println();
                }
            }else if(option == 3){
                System.out.println("Enter the customer name:");
                String name = scanner.nextLine();
                if (name.isEmpty()) {
                    name = scanner.nextLine();
                }
                customerDao.insertCustomer(name);
                System.out.println("The customer was added!");
                System.out.println();
            }
            startMenu();
            option = scanner.nextInt();
        }

        databaseCarsharing.closeConnection();
    }

    public static void startMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
    }

    private static void secondMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }
    private static void thirdMenu() {
        System.out.println("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
    }

    private static void fourthMenu() {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }
}