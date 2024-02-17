package librarymanagement.admin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import librarymanagement.Database;
import javafx.scene.input.MouseEvent;
import librarymanagement.user.getData;
import java.sql.Date;
import javafx.scene.control.TableCell;
import javafx.util.Callback;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.Comparator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dashboardAdminController implements Initializable {

    @FXML
    private TableView<availableBooks> availableBooks_tableView;

    @FXML
    private Label availableBooks_title;

    @FXML
    private ImageView availableBooks_imageView;

    @FXML
    private TableColumn<availableBooks, String> col_ab_bookTitle;

    @FXML
    private TableColumn<availableBooks, String> col_ab_author;

    @FXML
    private TableColumn<availableBooks, String> col_ab_bookType;

    @FXML
    private TableColumn<availableBooks, Date> col_ab_publishedDate;

    @FXML
    private TableView<returnBook> availableBooks_tableView11;

    @FXML
    private TableColumn<returnBook, String> col_ab_studentNumber11;

    @FXML
    private TableColumn<returnBook, String> col_ab_bookTitle11;

    @FXML
    private TableColumn<returnBook, String> col_ab_author11;

    @FXML
    private TableColumn<returnBook, String> col_ab_bookType11;

    @FXML
    private TableColumn<returnBook, Date> col_ab_publishedDate11;
    @FXML
    private TableColumn<returnBook, Date> col_ab_checkReturn11;

    @FXML
    private Button minimize;

    @FXML
    private Button bars_btn;

    @FXML
    private Button arrow_btn;

    @FXML
    private AnchorPane nav_form;

    @FXML
    private Circle circle_image;

    @FXML
    private Button availableBooks_btn;

    @FXML
    private Button addBooks_btn;

    @FXML
    private Button returnBooks_btn;

    @FXML
    private Button edit_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane availableBooks_form;

    @FXML
    private AnchorPane halfNav_form;

    @FXML
    private Circle smallCircle_image;

    @FXML
    private Button halfNav_availableBtn;

    @FXML
    private Button halfNav_addBtn;

    @FXML
    private Button halfNav_returnBtn;

    @FXML
    private AnchorPane mainCenter_form;

    @FXML
    private AnchorPane addBooks_form;

    @FXML
    private AnchorPane returnBook_form;

    @FXML
    private TextField add_BookTitle;

    @FXML
    private TextField add_BookAuthor;

    @FXML
    private TextField add_BookType;

    @FXML
    private FontAwesomeIcon edit_icon;

    @FXML
    private ImageView take_imageView;

    private Image image;

    public void addBook() {
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);

        String sql = "INSERT INTO book (bookTitle, author, bookType, image, date) VALUES (?,?,?,?,?)";

        try (Connection connection = Database.connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, add_BookTitle.getText());
            preparedStatement.setString(2, add_BookAuthor.getText());
            preparedStatement.setString(3, add_BookType.getText());

            if (pathValue != null && !pathValue.trim().isEmpty()) {
                // Print path information for debugging
                System.out.println("Selected Image Path: " + pathValue);

                // Use Paths.get to handle file separators correctly
                Path imagePath = Paths.get(pathValue);

                // Set the image path in the database
                preparedStatement.setString(4, imagePath.toString());

                // Display the image using the correct path
                image = new Image(imagePath.toUri().toString(), 127, 162, false, true);
            } else {
                // Handle the case when the path is null or empty
                System.out.println("Selected Image Path is null or empty. Setting a default or handling accordingly.");
                preparedStatement.setNull(4, java.sql.Types.VARCHAR);
            }

            preparedStatement.setDate(5, sqlDate);

            preparedStatement.executeUpdate();

            // Show success message
            showAlert(AlertType.INFORMATION, "Success", null, "Book added successfully!");
            clearInputFields();
            refreshAvailableBooksTable();
        } catch (SQLException e) {

            // Show error message
            showAlert(AlertType.ERROR, "Error", null, "An error occurred while adding the book. Please try again.");
        }
    }

    private void refreshAvailableBooksTable() {
        // Clear existing items in the TableView
        availableBooks_tableView.getItems().clear();

        // Reload the available books data
        availableBooksForm();
    }

    @FXML
    private void handleAddNewBooks() {
        try {
            // Validate input fields before adding a book
            if (validateInput()) {
                // If validation succeeds, proceed to add the book
                System.out.println("Selected Image Path in handleAddNewBooks: " + pathValue);
                addBook();
                showAlert(AlertType.INFORMATION, "Success", null, "Book added successfully!");
                clearInputFields();
            } else {
                System.out.println("Validation failed. Book not added.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", null, "An error occurred while adding the book. Please try again.");
        }
        refreshPage();
    }

    private void refreshPage() {
        // Implementasikan logika untuk merefresh data atau tampilan di sini
        // Misalnya, atur nilai-nilai default atau bersihkan data

        // Contoh: Atur nilai-nilai default
        add_BookTitle.setText("");
        add_BookAuthor.setText("");
        add_BookType.setText("");
        take_imageView.setImage(null);
    }

    private boolean validateInput() {
        String title = add_BookTitle.getText().trim();
        String author = add_BookAuthor.getText().trim();
        String type = add_BookType.getText().trim();

        if (title.isEmpty() || author.isEmpty() || type.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", null, "Please fill in all required fields.");
            return false;
        }

        return true;
    }

    private void clearInputFields() {
        add_BookTitle.clear();
        add_BookAuthor.clear();
        add_BookType.clear();
    }

// Show alert dialog
    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void selectAvailableBooks() {
        if (availableBooks_tableView == null || availableBooks_title == null || availableBooks_imageView == null) {
            return; // Handle the case where table view, label, or imageView is not initialized
        }

        availableBooks bookData = availableBooks_tableView.getSelectionModel().getSelectedItem();

        Image image = bookData.getImage();

        if (image != null) {
            // Set preserveRatio to false and set fitWidth and fitHeight to match availableBooks_imageView dimensions
            availableBooks_imageView.setPreserveRatio(false);
            availableBooks_imageView.setFitWidth(availableBooks_imageView.getFitWidth());
            availableBooks_imageView.setFitHeight(availableBooks_imageView.getFitHeight());

            availableBooks_imageView.setImage(image);
        } else {
            // Handle the case when the Image is null
            System.out.println("Image is null. Setting a default or handling accordingly.");
            availableBooks_imageView.setImage(null);
        }

        availableBooks_title.setText(bookData.getBookTitle());

        // Use the correct variable to retrieve the image path
        String imagePath = bookData.getImagePath();  // Assuming there is a getImagePath() method in your availableBooks class

        // Print for debugging
        System.out.println("Book Title: " + bookData.getBookTitle());
        System.out.println("Image Path from Database: " + imagePath);

        if (imagePath != null && !imagePath.trim().isEmpty()) {
            // Display the image using the correct path
            image = new Image("file:" + imagePath);
            availableBooks_imageView.setImage(image);
        } else {
            // Handle the case when the path is null or empty
            System.out.println("Selected Image Path is null or empty. Setting a default or handling accordingly.");
            availableBooks_imageView.setImage(null);
        }

        getData.takeBookTitle = bookData.getBookTitle();
        getData.savedTitle = bookData.getBookTitle();
        getData.savedAuthor = bookData.getAuthor();
        getData.savedImage = imagePath;
        getData.savedDate = bookData.getDate();
    }

    private void setBookImage(String imagePath) {
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            // Print path information for debugging
            System.out.println("Selected Image Path: " + imagePath);

            // Use Paths.get to handle file separators correctly
            Path imageFilePath = Paths.get(imagePath);

            // Set the image path in the database
            String uri = "file:" + imageFilePath.toString();
            image = new Image(uri, 134, 171, false, true);

            availableBooks_imageView.setImage(image);
        } else {
            // Handle the case when the path is null or empty
            System.out.println("Selected Image Path is null or empty. Setting a default or handling accordingly.");
            availableBooks_imageView.setImage(null);
        }
    }

    @FXML
    void handleButtonClick(ActionEvent event) {
        // Implementasi untuk menangani klik tombol
        System.out.println("Button clicked!");
    }

    @FXML
    void saveBooks(ActionEvent event) {
        // Implementasi untuk menyimpan perubahan pada buku
        System.out.println("Saving book changes...");
    }

    public void insertImage() {
        getData.path = null;
        FileChooser open = new FileChooser();
        open.setTitle("Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image file", "*png", "*jpg"));
        Stage stage = (Stage) nav_form.getScene().getWindow();

        File file = open.showOpenDialog(stage);

        if (file != null) {
            // Tambahkan print statement untuk debug
            System.out.println("Selected Image Path before: " + getData.path);

            image = new Image(file.toURI().toString(), 112, 84, false, true);
            circle_image.setFill(new ImagePattern(image));
            smallCircle_image.setFill(new ImagePattern(image));

            getData.path = file.getAbsolutePath();

            // Tambahkan print statement untuk debug
            System.out.println("Selected Image Path after: " + getData.path);

            changeProfile();
        }
    }

    public void changeProfile() {
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sql = "UPDATE anggota SET image = ? WHERE studentNumber = ?";

        try (Connection connection = Database.connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, uri);
            preparedStatement.setString(2, getData.studentNumber);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProfile() {

        String uri = "file:" + getData.path;

        image = new Image(uri, 112, 84, false, true);
        circle_image.setFill(new ImagePattern(image));
        smallCircle_image.setFill(new ImagePattern(image));

    }

    public void designInserImage() {

        edit_btn.setVisible(false);

        circle_image.setOnMouseEntered((MouseEvent event) -> {

            edit_btn.setVisible(true);

        });

        circle_image.setOnMouseExited((MouseEvent event) -> {

            edit_btn.setVisible(false);

        });

        edit_btn.setOnMouseEntered((MouseEvent event) -> {

            edit_btn.setVisible(true);
            edit_icon.setFill(Color.valueOf("#fff"));

        });

        edit_btn.setOnMousePressed((MouseEvent event) -> {

            edit_btn.setVisible(true);
            edit_icon.setFill(Color.RED);

        });

        edit_btn.setOnMouseExited((MouseEvent event) -> {

            edit_btn.setVisible(false);

        });

    }

    public void sideNavButtonDesign(ActionEvent event) {

        if (event.getSource() == halfNav_availableBtn) {

            availableBooks_form.setVisible(true);
            addBooks_form.setVisible(false);
            returnBook_form.setVisible(false);

            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            addBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_addBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

        } else if (event.getSource() == halfNav_addBtn) {

            availableBooks_form.setVisible(false);
            addBooks_form.setVisible(true);
            returnBook_form.setVisible(false);

            addBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_addBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

        } else if (event.getSource() == halfNav_returnBtn) {

            availableBooks_form.setVisible(false);
            addBooks_form.setVisible(false);
            returnBook_form.setVisible(true);

            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            addBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_addBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

        }

    }

    public void navButtonDesign(ActionEvent event) {

        if (event.getSource() == availableBooks_btn) {

            availableBooks_form.setVisible(true);
            addBooks_form.setVisible(false);
            returnBook_form.setVisible(false);

            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            addBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_addBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

        } else if (event.getSource() == addBooks_btn) {

            availableBooks_form.setVisible(false);
            addBooks_form.setVisible(true);
            returnBook_form.setVisible(false);

            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            addBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_addBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

        } else if (event.getSource() == returnBooks_btn) {

            availableBooks_form.setVisible(false);
            addBooks_form.setVisible(false);
            returnBook_form.setVisible(true);

            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            addBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_addBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

        } else {
            System.out.println("Unknown button clicked");
        }
    }

    private double x = 0;
    private double y = 0;

    public void sliderArrow() {

        TranslateTransition slide = new TranslateTransition();

        slide.setDuration(Duration.seconds(.5));
        slide.setNode(nav_form);
        slide.setToX(-224);

        TranslateTransition slide1 = new TranslateTransition();

        slide1.setDuration(Duration.seconds(.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(-224 + 90);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(.5));
        slide2.setNode(halfNav_form);
        slide2.setToX(0);

        slide.setOnFinished((ActionEvent event) -> {

            arrow_btn.setVisible(false);
            bars_btn.setVisible(true);

        });

        slide2.play();
        slide1.play();
        slide.play();

    }

    public void sliderBars() {

        TranslateTransition slide = new TranslateTransition();

        slide.setDuration(Duration.seconds(.5));
        slide.setNode(nav_form);
        slide.setToX(0);

        TranslateTransition slide1 = new TranslateTransition();

        slide1.setDuration(Duration.seconds(.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(0);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(.5));
        slide2.setNode(halfNav_form);
        slide2.setToX(-77);

        slide.setOnFinished((ActionEvent event) -> {

            arrow_btn.setVisible(true);
            bars_btn.setVisible(false);

        });

        slide2.play();
        slide1.play();
        slide.play();
    }

    @FXML
    public void logout(ActionEvent event) {
        try {
            if (event.getSource() == logout_btn) {
                // TO SWAP FROM DASHBOARD TO LOGIN FORM
                Parent root = FXMLLoader.load(getClass().getResource("/librarymanagement/FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent e) -> {
                    x = e.getSceneX();
                    y = e.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent e) -> {

                    stage.setX(e.getScreenX() - x);
                    stage.setY(e.getScreenY() - y);

                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();

                logout_btn.getScene().getWindow().hide();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {

        System.exit(0);

    }

    public void minimize() {

        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        designInserImage();

        showProfile();

        returnBookForm();
        // Set cell value factories for other columns
        col_ab_author11.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_ab_bookType11.setCellValueFactory(new PropertyValueFactory<>("bookType"));
        col_ab_publishedDate11.setCellValueFactory(new PropertyValueFactory<>("dateType"));
        col_ab_checkReturn11.setCellValueFactory(new PropertyValueFactory<>("checkReturn"));

        availableBooksForm();
        // Set cell value factory for colBookTitle
        if (col_ab_bookTitle11 != null) {
            col_ab_bookTitle11.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            // Other column setup code...
        } else {
            System.err.println("colBookTitle is null");
        }

        // Set cell value factories for other columns
        col_ab_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_ab_bookType.setCellValueFactory(new PropertyValueFactory<>("bookType"));
        col_ab_publishedDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Set cell value factory for colBookTitle
        if (col_ab_bookTitle != null) {
            col_ab_bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            // Other column setup code...
        } else {
            System.err.println("colBookTitle is null");
        }
    }

    public class returnBook {

        private String studentNumber;
        private String bookTitle;
        private String author;
        private String bookType;
        private Date dateType;
        private String checkReturn;

        // Constructor without the 'image' parameter
        public returnBook(String studentNumber, String bookTitle, String author, String bookType, Date dateType, String checkReturn) {
            this.studentNumber = studentNumber;
            this.bookTitle = bookTitle;
            this.author = author;
            this.bookType = bookType;
            this.dateType = dateType;
            this.checkReturn = checkReturn;
        }

        public String getStudentNumber() {
            return studentNumber;
        }

        public void setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getBookType() {
            return bookType;
        }

        public void setBookType(String bookType) {
            this.bookType = bookType;
        }

        public Date getDateType() {
            return dateType;
        }

        public void setDateType(Date dateType) {
            this.dateType = dateType;
        }

        public String getCheckReturn() {
            return checkReturn;
        }

        public void setCheckReturn(String checkReturn) {
            this.checkReturn = checkReturn;
        }

        @Override
        public String toString() {
            return "ReturnBook{"
                    + "studentNumber='" + studentNumber + '\''
                    + ", bookTitle='" + bookTitle + '\''
                    + ", author='" + author + '\''
                    + ", bookType='" + bookType + '\''
                    + ", date=" + dateType + '\''
                    + ", checkReturn=" + checkReturn
                    + '}';
        }
    }

    public void returnBookForm() {
        ObservableList<returnBook> returnBooksList = FXCollections.observableArrayList();

        // Update the SQL query to fetch data from the "take" table
        String sql = "SELECT studentNumber, bookTitle, author, bookType, date, checkReturn FROM take";

        try (Connection connection = Database.connectDB(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String studentNumber = resultSet.getString("studentNumber");
                String bookTitle = resultSet.getString("bookTitle");
                String author = resultSet.getString("author");
                String bookType = resultSet.getString("bookType");
                Date dateType = resultSet.getDate("date");
                String checkReturn = resultSet.getString("checkReturn");

                // Create a ReturnBook object and add it to the list
                returnBooksList.add(new returnBook(studentNumber, bookTitle, author, bookType, dateType, checkReturn));
            }

            // Sort the list based on date in descending order
            returnBooksList.sort(Comparator.comparing(returnBook::getDateType).reversed());

            // Print the fetched data to the console for debugging
            System.out.println("Fetched Data:");

            returnBooksList.forEach(book -> System.out.println(book));

            // Set the cell value factories for each column
            col_ab_studentNumber11.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
            col_ab_bookTitle11.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            col_ab_author11.setCellValueFactory(new PropertyValueFactory<>("author"));
            col_ab_bookType11.setCellValueFactory(new PropertyValueFactory<>("bookType"));
            col_ab_publishedDate11.setCellValueFactory(new PropertyValueFactory<>("dateType"));
            col_ab_checkReturn11.setCellValueFactory(new PropertyValueFactory<>("dateType"));

            // Set the items for the TableView
            availableBooks_tableView11.setItems(returnBooksList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", null, "An error occurred while fetching return books data.");
        }
    }

    public class availableBooks {

        private String bookTitle;
        private String author;
        private String bookType;
        private String imagePath; 
        private String image;
        private Date date;

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public availableBooks(String bookTitle, String author, String bookType, Date date) {
            this.bookTitle = bookTitle;
            this.author = author;
            this.bookType = bookType;
            this.date = date;
        }

        public Image getImage() {
            if (imagePath != null && !imagePath.trim().isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    return new Image(file.toURI().toString());
                } else {
                    System.out.println("Image file does not exist: " + imagePath);
                }
            } else {
                System.out.println("Image path is null or empty.");
            }
            return null;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getBookType() {
            return bookType;
        }

        public void setBookType(String bookType) {
            this.bookType = bookType;
        }

        public Date getdateType() {
            return date;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = java.sql.Date.valueOf(date);
        }

        public void setdateType(Date date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "ReturnBook{"
                    + ", bookTitle='" + bookTitle + '\''
                    + ", author='" + author + '\''
                    + ", bookType='" + bookType + '\''
                    + ", date=" + date + '\''
                    + '}';
        }
    }

    private void availableBooksForm() {
        ObservableList<availableBooks> availableBooksList = FXCollections.observableArrayList();

        // Update the SQL query to fetch data from the "book" table
        String sql = "SELECT bookTitle, author, bookType, date, image FROM book";

        try (Connection connection = Database.connectDB(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String bookTitle = resultSet.getString("bookTitle");
                String author = resultSet.getString("author");
                String bookType = resultSet.getString("bookType");
                Date date = resultSet.getDate("date");
                String imagePath = resultSet.getString("image"); // Retrieve image path from the database

                availableBooks book = new availableBooks(bookTitle, author, bookType, date);
                book.setImagePath(imagePath); // Set the image path

                availableBooksList.add(book);
            }

            // Sort the list based on date before setting it to the TableView
            sortAvailableBooksList(availableBooksList);

            // Set cell value factories for each column
            col_ab_bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            col_ab_author.setCellValueFactory(new PropertyValueFactory<>("author"));
            col_ab_bookType.setCellValueFactory(new PropertyValueFactory<>("bookType"));
            col_ab_publishedDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            col_ab_publishedDate.setCellFactory(getDateCellFactory());

            // Set the items for the TableView
            availableBooks_tableView.setItems(availableBooksList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlertAvailableBooks(Alert.AlertType.ERROR, "Error", null, "An error occurred while fetching available books data.");
        }
    }

    private void sortAvailableBooksList(ObservableList<availableBooks> list) {
        // Sort the list based on date in descending order
        FXCollections.sort(list, Comparator.comparing(availableBooks::getDate).reversed());
    }

    private Callback<TableColumn<availableBooks, Date>, TableCell<availableBooks, Date>> getDateCellFactory() {
        return column -> new TableCell<availableBooks, Date>() {
            private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        };
    }

    // Metode showAlert
    private void showAlertAvailableBooks(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String pathValue;

    @FXML
    private void chooseProfilePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Setelah memilih file, atur gambar ke take_imageView
            Image image = new Image(selectedFile.toURI().toString());
            take_imageView.setImage(image);

            // Agar gambar ditampilkan penuh
            take_imageView.setPreserveRatio(false);

            // Set pathValue ke path file yang dipilih
            pathValue = selectedFile.getAbsolutePath();
            System.out.println("Selected Image Path: " + pathValue);
        }
    }

}
