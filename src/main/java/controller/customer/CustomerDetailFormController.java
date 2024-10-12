package controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

public class CustomerDetailFormController {

    @FXML
    private DatePicker dateDisplayDOB;

    @FXML
    private TextField txtDisplayAddress;

    @FXML
    private TextField txtDisplayCity;

    @FXML
    private TextField txtDisplayContactNum;

    @FXML
    private TextField txtDisplayID;

    @FXML
    private TextField txtDisplayName;

    @FXML
    private TextField txtDisplayProfession;

    private Customer customer;

    private CustomerManagerFormController mainController;

    public void setValues(Customer customer) {
        txtDisplayID.setText(customer.getId());
        txtDisplayName.setText(customer.getTitle()+"."+customer.getName());
        dateDisplayDOB.setValue(customer.getDob());
        txtDisplayContactNum.setText(String.valueOf(customer.getContactNum()));
        txtDisplayProfession.setText(customer.getProfession());
        txtDisplayAddress.setText(customer.getAddress());
        txtDisplayCity.setText(customer.getCity());

        this.customer = customer;
    }

    public void setMainController(CustomerManagerFormController mainController) {
        this.mainController = mainController;
    }

    public void handleClose() {
        if (mainController != null) {
            mainController.loadTable();  // Call the method to reload the table
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent){
        mainController.setSearchFields(customer);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        mainController.loadTable();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        if (new CustomerManagerFormController().service.deleteCustomer(txtDisplayID.getText())){
            new Alert(Alert.AlertType.INFORMATION,"Customer Deleted !!").show();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            mainController.loadTable();
            mainController.nextIdGenerator();
        }else{
            new Alert(Alert.AlertType.ERROR,"Customer Not Deleted! Please Try Again").show();
        }
    }


}
