package Model;

import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class Master {

    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static ObservableList<Task> allTasks = FXCollections.observableArrayList();

    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }

    public static void setAllUsers(ObservableList<User> allUsers) {
        Master.allUsers = allUsers;
    }

    public static void addUser(User user) {
        allUsers.add(user);
    }

    public static void deleteUser(User user) {
        allUsers.remove(user);
    }

    public static void deleteAllUsers() {
        allUsers.clear();
    }

    public static ObservableList<Task> getAllTasks() {
        return allTasks;
    }

    public static void setAllTasks(ObservableList<Task> allTasks) {
        Master.allTasks = allTasks;
    }

    public static void addTask(Task task) {
        allTasks.add(task);
    }

    public static void deleteTask(Task task) {
        allTasks.remove(task);
    }

    public static void deleteAllTasks() {
        allTasks.clear();
    }
        
    public Optional<ButtonType> showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Image image;
        if (null == type) {
            image = new Image("/Model/error48.png");
        } else {
            switch (type.toString()) {
                case "INFORMATION":
                    image = new Image("/Model/information48.png");
                    break;
                case "CONFIRMATION":
                    image = new Image("/Model/confirmation48.png");
                    break;
                case "ERROR":
                    image = new Image("/Model/error48.png");
                    break;
                default:
                    image = new Image("/Model/error48.png");
                    break;
            }
        }
        
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/Model/listChecks24.png"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK Button Selected");
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            System.out.println("CANCEL Button Selected");
        }
        return result;
    }

}
