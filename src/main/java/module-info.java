module org.example.pixel_art_generator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pixel_art_generator to javafx.fxml;
    exports org.example.pixel_art_generator;
}