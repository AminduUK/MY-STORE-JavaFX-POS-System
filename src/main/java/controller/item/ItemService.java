package controller.item;

import javafx.collections.ObservableList;
import model.Item;
import model.OrderDetail;

import java.util.List;

public interface ItemService {
    boolean addItem(Item item);

    boolean updateItem(Item item);

    boolean deleteItem(String code);

    Item searchItem(String code);

    ObservableList<Item> getAll();

    String getNextItemCode();

    ObservableList<String> getItemCodes();

    boolean updateStock(List<OrderDetail> orderDetailList);
}
