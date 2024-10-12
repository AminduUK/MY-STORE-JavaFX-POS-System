package controller.item;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Item;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ItemManagerFormController implements Initializable {

    @FXML
    public Button btnAddItem;

    @FXML
    public Button btnRefreshItem;

    @FXML
    public Button btnSearchItem;

    @FXML
    public Button btnUpdateItem;

    @FXML
    public Button btnDeleteItem;

    @FXML
    public Button btnClearItem;

    @FXML
    private TableColumn colDescription;

    @FXML
    private TableColumn colItemCode;

    @FXML
    private TableColumn colPackSize;

    @FXML
    private TableColumn colQuantity;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    public TableView<Item> tblItem;

    @FXML
    private TextArea txtAddDescription;

    @FXML
    private TextField txtAddItemCode;

    @FXML
    private TextField txtAddPackSize;

    @FXML
    private TextField txtAddQuantity;

    @FXML
    private TextField txtAddUnitPrice;

    @FXML
    private TextArea txtSearchDescription;

    @FXML
    private TextField txtSearchItem;

    @FXML
    private TextField txtSearchItemCode;

    @FXML
    private TextField txtSearchPackSize;

    @FXML
    private TextField txtSearchQuantity;

    @FXML
    private TextField txtSearchUnitPrice;

    ItemService service = ItemController.getInstance();

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Item item = new Item(
                txtAddItemCode.getText(),
                txtAddDescription.getText(),
                txtAddPackSize.getText(),
                Double.parseDouble(txtAddUnitPrice.getText()),
                Integer.parseInt(txtAddQuantity.getText())
        );
        if (service.addItem(item)){
            new Alert(Alert.AlertType.INFORMATION,"Item Added Successfully").show();
            loadTable();
            clearFields();
            nextCodeGenerator();
        }else{
            new Alert(Alert.AlertType.ERROR,"Item Not Added! Please Try Again").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Item item = new Item(
                txtSearchItemCode.getText(),
                txtSearchDescription.getText(),
                txtSearchPackSize.getText(),
                Double.parseDouble(txtSearchUnitPrice.getText()),
                Integer.parseInt(txtSearchQuantity.getText())
        );
        if (service.updateItem(item)){
            new Alert(Alert.AlertType.INFORMATION,"Item Updated Successfully").show();
            loadTable();
            clearSearchFields();
        }else{
            new Alert(Alert.AlertType.ERROR,"Item Not Updated! Please Try Again").show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String code = txtSearchItem.getText();
        Item item = service.searchItem(code);
        if(Objects.equals(item.getItemCode(), code)){
            txtSearchItemCode.setText(item.getItemCode());
            txtSearchDescription.setText(item.getDescription());
            txtSearchPackSize.setText(item.getPackSize());
            txtSearchUnitPrice.setText(String.valueOf(item.getUnitPrice()));
            txtSearchQuantity.setText(String.valueOf(item.getQuantity()));
        }else{
            new Alert(Alert.AlertType.ERROR,"Invalid Item Code! Please Try Again").show();
            clearSearchFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (service.deleteItem(txtSearchItem.getText())){
            new Alert(Alert.AlertType.INFORMATION,"Item Deleted !!").show();
            txtSearchItem.setText(null);
            clearSearchFields();
            loadTable();
            nextCodeGenerator();
        }else{
            new Alert(Alert.AlertType.ERROR,"Item Not Deleted! Please Try Again").show();
        }
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        loadTable();
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearSearchFields();
    }

    public void nextCodeGenerator(){
        String lastCode = service.getNextItemCode();
        // Extract numeric part, increment and format the new ID
        String numericPart = lastCode.substring(1);
        int nextCode = Integer.parseInt(numericPart) + 1;
        String newCode = String.format("P%03d", nextCode);
        txtAddItemCode.setText(newCode);
    }

    public void loadTable() {
        ObservableList<Item> itemObservableList = service.getAll();

        if(itemObservableList.isEmpty()){
            tblItem.setItems(itemObservableList);
        }else{
            tblItem.getItems().clear();
            tblItem.setItems(itemObservableList);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("PackSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadTable();
        nextCodeGenerator();

        tblItem.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Item rowData = row.getItem();
                    openItemDetails(rowData); // Open new window with row data
                }
            });
            return row;
        });
    }

    public Stage stage = new Stage();

    private void openItemDetails(Item item) {
        try {
            // Load the customer detail form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/item_detail_form.fxml"));
            Parent ItemDetailsRoot = loader.load();

            // Get the controller for customer detail form
            ItemDetailFormController controller = loader.getController();

            // Pass the selected customer data to the controller
            controller.setValues(item);
            controller.setMainController(this);

            // Show the new stage (window) with customer details
            stage.setTitle("Item Details");
            stage.setScene(new Scene(ItemDetailsRoot));
            stage.setOnCloseRequest(event -> {
                controller.handleClose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSearchFields(Item item){
        txtSearchItemCode.setText(item.getItemCode());
        txtSearchDescription.setText(item.getDescription());
        txtSearchPackSize.setText(item.getPackSize());
        txtSearchUnitPrice.setText(String.valueOf(item.getUnitPrice()));
        txtSearchQuantity.setText(String.valueOf(item.getQuantity()));
    }

    public void clearFields(){
        txtAddItemCode.setText(null);
        txtAddDescription.setText(null);
        txtAddPackSize.setText(null);
        txtAddUnitPrice.setText(null);
        txtAddQuantity.setText(null);
    }

    public void clearSearchFields(){
        txtSearchItem.setText(null);
        txtSearchItemCode.setText(null);
        txtSearchDescription.setText(null);
        txtSearchPackSize.setText(null);
        txtSearchUnitPrice.setText(null);
        txtSearchQuantity.setText(null);
    }
}
