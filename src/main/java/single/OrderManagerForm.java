package single;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderManagerForm {

    private static OrderManagerForm instance;
    private Stage stage;

    private OrderManagerForm(){
        stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/order_manager_form.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Order Manager Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static OrderManagerForm getInstance(){
        if (instance == null){
            instance = new OrderManagerForm();
        }
        return instance;
    }

    public void show(){
        stage.show();
    }
}
