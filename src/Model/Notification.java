package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 *
 * @author Schaffeld, B. (000790777);
 */
public class Notification extends Alert {

    private String title;
    private String header;
    private String content;
    private AlertType type;
    private ButtonType buttonType;
    private static final String STYLESHEET = Notification.class.getResource("/View_Controller/Style.css").toExternalForm();

    public Notification(String title, String header, String content, AlertType alertType) {
        super(alertType);
        super.getDialogPane().getStylesheets().add(STYLESHEET);
        this.title = title;
        this.header = header;
        this.content = content;
        this.type = type;

        showNotification();
    }

//    public Notification(String title, String header, String content, AlertType type) {
//        this.title = title;
//        this.header = header;
//        this.content = content;
//        this.type = type;
//
//        showNotification();
//    }
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public ButtonType getButtonType() {
        return buttonType;
    }

    public void setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
    }

    public void showNotification() {

        Image image;
        if (null == this.getType()) {
            image = new Image("/Model/error48.png");
        } else {
            switch (this.getType()) {
                case INFORMATION:
                    image = new Image("/Model/information48.png");
                    setButtonType(ButtonType.OK);
                    break;
                case CONFIRMATION:
                    image = new Image("/Model/confirmation48.png");
                    setButtonType(ButtonType.OK);
                    break;
                case ERROR:
                    image = new Image("/Model/error48.png");
                    setButtonType(ButtonType.OK);
                    break;
                default:
                    image = new Image("/Model/error48.png");
                    break;
            }
        }

        Alert alert = new Alert(this.getType());
        alert.setTitle(this.getTitle());
        alert.setHeaderText(this.getHeader());
        alert.setContentText(this.getContent());

        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/Model/listChecks24.png"));
        alert.showAndWait();

    }
}
