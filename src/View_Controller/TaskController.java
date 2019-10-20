package View_Controller;

import Model.DBConnection;
import Model.MYSQL;
import Model.Master;
import Model.Task;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TablePosition;
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
    private ChoiceBox<String> usernameChoiceBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private DatePicker dueDateDatePicker;
    @FXML
    private ChoiceBox<Double> scopeChoiceBox;
    @FXML
    private ChoiceBox<Double> severityChoiceBox;
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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Scope
        scopeChoiceBox.getItems().addAll(1.0, 2.0, 3.0);

        // Severity
        severityChoiceBox.getItems().addAll(1.0, 2.0, 3.0);

        // Username
        // Get all users from user table
        ObservableList<ArrayList> result = null;
        try {
            String sql = "SELECT username FROM user;";
            result = new MYSQL().query(sql);

            for (int r = 0; r < result.size(); r++) {
                // Add each item to the usernameChoiceBox
                usernameChoiceBox.getItems().add(String.valueOf(result.get(r).get(0)));
            }

            System.out.println(result);
        } catch (Exception ex) {
            System.out.println("Error");
        }

        try {
            // Gets next auto increment value for adding another user
            getAIForTask();
        } catch (Exception ex) {
        }

        // Clears All Tasks, Builds new Table View, and Adds Values from DB
        // TRUE == first time, runs the CellValueFactory and PropertyValueFactory bits
        clearAndRebuildTableView(true);

        usernameChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, newV) -> {
                    getUserIdFromUsername(newV);
                    System.out.println("changed: " + newV);
                }
                );
    }

    private void getUserIdFromUsername(String username) {

        ObservableList<ArrayList> result = null;
        try {
            String sql = "SELECT id FROM user WHERE username = '" + username + "';";
            result = new MYSQL().query(sql);

            for (int r = 0; r < result.size(); r++) {
                // Add each item to the usernameChoiceBox
                userIdTextField.setText(String.valueOf(result.get(r).get(0)));
            }

            System.out.println(result);
        } catch (Exception ex) {
            System.out.println("Error");
        }

    }

    @FXML
    private void addButtonAction(ActionEvent event) throws Exception {
        String name = nameTextField.getText().trim().toLowerCase();
        String description = descriptionTextArea.getText().trim().toLowerCase();
        String stringDueDate = dueDateDatePicker.getValue().toString();
        DateFormat dueDateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateDueDate = dueDateDateFormat.parse(stringDueDate);
        java.sql.Date sqlDueDate = new java.sql.Date(dateDueDate.getTime());

        double scope = scopeChoiceBox.getValue();
        double severity = severityChoiceBox.getValue();
        double priority = Double.parseDouble(priorityTextField.getText().trim());
        int uid = Integer.parseInt(userIdTextField.getText().trim());

        try {
            System.out.println("Starting Insert...");

            String q = "INSERT INTO task (name, description, dueDate, scope, severity, priority, userId) VALUES (?,?,?,?,?,?,?);";
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(q);

                ps.setString(1, name);
                ps.setString(2, description);
                ps.setDate(3, sqlDueDate);
                ps.setDouble(4, scope);
                ps.setDouble(5, severity);
                ps.setDouble(6, priority);
                ps.setInt(7, uid);

                ps.execute();
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        // Rebuilt TableView
        clearAndRebuildTableView(false);

        // Get next AI PK for User
        getAIForTask();
    }

    @FXML
    private void updateButtonAction(ActionEvent event) throws ParseException, Exception {
        int id = Integer.parseInt(taskIdTextField.getText().trim());
        String name = nameTextField.getText().trim().toLowerCase();
        String description = descriptionTextArea.getText().trim().toLowerCase();
        String stringDueDate = dueDateDatePicker.getValue().toString();
        DateFormat dueDateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateDueDate = dueDateDateFormat.parse(stringDueDate);
        java.sql.Date sqlDueDate = new java.sql.Date(dateDueDate.getTime());

        double scope = scopeChoiceBox.getValue();
        double severity = severityChoiceBox.getValue();
        double priority = Double.parseDouble(priorityTextField.getText().trim());
        int uid = Integer.parseInt(userIdTextField.getText().trim());

        try {
            System.out.println("Starting Update...");

            String q = "UPDATE task SET name = ?, description = ?, dueDate = ?, scope = ?, severity = ?, priority = ?, userId = ? WHERE id = ?;";
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(q);

                ps.setString(1, name);
                ps.setString(2, description);
                ps.setDate(3, sqlDueDate);
                ps.setDouble(4, scope);
                ps.setDouble(5, severity);
                ps.setDouble(6, priority);
                ps.setInt(7, uid);
                ps.setInt(8, id);

                ps.execute();
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        // Rebuilt TableView
        clearAndRebuildTableView(false);
    }

    @FXML
    private void deleteButtonAction(ActionEvent event) throws Exception {
        int id = Integer.parseInt(taskIdTextField.getText().trim());

        try {
            System.out.println("Starting Delete...");

            String q = "DELETE FROM task WHERE id = ?;";
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(q);

                ps.setInt(1, id);

                ps.execute();
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        // Rebuilt TableView
        clearAndRebuildTableView(false);

    }

    @FXML
    private void clearButtonAction(ActionEvent event) throws Exception {
        clearForm();

        getAIForTask();

        clearAndRebuildTableView(false);
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
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        taskIdTextField.setText(String.valueOf(selectedTask.getId()));
        nameTextField.setText(selectedTask.getName());
        descriptionTextArea.setText(selectedTask.getDescription());

        // DueDate DatePicker Converting
        java.util.Date jud = new java.util.Date(selectedTask.getDueDate().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(jud);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dueDateDatePicker.setValue(LocalDate.of(year, month, day));

        scopeChoiceBox.setValue(selectedTask.getScope());
        severityChoiceBox.setValue(selectedTask.getSeverity());
        priorityTextField.setText(String.valueOf(selectedTask.getPriority()));
        userIdTextField.setText(String.valueOf(selectedTask.getUserId()));

        TablePosition pos = taskTableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        int col = taskTableView.getColumns().size();
        for (int c = 0; c < taskTableView.getColumns().size(); c++) {
            String cHeader = taskTableView.getColumns().get(c).getText();
            if (cHeader.equals("Username")) {
                col = c;
            }
        }

        String un = null;
        try {
            un = (String) taskTableView.getColumns().get(col).getCellObservableValue(row).getValue();
            usernameChoiceBox.setValue(un);
        } catch (Exception e) {
            System.out.println("Catch...");
            System.out.println(e);
        }
    }

    private void clearForm() {
        taskIdTextField.setText(null);
        nameTextField.setText(null);
        descriptionTextArea.setText(null);
        dueDateDatePicker.setValue(null);
        scopeChoiceBox.setValue(null);
        severityChoiceBox.setValue(null);
        priorityTextField.setText(null);
        userIdTextField.setText(null);
        usernameChoiceBox.setValue(null);
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
            priorityTableColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
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
