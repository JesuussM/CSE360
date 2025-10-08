package guiOtp;

import applicationMain.FoundationsMain;
import database.Database;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewOtpReset {

    private static double width = 420;
    private static double height = 260;

    public static void display(Stage owner) {
        final Database db = FoundationsMain.database;

        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Reset Password with Code");

        Pane root = new Pane();
        Scene scene = new Scene(root, width, height);
        dialog.setScene(scene);

        Label lblUser = new Label("Username:");
        lblUser.getStyleClass().addAll("text-bold");
        lblUser.setFont(Font.font("Arial", 14));
        lblUser.setLayoutX(24); lblUser.setLayoutY(24);

        TextField tfUser = new TextField();
        tfUser.setFont(Font.font("Arial", 14));
        tfUser.setPromptText("e.g., vansh123");
        tfUser.setLayoutX(140); tfUser.setLayoutY(20);
        tfUser.setPrefWidth(240);

        Label lblCode = new Label("One-Time Code:");
        lblCode.getStyleClass().addAll("text-bold");
        lblCode.setFont(Font.font("Arial", 14));
        lblCode.setLayoutX(24); lblCode.setLayoutY(66);

        TextField tfCode = new TextField();
        tfCode.setFont(Font.font("Arial", 14));
        tfCode.setPromptText("paste code here");
        tfCode.setLayoutX(140); tfCode.setLayoutY(62);
        tfCode.setPrefWidth(240);

        Label lblNew = new Label("New Password:");
        lblNew.getStyleClass().addAll("text-bold");
        lblNew.setFont(Font.font("Arial", 14));
        lblNew.setLayoutX(24); lblNew.setLayoutY(108);

        PasswordField pfNew = new PasswordField();
        pfNew.setFont(Font.font("Arial", 14));
        pfNew.setPromptText("new password");
        pfNew.setLayoutX(140); pfNew.setLayoutY(104);
        pfNew.setPrefWidth(240);

        Label lblConfirm = new Label("Confirm:");
        lblConfirm.getStyleClass().addAll("text-bold");
        lblConfirm.setFont(Font.font("Arial", 14));
        lblConfirm.setLayoutX(24); lblConfirm.setLayoutY(150);

        PasswordField pfConfirm = new PasswordField();
        pfConfirm.setFont(Font.font("Arial", 14));
        pfConfirm.setPromptText("confirm password");
        pfConfirm.setLayoutX(140); pfConfirm.setLayoutY(146);
        pfConfirm.setPrefWidth(240);

        Button btnReset = new Button("Reset Password");
        btnReset.getStyleClass().addAll("success");
        btnReset.setFont(Font.font("Dialog", 14));
        btnReset.setLayoutX(140); btnReset.setLayoutY(190);
        btnReset.setPrefWidth(160);

        Button btnClose = new Button("Close");
        btnClose.getStyleClass().addAll("danger", "button-outlined");
        btnClose.setFont(Font.font("Dialog", 14));
        btnClose.setLayoutX(310); btnClose.setLayoutY(190);
        btnClose.setPrefWidth(70);
        btnClose.setOnAction(e -> dialog.close());

        btnReset.setOnAction(e -> {
            String username = tfUser.getText().trim();
            String code = tfCode.getText().trim();
            String np = pfNew.getText();
            String cp = pfConfirm.getText();

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Reset with Code");

            if (username.isEmpty() || code.isEmpty() || np.isEmpty() || cp.isEmpty()) {
                a.setHeaderText("Missing data");
                a.setContentText("Please fill in all fields.");
                a.showAndWait();
                return;
            }
            if (!np.equals(cp)) {
                a.setHeaderText("Password mismatch");
                a.setContentText("New password and confirm must match.");
                a.showAndWait();
                return;
            }

            // 🔒 Enforce password policy
            if (!db.isValidPasswordPolicy(np)) {
                a.setHeaderText("Invalid Password");
                a.setContentText("Password must be 8–32 chars, include uppercase, lowercase, digit, special character, and no spaces.");
                a.showAndWait();
                return;
            }

            Database.OtpValidation v = db.validateOtpCode(username, code);
            if (!v.isValid) {
                a.setHeaderText("Invalid / expired code");
                a.setContentText(v.message);
                a.showAndWait();
                return;
            }

            boolean ok = db.resetPasswordWithOtp(username, code, np);
            if (!ok) {
                a.setHeaderText("Reset failed");
                a.setContentText("Could not reset the password. Try a new code.");
                a.showAndWait();
                return;
            }

            a.setHeaderText("Success");
            a.setContentText("Password updated. You can now log in with your new password.");
            a.showAndWait();
            dialog.close();
        });

        root.getChildren().addAll(
            lblUser, tfUser,
            lblCode, tfCode,
            lblNew, pfNew,
            lblConfirm, pfConfirm,
            btnReset, btnClose
        );

        dialog.showAndWait();
    }
}
