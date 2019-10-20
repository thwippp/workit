package View_Controller;

import Model.MYSQL;
import Model.Master;
import Model.Task;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Button reportButton;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, Integer> taskIdTableColumn;
    @FXML
    private TableColumn<Task, String> nameTableColumn;
    @FXML
    private TableColumn<Task, String> descriptionTableColumn;
    @FXML
    private TableColumn<Task, Date> dueDateTableColumn;
    @FXML
    private TableColumn<Task, Double> scopeTableColumn;
    @FXML
    private TableColumn<Task, Double> severityTableColumn;
    @FXML
    private TableColumn<Task, Double> priorityTableColumn;
    @FXML
    private TableColumn<Task, Integer> userIdTableColumn;
    @FXML
    private TableColumn<Task, String> usernameTableColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        try {
            // Gets next auto increment value for adding another user
            getAIForTask();
        } catch (Exception ex) {
        }

        // Clears All Tasks, Builds new Table View, and Adds Values from DB
        // TRUE == first time, runs the CellValueFactory and PropertyValueFactory bits
        clearAndRebuildTableView(true);

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

    // Gets next auto increment value for adding another task
    private void getAIForTask() throws Exception {
        ObservableList<ArrayList> result = null;
        try {
            String sql = "SELECT auto_increment FROM information_schema.tables WHERE table_name='task'";
            result = new MYSQL().query(sql);

            String tId = result.get(0).get(0).toString();
            taskIdTextField.setText(tId);

            System.out.println(result);
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

    // Clears All Tasks, Builds new Table View, and Adds Values from DB
    private void clearAndRebuildTableView(boolean isOnInitialize) {

        System.out.println("Starting Clear and Rebuild...");

        // Clears all users from the tableview
        Master.deleteAllTasks();

        if (isOnInitialize) {
            // Get list of customers in CustomerScreen
            taskTableView.setItems(Master.getAllTasks());
            taskIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            dueDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            scopeTableColumn.setCellValueFactory(new PropertyValueFactory<>("scope"));
            severityTableColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
            priorityTableColumn.setCellValueFactory(new PropertyValueFactory<>("prirority"));
            userIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        }

        boolean isAdded = false;
        try {
            String sql = "SELECT t.id, name, description, dueDate, scope, severity, priority, t.userId, u.username FROM task t LEFT JOIN user u ON (t.userId = u.id);";
            isAdded = new MYSQL().addTasksFromQuery(sql);
            System.out.println(isAdded);
        } catch (Exception ex) {
            System.out.println("Error");
            System.out.println(ex);
        }

    }

}
