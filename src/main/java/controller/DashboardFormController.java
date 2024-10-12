package controller;

import controller.order.OrderManagerFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import single.CustomerManagerForm;
import single.ItemManagerForm;
import single.OrderManagerForm;

import java.io.IOException;

public class DashboardFormController {

    public Button btnCustomerManager;
    public Button btnItemManager;
    public Button btnOrderManager;

    @FXML
    void btnCustomerManagerOnAction(ActionEvent event) {
        CustomerManagerForm customerManagerForm = CustomerManagerForm.getInstance();
        customerManagerForm.show();
    }

    @FXML
    void btnItemManagerOnAction(ActionEvent event) {
        ItemManagerForm itemManagerForm = ItemManagerForm.getInstance();
        itemManagerForm.show();
    }

    @FXML
    void btnOrderManagerOnAction(ActionEvent event) {
        OrderManagerForm orderManagerForm = OrderManagerForm.getInstance();
        orderManagerForm.show();
    }

}
