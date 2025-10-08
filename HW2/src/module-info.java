module FoundationsF25 {
	requires javafx.controls;
	requires java.sql;
	requires javafx.base;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
	opens guiListUsers to javafx.base;
}
