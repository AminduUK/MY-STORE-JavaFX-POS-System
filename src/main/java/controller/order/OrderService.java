package controller.order;

import model.Order;

import java.util.List;

public interface OrderService {
    String fetchCustomerDetailsByPhoneNumber(int phoneNumber);

    String fetchItemDetailsByItemDescription(String code);

    List<String> fetchItemNamesFromDatabase(String itemName);

    String getNextOrderID();

    boolean placeOrder(Order order);
}
