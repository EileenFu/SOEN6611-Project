module TVM {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controllers to javafx.graphics, javafx.fxml;

	
	exports application.controllers;
    exports application;
}
