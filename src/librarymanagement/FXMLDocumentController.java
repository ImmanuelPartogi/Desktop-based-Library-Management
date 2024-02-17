package librarymanagement;

import java.sql.SQLIntegrityConstraintViolationException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import librarymanagement.user.getData;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

public class FXMLDocumentController {
    private Alert alert;

    @FXML
    private ChoiceBox<String> registrationSelectGender;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private TextField studentNumber;

    @FXML
    private PasswordField password;

    @FXML
    private Button login_Btn;

    @FXML
    private Button switch_login;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Label selectedImageLabel;

    @FXML
    private Button choosePictureBtn;

    @FXML
    private AnchorPane registrationForm;

    @FXML
    private Button registration_Btn;

    @FXML
    private Button switch_registration;

    @FXML
    private Button regClose;

    @FXML
    private Button regMinimize;

    @FXML
    private TextField regStudentNumber;

    @FXML
    private TextField regFullName;

    @FXML
    private TextField regNoWhatsapp;

    @FXML
    private PasswordField regPassword;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private double x = 0;
    private double y = 0;

    @FXML
    public void initialize() {
        // Set the initial view to the login form
        showLoginForm();

        // Inisialisasi elemen ChoiceBox
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Male", "Female", "Other", "Prefer not to say");
        registrationSelectGender.setItems(genderOptions);
        
        // Set default value
        registrationSelectGender.setValue("Select Gender");
    }

    private void showLoginForm() {
        loginForm.setVisible(true);
        registrationForm.setVisible(false);
    }

    @FXML
    void exit() {
        System.exit(0);
    }

    @FXML
    void minimize() {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void login() throws IOException {
        String sql = "SELECT * FROM anggota WHERE studentNumber = ? AND password = ?";

        connect = Database.connectDB();

        try {
            String enteredStudentNumber = studentNumber.getText();
            String enteredPassword = password.getText();

            Alert alert;

            if (enteredStudentNumber.isEmpty() || enteredPassword.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields.");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, enteredStudentNumber);
                prepare.setString(2, enteredPassword);
                result = prepare.executeQuery();

                if (result.next()) {
                    String role = result.getString("role");
                    String gender = result.getString("gender");

                    if ("admin".equals(role)) {
                        // Code untuk user
                        getData.studentNumber = enteredStudentNumber;
                        getData.path = result.getString("image");

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Admin Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login!");
                        alert.showAndWait();

                        // Code untuk admin
                        Parent root = FXMLLoader.load(getClass().getResource("/librarymanagement/admin/dashboardAdmin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        // Attach event handler for window dragging
                        root.setOnMousePressed((MouseEvent mouseEvent) -> {
                            x = mouseEvent.getSceneX();
                            y = mouseEvent.getSceneY();
                        });

                        root.setOnMouseDragged((MouseEvent mouseEvent) -> {
                            stage.setX(mouseEvent.getScreenX() - x);
                            stage.setY(mouseEvent.getScreenY() - y);
                        });

                        stage.initStyle(StageStyle.TRANSPARENT);
                        stage.setScene(scene);
                        stage.show();

                        // Menutup halaman login
                        ((Stage) login_Btn.getScene().getWindow()).close();

                    } else if ("user".equals(role)) {
                        // Code untuk user
                        getData.studentNumber = enteredStudentNumber;
                        getData.path = result.getString("image");

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Admin Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Login!");
                        alert.showAndWait();

                        // Open the dashboard window
                        Parent root = FXMLLoader.load(getClass().getResource("/librarymanagement/user/dashboardUser.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);

                        // Attach event handler for window dragging
                        root.setOnMousePressed((MouseEvent mouseEvent) -> {
                            x = mouseEvent.getSceneX();
                            y = mouseEvent.getSceneY();
                        });

                        root.setOnMouseDragged((MouseEvent mouseEvent) -> {
                            stage.setX(mouseEvent.getScreenX() - x);
                            stage.setY(mouseEvent.getScreenY() - y);
                        });

                        stage.initStyle(StageStyle.TRANSPARENT);
                        stage.setScene(scene);
                        stage.show();

                        // Menutup halaman login
                        ((Stage) login_Btn.getScene().getWindow()).close();
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Admin Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username or Password.");
                    alert.showAndWait();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void chooseProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            selectedImageLabel.setText("Selected Image: " + selectedFile.getName());
            getData.path = imagePath; // Update the path variable with the selected image path
            System.out.println("Selected Image Path: " + imagePath);
        } else {
            System.out.println("No image selected.");
            selectedImageLabel.setText("");
        }
    }

    @FXML
    void registration() {
        String sql = "INSERT INTO anggota (studentNumber, fullName, noWhatsapp, gender, password, image, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        connect = Database.connectDB();

        try {
            String enteredStudentNumber = regStudentNumber.getText();
            String enteredFullName = regFullName.getText();
            String enteredNoWhatsapp = regNoWhatsapp.getText();
            String enteredPassword = regPassword.getText();

            Alert alert;

            if (enteredStudentNumber.isEmpty() || enteredPassword.isEmpty() || getData.path == null || getData.path.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields and select a profile picture.");
                alert.showAndWait();
            } else if (enteredStudentNumber.length() < 8) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Student number must be at least 8 characters.");
                alert.showAndWait();
            } else if (!isValidPassword(enteredPassword)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Password must contain an uppercase letter, a lowercase letter, and a digit, and should be at least 8 characters long.");
                alert.showAndWait();
            } else if (enteredNoWhatsapp.length() < 12) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("No Whatsapp must be at least 12 characters.");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, enteredStudentNumber);
                prepare.setString(2, enteredFullName);
                prepare.setString(3, enteredNoWhatsapp);
                prepare.setString(4, registrationSelectGender.getValue());
                prepare.setString(5, enteredPassword);
                // Assuming 'path' is the file path selected during registration
                prepare.setString(6, getData.path);
                prepare.setString(7, "user"); // Default role for registration is user

                int rowsAffected = prepare.executeUpdate();

                if (rowsAffected > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Admin Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration successful!");
                    alert.showAndWait();

                    // Redirect to the login form after successful registration
                    switchToLogin();

                    // Optional: Clear the registration form fields
                    regStudentNumber.clear();
                    regFullName.clear();
                    regNoWhatsapp.clear();
                    regPassword.clear();
                    selectedImageLabel.setText("");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Admin Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Registration failed. Please try again.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            // Handle duplicate entry error
            alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Admin Message");
            alert.setHeaderText(null);
            alert.setContentText("Registration failed. Student number already exists.");
            alert.showAndWait();
        } else {
            // Handle other SQL exceptions
            e.printStackTrace();
        }
        } finally {
            try {
                if (prepare != null) {
                    prepare.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void switchToRegistration() {
        loginForm.setVisible(false);
        registrationForm.setVisible(true);
    }

    @FXML
    void switchToLogin() {
        registrationForm.setVisible(false);
        loginForm.setVisible(true);
    }

    @FXML
    void numbersOnly(KeyEvent event) {
        if (event.getCharacter().matches("[^\\e\t\r\\d+$]")) {
            event.consume();
            studentNumber.setStyle("-fx-border-color:#e04040");
        } else {
            studentNumber.setStyle("-fx-border-color:#fff");
        }
    }

    private boolean isValidPassword(String password) {
        // Password must contain an uppercase letter, a lowercase letter, and a digit
        // The length must be greater than or equal to 8
        return password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*") && password.length() >= 8;
    }
}
