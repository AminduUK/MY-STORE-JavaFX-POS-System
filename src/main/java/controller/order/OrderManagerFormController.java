package controller.order;

import com.jfoenix.controls.JFXTextField;
import controller.customer.CustomerController;
import controller.item.ItemController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.CartList;
import model.Order;
import model.OrderDetail;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderManagerFormController implements Initializable {

    @FXML
    public Label lblDate;

    @FXML
    public Label lblTime;

    @FXML
    public Label lblNetTotal;

    @FXML
    public ComboBox<String> cmbCustomerID;

    @FXML
    public JFXTextField txtOrderID;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    public JFXTextField txtCustomerContact;

    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    public Button btnClear;

    @FXML
    public Button btnClearTable;

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    public TextField txtPackSize;

    @FXML
    public TextField txtItemName;

    @FXML
    private TextField txtUnitPrice;

    @FXML
    public TextField txtQuantity;

    @FXML
    public TextField txtStock;

    @FXML
    public TableColumn colItemCode;

    @FXML
    public TableColumn colUnitPrice;

    @FXML
    public TableColumn colTotal;

    @FXML
    public TableColumn colQuantity;

    @FXML
    public TableColumn colPackSize;

    @FXML
    public TableColumn colItemName;

    @FXML
    private TableView<CartList> tblOrder;

    OrderService service = new OrderController();

    ObservableList<CartList> orderObservableList = FXCollections.observableArrayList();

    @FXML
    public void btnAddToCartOnAction(ActionEvent event) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        String itemCode = cmbItemCode.getValue();
        String itemName = txtItemName.getText();
        String packSize = txtPackSize.getText();
        Integer quantity = Integer.parseInt(txtQuantity.getText());
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Double total = unitPrice * quantity;

        boolean state = true;

        if (txtCustomerContact.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please Enter The Customer Details").show();
            state = false;
        }
        if (Integer.parseInt(txtQuantity.getText())>Integer.parseInt(txtStock.getText())){
            new Alert(Alert.AlertType.ERROR,"No Sufficient Stock Available").show();
            state = false;
            txtQuantity.setText(null);
        }
        if (state){
            orderObservableList.add(new CartList(itemCode,itemName,packSize,quantity,unitPrice,total));
            tblOrder.setItems(orderObservableList);
            lblNetTotal.setText("Rs."+calculateTotal());
            clearItmFields();
        }
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent event) {
        String orderID = txtOrderID.getText();
        LocalDate orderDate = LocalDate.now();
        String customerID = cmbCustomerID.getValue();

        List<OrderDetail> orderDetails = new ArrayList<>();

        orderObservableList.forEach(obj->{
            orderDetails.add(new OrderDetail(orderID, obj.getItemCode(), obj.getQuantity(), 0.0));
        });

        Order order = new Order(orderID, orderDate, customerID, orderDetails);

        service.placeOrder(order);

        tblOrder.getItems().clear();
        lblNetTotal.setText("Rs.0000");
        clearCustFields();
        nextIdGenerator();
        System.out.println(order);
    }

    @FXML
    public void btnClearTableOnAction(ActionEvent event) {
        tblOrder.getItems().clear();
        lblNetTotal.setText("Rs.0000");
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearCustFields();
        clearItmFields();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
        loadCustomerIDs();
        loadItemCodes();
        nextIdGenerator();

        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newCus) -> {
            if (newCus!=null){
                searchCustomer(newCus);
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newItm) -> {
            if (newItm!=null){
                searchItem(newItm);
            }
        });
    }

    private void loadDateAndTime(){
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = f.format(date);
        lblDate.setText(dateNow );

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,e->{
            LocalTime now = LocalTime.now();
            lblTime.setText(now.getHour()+" : "+ now.getMinute()+" : "+ now.getSecond());
        }),
            new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadCustomerIDs(){
        ObservableList<String> customerIDs = CustomerController.getInstance().getCustomerIDs();
        cmbCustomerID.setItems(customerIDs);
    }

    private void loadItemCodes(){
        ObservableList<String> itemCodes = ItemController.getInstance().getItemCodes();
        cmbItemCode.setItems(itemCodes);
    }

    private void searchCustomer(String id){
        txtCustomerName.setText(CustomerController.getInstance().searchCustomer(id).getName());
        txtCustomerContact.setText(String.valueOf(CustomerController.getInstance().searchCustomer(id).getContactNum()));
    }

    private void searchItem(String code) {
        txtItemName.setText(ItemController.getInstance().searchItem(code).getDescription());
        txtPackSize.setText(ItemController.getInstance().searchItem(code).getPackSize());
        txtUnitPrice.setText(String.valueOf(ItemController.getInstance().searchItem(code).getUnitPrice()));
        txtStock.setText(String.valueOf(ItemController.getInstance().searchItem(code).getQuantity()));
    }

    @FXML
    void onPhoneNumberEntered(ActionEvent event) {
        // Fetch the phone number entered by the user
        int phoneNumber = Integer.parseInt(txtCustomerContact.getText());
        // Perform the database operation
        String id = service.fetchCustomerDetailsByPhoneNumber(phoneNumber);
        if(id!=null){
            txtCustomerName.setText(CustomerController.getInstance().searchCustomer(id).getName());
            cmbCustomerID.getItems().clear();  // Clear previous values
            cmbCustomerID.getItems().add(CustomerController.getInstance().searchCustomer(id).getId());  // Add new value
            cmbCustomerID.getSelectionModel().select(CustomerController.getInstance().searchCustomer(id).getId());  // Select the new value
            loadCustomerIDs();
        } else {
            new Alert(Alert.AlertType.ERROR,"Invalid Contact Number! Please Try Again").show();
            clearCustFields();
            loadCustomerIDs();
        }
    }

    private ContextMenu suggestionsMenu = new ContextMenu(); // For showing suggestions

    public void onItemNameKeyTyped(javafx.scene.input.KeyEvent keyEvent) {
        String input = txtItemName.getText();
        if (input.length() >= 2) { // Start suggesting after 2 characters
            showSuggestions(input);
            loadItemCodes();
        } else {
            suggestionsMenu.hide();
        }
    }

    // Method to fetch matching item names from the database
    private void showSuggestions(String itemName) {
        List<String> suggestions = service.fetchItemNamesFromDatabase(itemName);

        if (suggestions.isEmpty()) {
            suggestionsMenu.hide();  // Hide menu if no suggestions
            return;
        }
        // Clear existing items in the context menu
        suggestionsMenu.getItems().clear();

        // Add new menu items based on database results
        for (String Description: suggestions) {
            MenuItem menuItem = new MenuItem(Description);

            // When a suggestion is selected, set it to the text field
            menuItem.setOnAction(e -> {
                txtItemName.setText(Description);
                getDetailsByItemCode();
                suggestionsMenu.hide();  // Hide the menu after selection
            });

            suggestionsMenu.getItems().add(menuItem);
        }

        // Show suggestions if not showing already
        if (!suggestionsMenu.isShowing()) {
            suggestionsMenu.show(txtItemName, txtItemName.getLayoutX(), txtItemName.getLayoutY() + txtItemName.getHeight());
        }
    }

    public void getDetailsByItemCode(){
        String itemName = txtItemName.getText();
        String code = service.fetchItemDetailsByItemDescription(itemName);
        if (code!=null){
            txtPackSize.setText(ItemController.getInstance().searchItem(code).getPackSize());
            txtUnitPrice.setText(String.valueOf(ItemController.getInstance().searchItem(code).getUnitPrice()));
            cmbItemCode.getItems().clear();
            cmbItemCode.getItems().add(ItemController.getInstance().searchItem(code).getItemCode());
            cmbItemCode.getSelectionModel().select(ItemController.getInstance().searchItem(code).getItemCode());
            txtStock.setText(String.valueOf(ItemController.getInstance().searchItem(code).getQuantity()));
        } else {
            new Alert(Alert.AlertType.ERROR,"Invalid Item Name! Please Try Again").show();
            clearItmFields();
            loadItemCodes();
        }
    }

    public void nextIdGenerator(){
        String lastID = service.getNextOrderID();
        String numericPart = lastID.substring(1);
        int nextID = Integer.parseInt(numericPart) + 1;
        String newID = String.format("D%03d", nextID);
        txtOrderID.setText(newID);
    }

    private Double calculateTotal(){
        Double netTotal = 0.0;
        for (CartList cartList : orderObservableList){
            netTotal = netTotal + cartList.getTotal();
        }
        return netTotal;
    }

    private void clearCustFields(){
        txtCustomerName.setText(null);
        txtCustomerContact.setText(null);
        cmbCustomerID.getSelectionModel().clearSelection();
        cmbCustomerID.setValue(null);
    }

    private void clearItmFields(){
        txtItemName.setText(null);
        txtUnitPrice.setText(null);
        txtPackSize.setText(null);
        txtQuantity.setText(null);
        txtStock.setText(null);
        cmbItemCode.getSelectionModel().clearSelection();
        cmbItemCode.setValue(null);
    }
}
