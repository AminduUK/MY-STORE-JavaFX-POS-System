package controller.order;

import controller.item.ItemController;
import db.DBConnection;
import javafx.scene.control.Alert;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderController implements OrderService{

    @Override
    public boolean placeOrder(Order order) {
        String SQL = "INSERT INTO orders VALUE(?,?,?)";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setObject(1,order.getOrderID());
            pstm.setObject(2,order.getOrderDate());
            pstm.setObject(3,order.getCustomerID());
            boolean isOrderAdded = pstm.executeUpdate()>0;
            if (isOrderAdded){
                boolean isOrderDetailAdded = new OrderDetailController().addOrderDetail(order.getOrderDetailList());
                 if (isOrderDetailAdded){
                     boolean isStockUpdated = ItemController.getInstance().updateStock(order.getOrderDetailList());
                     if (isStockUpdated){
                         new Alert(Alert.AlertType.INFORMATION,"Order Placed Successfully!").show();
                     } else {
                         new Alert(Alert.AlertType.ERROR,"Order Not Placed!").show();
                     }
                 }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public String fetchCustomerDetailsByPhoneNumber(int phoneNumber) {
        String SQL = "SELECT ID FROM customer WHERE Contact = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setInt(1, phoneNumber);  // Bind the phone number to the query
            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ID");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String fetchItemDetailsByItemDescription(String code) {
        String SQL = "SELECT ItemCode FROM item WHERE Description = ?";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, code);
            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("ItemCode");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<String> fetchItemNamesFromDatabase(String itemName) {
        List<String> itemNames = new ArrayList<>();
        String SQL = "SELECT Description FROM item WHERE Description LIKE ? LIMIT 5";  // Fetch a few matches
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, itemName + "%"); // Bind itemName to query with a wildcard for matching
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                itemNames.add(resultSet.getString("Description"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itemNames;
    }

    @Override
    public String getNextOrderID() {
        String SQL = "SELECT OrderID FROM orders ORDER BY OrderID DESC LIMIT 1";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();
            String lastID = "";
            if (resultSet.next()) {
                lastID = resultSet.getString("OrderID");
            } else {
                lastID = "D000";
            }
            return lastID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
