package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerManagerFormController implements Initializable {
    @FXML
    public Button btnClearCustomer;

    @FXML
    public Button btnAddCustomer;

    @FXML
    public Button btnUpdateCustomer;

    @FXML
    public Button btnSearchCustomer;

    @FXML
    public Button btnDeleteCustomer;

    @FXML
    public Button btnRefreshCustomer;

    @FXML
    private ComboBox cmbAddTitle;

    @FXML
    public ComboBox cmbSearchTitle;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colCity;

    @FXML
    private TableColumn colContact;

    @FXML
    private TableColumn colDOB;

    @FXML
    private TableColumn colID;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colProfession;

    @FXML
    private TableColumn colTitle;

    @FXML
    private DatePicker dateAddDOB;

    @FXML
    private DatePicker dateSearchDOB;

    @FXML
    private TableView tblCustomer;

    @FXML
    private TextField txtAddAddress;

    @FXML
    private TextField txtAddCity;

    @FXML
    private TextField txtAddContactNum;

    @FXML
    private TextField txtAddID;

    @FXML
    private TextField txtAddName;

    @FXML
    private TextField txtAddProfession;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSearchAddress;

    @FXML
    private TextField txtSearchCity;

    @FXML
    private TextField txtSearchContactNum;

    @FXML
    private TextField txtSearchID;

    @FXML
    private TextField txtSearchName;

    @FXML
    private TextField txtSearchProfession;

    CustomerService service = CustomerController.getInstance();

    @FXML
    void btnAddOnAction(ActionEvent event) {

        LocalDate dob = dateAddDOB.getValue();
        LocalDate today = LocalDate.now();
        String contNo = txtAddContactNum.getText();
        boolean state = true;

        if(dob.isAfter(today)){
            new Alert(Alert.AlertType.ERROR,"Invalid Birthday! Please Try Again").show();
            state = false;
            dateAddDOB.setValue(null);
        }
        if(!(contNo.startsWith("07") && contNo.length() == 10)) {
            new Alert(Alert.AlertType.ERROR,"Invalid Contact Number! Please Try Again").show();
            state = false;
            txtAddContactNum.setText(null);
        }
        if (service.isContactNumExists(contNo)){
            new Alert(Alert.AlertType.ERROR,"Duplicate Contact Number! Please Try Again").show();
            state = false;
            txtAddContactNum.setText(null);
        }
        if(state){
            Customer customer = new Customer(
                    txtAddID.getText(),
                    cmbAddTitle.getValue().toString(),
                    txtAddName.getText(),
                    dateAddDOB.getValue(),
                    Integer.parseInt(txtAddContactNum.getText()),
                    txtAddProfession.getText(),
                    txtAddAddress.getText(),
                    txtAddCity.getText()
            );
            if (service.addCustomer(customer)){
                new Alert(Alert.AlertType.INFORMATION,"Customer Added Successfully").show();
                loadTable();
                cmbAddTitle.getSelectionModel().clearSelection();
                clearFields();
                nextIdGenerator();
            }else{
                new Alert(Alert.AlertType.ERROR,"Customer Not Added! Please Try Again").show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Customer customer = new Customer(
                txtSearchID.getText(),
                cmbSearchTitle.getValue().toString(),
                txtSearchName.getText(),
                dateSearchDOB.getValue(),
                Integer.parseInt(txtSearchContactNum.getText()),
                txtSearchProfession.getText(),
                txtSearchAddress.getText(),
                txtSearchCity.getText()
        );
        if (service.updateCustomer(customer)){
            new Alert(Alert.AlertType.INFORMATION,"Customer Updated Successfully").show();
            loadTable();
            clearSearchFields();
        }else{
            new Alert(Alert.AlertType.ERROR,"Customer Not Updated! Please Try Again").show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String id = txtSearch.getText();
        Customer customer = service.searchCustomer(id);
        if(Objects.equals(customer.getId(), id)){
            txtSearchID.setText(customer.getId());
            cmbSearchTitle.setValue(customer.getTitle());
            txtSearchName.setText(customer.getName());
            dateSearchDOB.setValue(customer.getDob());
            txtSearchContactNum.setText(String.valueOf(customer.getContactNum()));
            txtSearchProfession.setText(customer.getProfession());
            txtSearchAddress.setText(customer.getAddress());
            txtSearchCity.setText(customer.getCity());
        }else{
            new Alert(Alert.AlertType.ERROR,"Invalid Customer ID! Please Try Again").show();
            clearSearchFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (service.deleteCustomer(txtSearch.getText())){
            new Alert(Alert.AlertType.INFORMATION,"Customer Deleted !!").show();
            txtSearch.setText(null);
            clearSearchFields();
            loadTable();
            nextIdGenerator();
        }else{
            new Alert(Alert.AlertType.ERROR,"Customer Not Deleted! Please Try Again").show();
        }
    }

    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        loadTable();
    }

    @FXML
    public void btnClearOnAction(ActionEvent actionEvent) {
        clearSearchFields();
    }

    public void nextIdGenerator(){
        String lastID = service.getNextCustomerID();
        // Extract numeric part, increment and format the new ID
        String numericPart = lastID.substring(1); // Get the "001" part
        int nextID = Integer.parseInt(numericPart) + 1; // Increment the numeric part
        String newID = String.format("C%03d", nextID); // Format as CXXX, e.g., C002
        txtAddID.setText(newID);
    }

    public void loadTable() {
        ObservableList<Customer> customerObservableList = service.getAll();

        if(customerObservableList.isEmpty()){
            tblCustomer.setItems(customerObservableList);
        }else{
            tblCustomer.getItems().clear();
            tblCustomer.setItems(customerObservableList);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        colProfession.setCellValueFactory(new PropertyValueFactory<>("profession"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));

        ObservableList<String> titles = FXCollections.observableArrayList("Mr", "Miss", "Mrs");
        cmbAddTitle.setItems(titles);
        cmbSearchTitle.setItems(titles);

        loadTable();
        nextIdGenerator();

        tblCustomer.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Customer rowData = row.getItem();
                    openCustomerDetails(rowData); // Open new window with row data
                }
            });
            return row;
        });
    }

    public Stage stage = new Stage();

    private void openCustomerDetails(Customer customer) {
        try {
            // Load the customer detail form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/customer_detail_form.fxml"));
            Parent customerDetailsRoot = loader.load();

            // Get the controller for customer detail form
            CustomerDetailFormController controller = loader.getController();

            // Pass the selected customer data to the controller
            controller.setValues(customer);
            controller.setMainController(this);

            // Show the new stage (window) with customer details
            stage.setTitle("Customer Details");
            stage.setScene(new Scene(customerDetailsRoot));
            stage.setOnCloseRequest(event -> {
                controller.handleClose();
            });
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSearchFields(Customer customer){
        cmbSearchTitle.getSelectionModel().clearSelection();
        cmbSearchTitle.setValue(null);

        txtSearchID.setText(customer.getId());
        cmbSearchTitle.setValue(customer.getTitle());
        txtSearchName.setText(customer.getName());
        dateSearchDOB.setValue(customer.getDob());
        txtSearchContactNum.setText(String.valueOf(customer.getContactNum()));
        txtSearchProfession.setText(customer.getProfession());
        txtSearchAddress.setText(customer.getAddress());
        txtSearchCity.setText(customer.getCity());
    }

    public void clearFields(){
        txtAddID.setText(null);
        cmbAddTitle.setValue(null);
        txtAddName.setText(null);
        dateAddDOB.setValue(null);
        txtAddContactNum.setText(null);
        txtAddProfession.setText(null);
        txtAddAddress.setText(null);
        txtAddCity.setText(null);
    }

    public void clearSearchFields(){
        txtSearch.setText(null);
        txtSearchID.setText(null);
        cmbSearchTitle.setValue(null);
        txtSearchName.setText(null);
        dateSearchDOB.setValue(null);
        txtSearchContactNum.setText(null);
        txtSearchProfession.setText(null);
        txtSearchAddress.setText(null);
        txtSearchCity.setText(null);
    }
}
