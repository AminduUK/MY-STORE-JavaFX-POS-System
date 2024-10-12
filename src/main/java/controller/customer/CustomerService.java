package controller.customer;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import model.Customer;

public interface CustomerService {
    boolean addCustomer(Customer customer);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(String id);

    Customer searchCustomer(String id);

    ObservableList<Customer> getAll();

    String getNextCustomerID();

    boolean isContactNumExists(String contNo);

    ObservableList<String> getCustomerIDs();
}
