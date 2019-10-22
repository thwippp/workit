package View_Controller;

import Model.DBConnection;
import Model.MYSQL;
import Model.Master;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Schaffeld, B. (000790777);
 */
public class UserController implements Initializable {

    @FXML
    private TextField userIdTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
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
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, Integer> userIdTableColumn;
    @FXML
    private TableColumn<User, String> firstNameTableColumn;
    @FXML
    private TableColumn<User, String> lastNameTableColumn;
    @FXML
    private TableColumn<User, String> usernameTableColumn;
    @FXML
    private TableColumn<User, String> emailTableColumn;

    Master m = new Master();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // Gets next auto increment value for adding another user
            getAIForUser();
        } catch (Exception ex) {
        }

        // Clears All Users, Builds new Table View, and Adds Values from DB
        // TRUE == first time, runs the CellValueFactory and PropertyValueFactory bits
        clearAndRebuildTableView(true);

    }

    @FXML
    private void addButtonAction(ActionEvent event) throws Exception {
        // Checks for null and blanks
        // Checks for "too long" data fields
        // Checks for only letters in First Name and Last Name fields
        // Checks for  @ and . in email 

        // Checks for null values and blanks
        boolean isUserIdBlank = userIdTextField.getText() == null || userIdTextField.getText().trim().isEmpty();
        boolean isFirstBlank = firstNameTextField.getText() == null || firstNameTextField.getText().trim().isEmpty();
        boolean isLastBlank = lastNameTextField.getText() == null || lastNameTextField.getText().trim().isEmpty();
        boolean isUsernameBlank = usernameTextField.getText() == null || usernameTextField.getText().trim().isEmpty();
        boolean isEmailBlank = emailTextField.getText() == null || emailTextField.getText().trim().isEmpty();

        if (isUserIdBlank || isFirstBlank || isLastBlank || isUsernameBlank || isEmailBlank) {
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Please do not leave any field blank.", Alert.AlertType.ERROR);
            System.out.println("Invalid Data.");
        } else if (String.valueOf(userIdTextField.getText().trim()).length() > 11 || (firstNameTextField.getText().trim().length() > 255 || lastNameTextField.getText().trim().length() > 255 || usernameTextField.getText().trim().length() > emailTextField.getText().length())) {
            // Checks each field for length requirements
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Data is too long.", Alert.AlertType.ERROR);
            System.out.println("Data too long.");
        } else if (!Pattern.matches(".*\\D.*", firstNameTextField.getText()) || !Pattern.matches("[a-z A-Z]+", lastNameTextField.getText())) {
            // Do something
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  The First and Last Name fields can only contain letters.", Alert.AlertType.ERROR);
            System.out.println("Only letters violation.");
        } else if (!Pattern.matches("\\w*@\\w*\\.\\w*", emailTextField.getText())) {
            // Do something
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Email addresses must have both \"@\" and \".\"", Alert.AlertType.ERROR);
            System.out.println("Invalid Email.");
        } else {

            int id = Integer.parseInt(userIdTextField.getText().trim());
            String first = firstNameTextField.getText().trim();
            String last = lastNameTextField.getText().trim();
            String username = usernameTextField.getText().trim();
            String email = emailTextField.getText().trim();

            try {
                System.out.println("Valid Data.");
                System.out.println("Starting Insert...");

                String q = "INSERT INTO user (firstName, lastName, username, email) VALUES (?, ?, ?, ?);";
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement(q);

                    ps.setString(1, first);
                    ps.setString(2, last);
                    ps.setString(3, username);
                    ps.setString(4, email);

                    ps.execute();
                }

            } catch (SQLException ex) {
                System.out.println(ex);
            }

            // Clears intake form
            clearForm();

            // Rebuilt TableView
            clearAndRebuildTableView(false);

            // Get next AI PK for User
            getAIForUser();
        }
    }

    @FXML
    private void updateButtonAction(ActionEvent event) throws Exception {

        // Checks for null values and blanks
        boolean isUserIdBlank = userIdTextField.getText() == null || userIdTextField.getText().trim().isEmpty();
        boolean isFirstBlank = firstNameTextField.getText() == null || firstNameTextField.getText().trim().isEmpty();
        boolean isLastBlank = lastNameTextField.getText() == null || lastNameTextField.getText().trim().isEmpty();
        boolean isUsernameBlank = usernameTextField.getText() == null || usernameTextField.getText().trim().isEmpty();
        boolean isEmailBlank = emailTextField.getText() == null || emailTextField.getText().trim().isEmpty();

        if (isUserIdBlank || isFirstBlank || isLastBlank || isUsernameBlank || isEmailBlank) {
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Please do not leave any field blank.", Alert.AlertType.ERROR);
            System.out.println("Invalid Data.");
        } else if (String.valueOf(userIdTextField.getText().trim()).length() > 11 || (firstNameTextField.getText().trim().length() > 255 || lastNameTextField.getText().trim().length() > 255 || usernameTextField.getText().trim().length() > emailTextField.getText().length())) {
            // Checks each field for length requirements
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Data is too long.", Alert.AlertType.ERROR);
            System.out.println("Data too long.");
        } else if (!Pattern.matches(".*\\D.*", firstNameTextField.getText()) || !Pattern.matches("[a-z A-Z]+", lastNameTextField.getText())) {
            // Do something
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  The First and Last Name fields can only contain letters.", Alert.AlertType.ERROR);
            System.out.println("Only letters violation.");
        } else if (!Pattern.matches("\\w*@\\w*\\.\\w*", emailTextField.getText())) {
            // Do something
            Optional<ButtonType> e = m.showAlert("Error", "Invalid Data", "Sorry.  Email addresses must have both \"@\" and \".\"", Alert.AlertType.ERROR);
            System.out.println("Invalid Email.");
        }
        try {
            System.out.println("Starting Update...");

            int id = Integer.parseInt(userIdTextField.getText().trim());
            String first = firstNameTextField.getText().trim();
            String last = lastNameTextField.getText().trim();
            String username = usernameTextField.getText().trim();
            String email = emailTextField.getText().trim();

            String q = "UPDATE USER SET firstName = ?, lastName = ?, username = ?, email = ? WHERE id = ?;";
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(q);

                ps.setString(1, first);
                ps.setString(2, last);
                ps.setString(3, username);
                ps.setString(4, email);
                ps.setInt(5, id);

                ps.execute();
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        // Clears intake form
        clearForm();

        // Rebuilt TableView
        clearAndRebuildTableView(false);

    }

    @FXML
    private void deleteButtonAction(ActionEvent event) throws Exception {
        Optional<ButtonType> deleteWarning = m.showAlert("Confirmation", "Are you sure?", "This action will permanently delete the selected user.  Press OK to continue or close this dialog if this was an error.", Alert.AlertType.CONFIRMATION);

        if (deleteWarning.get() == ButtonType.OK) {
            System.out.println("Delete user!");

            int id = Integer.parseInt(userIdTextField.getText().trim());

            try {
                System.out.println("Starting Delete...");

                String q = "DELETE FROM user WHERE id = ?;";
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
    }

    @FXML
    private void clearButtonAction(ActionEvent event) throws Exception {
        clearForm();

        getAIForUser();

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
            User selectedUser = userTableView.getSelectionModel().getSelectedItem();
            userIdTextField.setText(String.valueOf(selectedUser.getId()));
            firstNameTextField.setText(selectedUser.getFirstName());
            lastNameTextField.setText(selectedUser.getLastName());
            usernameTextField.setText(selectedUser.getUsername());
            emailTextField.setText(selectedUser.getEmail());
        } catch (Exception e) {
            Optional<ButtonType> tableViewSelectionError = m.showAlert("Error", "Nothing Selected", "Please select a valid user from the table on the right.", Alert.AlertType.ERROR);
            System.out.println("Invalid Table Selection.");
        }
    }

    private void clearForm() {
        userIdTextField.setText(null);
        firstNameTextField.setText(null);
        lastNameTextField.setText(null);
        usernameTextField.setText(null);
        emailTextField.setText(null);
    }

    // Gets next auto increment value for adding another user
    private void getAIForUser() throws Exception {
        ObservableList<ArrayList> result = null;
        try {
            String sql = "SELECT auto_increment FROM information_schema.tables WHERE table_name='user'";
            result = new MYSQL().query(sql);

            String uId = result.get(0).get(0).toString();
            userIdTextField.setText(uId);

            System.out.println(result);
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

    // Clears All Users, Builds new Table View, and Adds Values from DB
    private void clearAndRebuildTableView(boolean isOnInitialize) {

        // Clears all users from the tableview
        Master.deleteAllUsers();

        if (isOnInitialize) {
            // Get list of customers in CustomerScreen
            userTableView.setItems(Master.getAllUsers());
            userIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        }

        boolean isAdded = false;
        try {
            String sql = "SELECT * FROM USER;";
            isAdded = new MYSQL().addUsersFromQuery(sql);
            System.out.println(isAdded);
        } catch (Exception ex) {
            System.out.println("Error");
        }

    }

}
