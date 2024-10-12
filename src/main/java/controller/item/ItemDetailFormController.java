package controller.item;

import controller.customer.CustomerManagerFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Item;

public class ItemDetailFormController {

    @FXML
    public Button btnUpdate3;

    @FXML
    public Button btnDelete4;

    @FXML
    private TextArea txtDisplayDescription;

    @FXML
    private TextField txtDisplayItemCode;

    @FXML
    private TextField txtDisplayPackSize;

    @FXML
    private TextField txtDisplayQuantity;

    @FXML
    private TextField txtDisplayUnitPrice;

    private Item item;

    private ItemManagerFormController mainController;

    public void setValues(Item item) {
        txtDisplayItemCode.setText(item.getItemCode());
        txtDisplayDescription.setText(item.getDescription());
        txtDisplayPackSize.setText(item.getPackSize());
        txtDisplayUnitPrice.setText("Rs."+item.getUnitPrice());
        txtDisplayQuantity.setText(String.valueOf(item.getQuantity()));

        this.item = item;
    }

    public void setMainController(ItemManagerFormController mainController) {
        this.mainController = mainController;
    }

    public void handleClose() {
        if (mainController != null) {
            mainController.loadTable();  // Call the method to reload the table
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (new ItemManagerFormController().service.deleteItem(txtDisplayItemCode.getText())){
            new Alert(Alert.AlertType.INFORMATION,"Item Deleted !!").show();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            mainController.loadTable();
            mainController.nextCodeGenerator();
        }else{
            new Alert(Alert.AlertType.ERROR,"Item Not Deleted! Please Try Again").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        mainController.setSearchFields(item);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        mainController.loadTable();
    }
}
