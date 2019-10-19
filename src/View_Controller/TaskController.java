package View_Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Schaffeld, B. (000790777);
 */
public class TaskController implements Initializable {

    @FXML
    private TextField taskIdTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField userIdTextField;
    @FXML
    private ChoiceBox<?> usernameChoiceBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private DatePicker dueDateDatePicker;
    @FXML
    private ChoiceBox<?> scopeChoiceBox;
    @FXML
    private ChoiceBox<?> severityChoiceBox;
    @FXML
    private TextField priorityTextField;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TableView<?> userTableView;
    @FXML
    private TableColumn<?, ?> taskIdTableColumn;
    @FXML
    private TableColumn<?, ?> nameTableColumn;
    @FXML
    private TableColumn<?, ?> descriptionTableColumn;
    @FXML
    private TableColumn<?, ?> scopeTableColumn;
    @FXML
    private TableColumn<?, ?> severityTableColumn;
    @FXML
    private TableColumn<?, ?> priorityTableColumn;
    @FXML
    private TableColumn<?, ?> userIdTableColumn;
    @FXML
    private TableColumn<?, ?> usernameTableColumn;
    @FXML
    private Button reportButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addButtonAction(ActionEvent event) {
    }

    @FXML
    private void updateButtonAction(ActionEvent event) {
    }

    @FXML
    private void deleteButtonAction(ActionEvent event) {
    }

    @FXML
    private void clearButtonAction(ActionEvent event) {
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) throws IOException {
    Stage stage;
        Parent root;
        stage = (Stage) cancelButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/View_Controller/Main.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void tableViewSelectionAction(MouseEvent event) {
    }
    
}
