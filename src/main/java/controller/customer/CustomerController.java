package controller.customer;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import util.CrudUtil;

import java.sql.*;

public class CustomerController implements CustomerService{

    private static CustomerController instance;

    private CustomerController(){}

    public static CustomerController getInstance(){
        return instance == null ? instance = new CustomerController() : instance;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        String SQL = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?)";
        try {
            return CrudUtil.execute(
                    SQL,
                    customer.getId(),
                    customer.getTitle(),
                    customer.getName(),
                    customer.getDob(),
                    customer.getContactNum(),
                    customer.getProfession(),
                    customer.getAddress(),
                    customer.getCity()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        String id = customer.getId();
        String SQL = "UPDATE customer SET Title = ?, Name = ?, DOB = ?, Contact = ?, Profession = ?, Address = ?, City = ? WHERE ID = ?";
        try {
            return CrudUtil.execute(
                    SQL,
                    customer.getTitle(),
                    customer.getName(),
                    customer.getDob(),
                    customer.getContactNum(),
                    customer.getProfession(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getId()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteCustomer(String id) {
        String SQL = "DELETE FROM customer WHERE ID='" + id + "'";
        try {
            return CrudUtil.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer searchCustomer(String id) {
        String SQL = "SELECT * FROM customer WHERE ID = ?";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL,id);
            while (resultSet.next()){
                return new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate(),
                        resultSet.getInt(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ObservableList<Customer> getAll() {
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM customer";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL);
            while (resultSet.next()){
                customerObservableList.add(new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate(),
                        resultSet.getInt(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8)
                ));
            }
            return customerObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNextCustomerID() {
        String SQL = "SELECT ID FROM customer ORDER BY ID DESC LIMIT 1";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();
            // If there is a result, process it
            String lastID = "";
            if (resultSet.next()) {
                lastID = resultSet.getString("ID");
            } else {
                // If no customers exist, start with C001
                lastID = "C000";
            }
            return lastID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isContactNumExists(String contNum){
        String SQL = "SELECT Contact FROM customer WHERE Contact = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1,contNum);
            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                // If a record is found, the phone number exists
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<String> getCustomerIDs() {
        ObservableList<String> customerIDs = FXCollections.observableArrayList();
        ObservableList<Customer> customerObservableList = getAll();
        customerObservableList.forEach(customer->{
            customerIDs.add(customer.getId());
        });
        return customerIDs;
    }
}
