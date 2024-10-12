package single;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ItemManagerForm {

    private static ItemManagerForm instance;
    private Stage stage;

    private ItemManagerForm(){
        stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/item_manager_form.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Item Manager Form");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemManagerForm getInstance(){
        if (instance == null){
            instance = new ItemManagerForm();
        }
        return instance;
    }

    public void show() {
        stage.show();
    }
}
