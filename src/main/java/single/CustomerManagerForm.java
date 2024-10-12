package single;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerManagerForm {

    private static CustomerManagerForm instance;
    private Stage stage;

    private CustomerManagerForm(){
        stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/customer_manager_form.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Manager Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomerManagerForm getInstance(){
        if (instance == null){
            instance = new CustomerManagerForm();
        }
        return instance;
    }

    public void show() {
        stage.show();
    }
}
