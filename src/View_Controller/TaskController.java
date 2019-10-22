package View_Controller;

import Model.DBConnection;
import Model.MYSQL;
import Model.Master;
import Model.Task;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.filechooser.FileSystemView;

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
    private ChoiceBox<String> sortExportByChoiceBox;
    @FXML
    private Button reportTxtButton;
    @FXML
    private Button reportCsvButton;
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

    Master m = new Master();

    final Tooltip scopeTooltip = new Tooltip();
    final Tooltip severityTooltip = new Tooltip();
    final Tooltip readOnlyTooltip = new Tooltip();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Set Tooltips
        readOnlyTooltip.setText("Read Only");
        taskIdTextField.setTooltip(readOnlyTooltip);
        userIdTextField.setTooltip(readOnlyTooltip);
        priorityTextField.setTooltip(readOnlyTooltip);

        scopeTooltip.setText("1.0 = Isolated, 2.0 = Pattern, 3.0 = Widespread");
        scopeChoiceBox.setTooltip(scopeTooltip);

        severityTooltip.setText("1.0 = Low, 2.0 = Med, 3.0 = High");
        severityChoiceBox.setTooltip(severityTooltip);

        // Initialize Choiceboxes
        // Scope
        scopeChoiceBox.getItems().addAll(1.0, 2.0, 3.0);

        // Severity
        severityChoiceBox.getItems().addAll(1.0, 2.0, 3.0);

        // SortExportByChoiceBox
        sortExportByChoiceBox.getItems().addAll("Id", "Name", "Description", "Due Date", "Scope", "Severity", "Priority", "User Id", "Username");

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
                    System.out.println("Username changed: " + newV);
                }
                );

        scopeChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, newV) -> {
                    try {
                        priorityTextField.setText(String.valueOf(setPriority(scopeChoiceBox.getValue(), severityChoiceBox.getValue())));
                    } catch (Exception e) {
//                        priorityTextField.setText(null);
                    }
                    System.out.println("Scope changed: " + newV);
                }
                );

        severityChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, newV) -> {
                    try {
                        priorityTextField.setText(String.valueOf(setPriority(scopeChoiceBox.getValue(), severityChoiceBox.getValue())));
                    } catch (Exception e) {
//                        priorityTextField.setText(null);
                    }
                    System.out.println("Severity changed: " + newV);
                }
                );
//        setPriority(scopeChoiceBox.getValue(),severityChoiceBox.getValue());
    }

    private void getUserIdFromUsername(String username) {

        ObservableList<ArrayList> result = null;
        try {
            String sql = "SELECT id FROM user WHERE username = '" + username + "';";
            result = new MYSQL().query(sql);

            for (int r = 0; r < result.size(); r++) {
                // Add each item to the usernameChoiceBox
                String uid = String.valueOf(result.get(r).get(0));
                userIdTextField.setText(uid);
            }

            System.out.println(result);
        } catch (Exception ex) {
            System.out.println("Error");
        }

    }

    @FXML
    private void addButtonAction(ActionEvent event) throws Exception {
        // Checks for null and blanks
        // Checks for "too long" data fields

        boolean isNameBlank = nameTextField.getText() == null || nameTextField.getText().trim().isEmpty();
        boolean isDueDateBlank = dueDateDatePicker.getValue() == null;
        boolean isScopeBlank = scopeChoiceBox.getValue() == null;
        boolean isSeverityBlank = severityChoiceBox.getValue() == null;

        if (isNameBlank || isDueDateBlank || isScopeBlank || isSeverityBlank) {
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Please do not leave any field blank.", Alert.AlertType.ERROR);
            System.out.println("Invalid Data.");
        } else if (nameTextField.getText().trim().length() > 255 || descriptionTextArea.getText().trim().length() > 255) {
// Checks each field for length requirements
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Data is too long.", Alert.AlertType.ERROR);
            System.out.println("Data too long.");
        } else {
            String name = nameTextField.getText().trim().toLowerCase();
            String description = descriptionTextArea.getText().trim().toLowerCase();
            String stringDueDate = dueDateDatePicker.getValue().toString();
            DateFormat dueDateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDueDate = dueDateDateFormat.parse(stringDueDate);
            java.sql.Date sqlDueDate = new java.sql.Date(dateDueDate.getTime());

            double scope = scopeChoiceBox.getValue();
            double severity = severityChoiceBox.getValue();
            double priority = Double.parseDouble(priorityTextField.getText().trim());

            boolean isUserIdBlank = userIdTextField.getText() == null || userIdTextField.getText().trim().isEmpty();

            if (isUserIdBlank) {
                try {
                    System.out.println("Starting Insert...");

                    String q = "INSERT INTO task (name, description, dueDate, scope, severity, priority) VALUES (?,?,?,?,?,?);";
                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement(q);

                        ps.setString(1, name);
                        ps.setString(2, description);
                        ps.setDate(3, sqlDueDate);
                        ps.setDouble(4, scope);
                        ps.setDouble(5, severity);
                        ps.setDouble(6, priority);

                        ps.execute();
                        DBConnection.closeConnection();
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                int uid = Integer.parseInt(userIdTextField.getText().trim());

                try {
                    System.out.println("Starting Insert...");

                    String q = "INSERT INTO task (name, description, dueDate, scope, severity, priority) VALUES (?,?,?,?,?,?,?);";
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
                        DBConnection.closeConnection();
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }

            // Clears intake form
            clearForm();

            // Rebuilt TableView
            clearAndRebuildTableView(false);

            // Get next AI PK for User
            getAIForTask();
        }
    }

    @FXML
    private void updateButtonAction(ActionEvent event) throws ParseException, Exception {
        // Checks for null and blanks
        // Checks for "too long" data fields

        boolean isNameBlank = nameTextField.getText() == null || nameTextField.getText().trim().isEmpty();
        boolean isDueDateBlank = dueDateDatePicker.getValue() == null;
        boolean isScopeBlank = scopeChoiceBox.getValue() == null;
        boolean isSeverityBlank = severityChoiceBox.getValue() == null;

        if (isNameBlank || isDueDateBlank || isScopeBlank || isSeverityBlank) {
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Please do not leave any field blank.", Alert.AlertType.ERROR);
            System.out.println("Invalid Data.");
        } else if (nameTextField.getText().trim().length() > 255 || descriptionTextArea.getText().trim().length() > 255) {
// Checks each field for length requirements
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Data is too long.", Alert.AlertType.ERROR);
            System.out.println("Data too long.");
        } else {
            int id = Integer.parseInt(taskIdTextField.getText().trim());
            String name = nameTextField.getText().trim();
            String description = descriptionTextArea.getText().trim();
            String stringDueDate = dueDateDatePicker.getValue().toString();
            DateFormat dueDateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDueDate = dueDateDateFormat.parse(stringDueDate);
            java.sql.Date sqlDueDate = new java.sql.Date(dateDueDate.getTime());

            double scope = scopeChoiceBox.getValue();
            double severity = severityChoiceBox.getValue();
            double priority = Double.parseDouble(priorityTextField.getText().trim());

            String sUid = userIdTextField.getText();

            if (sUid == null) {
                try {
                    System.out.println("Starting Update (uid null)...");

                    String q = "UPDATE task SET name = ?, description = ?, dueDate = ?, scope = ?, severity = ?, priority = ? WHERE id = ?;";
                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement(q);

                        ps.setString(1, name);
                        ps.setString(2, description);
                        ps.setDate(3, sqlDueDate);
                        ps.setDouble(4, scope);
                        ps.setDouble(5, severity);
                        ps.setDouble(6, priority);
                        ps.setInt(7, id);

                        ps.execute();
                        DBConnection.closeConnection();
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                try {
                    System.out.println("Starting Update (uid not null)...");

                    int uid = Integer.parseInt(userIdTextField.getText().trim());

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
                        DBConnection.closeConnection();
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }

            // Clears intake form
            clearForm();

            // Rebuilt TableView
            clearAndRebuildTableView(false);
        }
    }

    @FXML
    private void deleteButtonAction(ActionEvent event) throws Exception {
        try {
            // Will catch if there is no item currently selected
            taskTableView.getSelectionModel().getSelectedItem().getId();

            // If there is a valid item, the rest of the function will proceed
            Optional<ButtonType> deleteWarning = m.showAlert("Confirmation", "Are you sure?", "This action will permanently delete the selected task.  Press OK to continue or close this dialog if this was an error.", Alert.AlertType.CONFIRMATION);

            if (deleteWarning.get() == ButtonType.OK) {
                System.out.println("Delete task!");

                int id = Integer.parseInt(taskIdTextField.getText().trim());

                try {
                    System.out.println("Starting Delete...");

                    String q = "DELETE FROM task WHERE id = ?;";
                    try (Connection conn = DBConnection.getConnection()) {
                        PreparedStatement ps = conn.prepareStatement(q);

                        ps.setInt(1, id);

                        ps.execute();
                        DBConnection.closeConnection();
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }

                // Rebuilt TableView
                clearAndRebuildTableView(false);
            }
        } catch (Exception e) {
            Optional<ButtonType> tableViewSelectionError = m.showAlert("Error", "Nothing Selected", "Please select a valid task from the table on the right.", Alert.AlertType.ERROR);
            System.out.println("Invalid Table Selection.");
        }

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
        try {
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

            int uid = selectedTask.getUserId();
            if (uid > 0) {
                userIdTextField.setText(String.valueOf(uid));
            } else {
                userIdTextField.setText(null);
            }

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
        } catch (Exception e) {
            Optional<ButtonType> tableViewSelectionError = m.showAlert("Error", "Nothing Selected", "Please select a valid user from the table on the right.", Alert.AlertType.ERROR);
            System.out.println("Invalid Table Selection.");
        }
    }

    @FXML
    private void reportTxtButtonAction() throws IOException {
        System.out.println("Starting TXT Report");
        Optional<ButtonType> reportTxtNotification = m.showAlert("Exporting", "Generating TXT Report", "Generating a report.  You can find it in your default home directory with the name TaskExportTXT.txt.\nPress OK to continue.  Close this dialog or select CANCEL to abort.", Alert.AlertType.CONFIRMATION);
        if (reportTxtNotification.get() == ButtonType.OK) {
            createReportFile("TaskExportTXT", true);
            System.out.println("Finished TXT Report");
        }
    }

    @FXML
    private void reportCsvButtonAction() throws IOException {
        System.out.println("Starting CSV Report");
        Optional<ButtonType> reportCsvNotification = m.showAlert("Exporting", "Generating CSV Report", "Generating a report.  You can find it in your default home directory with the name TaskExportCSV.csv.\nPress OK to continue.  Close this dialog or select CANCEL to abort.", Alert.AlertType.CONFIRMATION);
        if (reportCsvNotification.get() == ButtonType.OK) {
            createReportFile("TaskExportCSV", false);
            System.out.println("Finished CSV Report");
        }
    }

    private double setPriority(double inputScope, double inputSeverity) {

        double priorityScore = Double.sum(inputScope, inputSeverity);

        if (priorityScore > 5) {
            return 1;
        } else if (priorityScore > 4) {
            return 2;
        } else if (priorityScore > 3) {
            return 3;
        } else if (priorityScore > 2) {
            return 4;
        } else {
            return 5;
        }
    }

    private void createReportFile(String fileName, boolean isTxt) throws IOException {
        String orderBy;
        if (sortExportByChoiceBox.getValue() == null) {
            orderBy = "t.id";
        } else {

            orderBy = sortExportByChoiceBox.getValue();
            switch (orderBy) {
                case "Id":
                    orderBy = "t.id";
                    break;
                case "Name":
                    orderBy = "username";
                    break;
                case "Description":
                    orderBy = "description";
                    break;
                case "Due Date":
                    orderBy = "dueDate";
                    break;
                case "Scope":
                    orderBy = "scope";
                    break;
                case "Severity":
                    orderBy = "severity";
                    break;
                case "Priority":
                    orderBy = "priority";
                    break;
                case "User Id":
                    orderBy = "t.userId";
                    break;
                case "Username":
                    orderBy = "u.username";
                    break;
                default:
                    orderBy = "t.id";
            }
        }

        Writer writer = null;
        try {
            System.out.println("Starting File...");

            FileSystemView filesys = FileSystemView.getFileSystemView();

//            File[] roots = filesys.getRoots();
            String path = filesys.getHomeDirectory().getPath();

            if (isTxt) {
                path = path + "\\" + fileName + ".txt";
            } else {
                path = path + "\\" + fileName + ".csv";
            }

            File file = new File(path);
            writer = new BufferedWriter(new FileWriter(file));

            ObservableList<ArrayList> result = FXCollections.observableArrayList();
            try {
                System.out.println("Starting query...");
                String sql = "SELECT t.id, name, description, dueDate, scope, severity, priority, t.userId, u.username FROM task t LEFT JOIN user u ON (t.userId = u.id) ORDER BY " + orderBy + ";";
                result = new MYSQL().query(sql);

                String headers = "Id, Name, Description, DueDate, Scope, Severity, Priority, UserId, Username" + "\n";
                writer.write(headers);

                for (int r = 0; r < result.size(); r++) {
                    String currentLine = result.get(r).toString() + "\n";
                    currentLine = currentLine.replace("[", "").replace("]", "");
                    System.out.println("Writing line...");
                    writer.write(currentLine);
                }

                System.out.println(result);
            } catch (Exception ex) {
                System.out.println("Error");
            }

        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            writer.flush();
            writer.close();
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
