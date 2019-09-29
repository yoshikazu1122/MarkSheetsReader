package controller08;

import com.springfire.jp.Main;

import javafx.fxml.FXML;

public class Controller08 {
	@FXML public void handlePrev() {
		Main.getInstance().prevPage(0);
	}

	@FXML public void handleNext() {
		Main.getInstance().nextPage(0);
	}
}
