package View_Controller;

import Model.DBConnection;
import Model.MYSQL;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Schaffeld, B. (000790777);
 */
public class LoginController implements Initializable {

    @FXML
    private Text workItText;

    @FXML
    private Text loginText;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // DUMMY DATA
        usernameTextField.setText("test");
        passwordPasswordField.setText("test");
        
    }

    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException, SQLException, Exception {

        // Check user against database
        // Get username
        String username = usernameTextField.getText().trim();

        // Get password
        String password = passwordPasswordField.getText().trim();

        Boolean isAuthenticated = null;

        // Call "callable function"
        Connection conn = DBConnection.getConnection();
        String q = "{call authenticateUser(?,?)}";
        CallableStatement cs = null;
        cs = conn.prepareCall(q);
        cs.setString(1, username);
        cs.setString(2, password);
        ResultSet rs = cs.executeQuery();

        while (rs.next()) {
            isAuthenticated = rs.getBoolean("IsAuthenticated");
        }
        conn.close();

        // Process results
        if (isAuthenticated) {
            System.out.println("User Successfully Authenticated.");

            // Switch scene to MainScreen
            Stage stage;
            Parent root;
            stage = (Stage) loginButton.getScene().getWindow();
            //load up OTHER FXML document
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/View_Controller/Main.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            System.out.println("Incorrect Username or Password. Please try again.");
        }
    }
}
