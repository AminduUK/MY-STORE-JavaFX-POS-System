package controller.item;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;
import model.OrderDetail;
import util.CrudUtil;

import java.sql.*;
import java.util.List;

public class ItemController implements ItemService{

    private static ItemController instance;

    private ItemController(){}

    public static ItemController getInstance(){
        return instance == null ? instance = new ItemController() : instance;
    }

    @Override
    public boolean addItem(Item item) {
        String SQL = "INSERT INTO item VALUES(?,?,?,?,?)";
        try {
           return CrudUtil.execute(
                   SQL,
                   item.getItemCode(),
                   item.getDescription(),
                   item.getPackSize(),
                   item.getUnitPrice(),
                   item.getQuantity()
           );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateItem(Item item) {
        String code = item.getItemCode();
        String SQL = "UPDATE item SET Description = ?, PackSize = ?, UnitPrice = ?, QtyOnHand = ? WHERE ItemCode = ?";
        try {
            return CrudUtil.execute(
                    SQL,
                    item.getDescription(),
                    item.getPackSize(),
                    item.getUnitPrice(),
                    item.getQuantity(),
                    item.getItemCode()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteItem(String code) {
        String SQL = "DELETE FROM item WHERE ItemCode='" + code + "'";
        try {
            return CrudUtil.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item searchItem(String code) {
        String SQL = "SELECT * FROM item WHERE ItemCode=?";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL,code);
            while (resultSet.next()){
                return new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ObservableList<Item> getAll() {
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM item";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL);
            while (resultSet.next()){
                itemObservableList.add(new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                ));
            }
            return itemObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getNextItemCode() {
        String SQL = "SELECT ItemCode FROM item ORDER BY ItemCode DESC LIMIT 1";
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement(SQL);
            ResultSet resultSet = pstm.executeQuery();
            // If there is a result, process it
            String lastCode = "";
            if (resultSet.next()) {
                lastCode = resultSet.getString("ItemCode");
            } else {
                // If no customers exist, start with C001
                lastCode = "P000";
            }
            return lastCode;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<String> getItemCodes() {
        ObservableList<String> itemCodes = FXCollections.observableArrayList();
        ObservableList<Item> itemObservableList = getAll();
        itemObservableList.forEach(item ->{
                itemCodes.add(item.getItemCode());
        });
        return itemCodes;
    }

    @Override
    public boolean updateStock(List<OrderDetail> orderDetailList) {
        for (OrderDetail orderDetail : orderDetailList){
            boolean updateStock = updateStock(orderDetail);
            if (!updateStock){
                return false;
            }
        }
        return true;
    }

    private boolean updateStock(OrderDetail orderDetails){
        String SQL = "UPDATE item SET QtyOnHand = QtyOnHand - ? WHERE itemCode = ?";
        try {
            return CrudUtil.execute(SQL,orderDetails.getQuantity(),orderDetails.getItemCode());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
