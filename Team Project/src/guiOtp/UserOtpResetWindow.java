package guiOtp;

import applicationMain.FoundationsMain;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * User flow: enter username + OTP → if valid, must set a new password.
 * On success, OTP is marked used and user must login again with the new password.
 */
public class UserOtpResetWindow {

    public static void show(Stage owner) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.WINDOW_MODAL);
        if (owner != null) dlg.initOwner(owner);
        dlg.setTitle("One-Time Code Login → Reset Password");

        // Step 1: username + OTP
        Label uLbl = new Label("Username:");
        TextField userTf = new TextField();

        Label oLbl = new Label("One-time code:");
        TextField otpTf = new TextField();
        otpTf.setPromptText("6 digits");

        Button verifyBtn = new Button("Verify OTP");
        Button closeBtn = new Button("Close");

        VBox step1 = new VBox(8, uLbl, userTf, oLbl, otpTf, new HBox(10, verifyBtn, closeBtn));
        step1.setPadding(new Insets(14));

        // Step 2: new password
        Label np1Lbl = new Label("New password:");
        PasswordField np1 = new PasswordField();
        Label np2Lbl = new Label("Confirm password:");
        PasswordField np2 = new PasswordField();
        Button setBtn = new Button("Set New Password");
        Button cancelBtn = new Button("Cancel");

        VBox step2 = new VBox(8, np1Lbl, np1, np2Lbl, np2, new HBox(10, setBtn, cancelBtn));
        step2.setPadding(new Insets(14));
        step2.setVisible(false);
        step2.setManaged(false);

        VBox root = new VBox(8, step1, new Separator(), step2);
        Scene scene = new Scene(root, 380, 260);
        dlg.setScene(scene);

        verifyBtn.setOnAction(ev -> {
            String u = userTf.getText().trim();
            String code = otpTf.getText().trim();
            if (u.isEmpty() || code.isEmpty()) {
                error("Provide both username and code.");
                return;
            }
            boolean ok = FoundationsMain.database.loginWithOtp(u, code);
            if (!ok) {
                error("Invalid or expired code (or no active OTP).");
                return;
            }
            info("OTP accepted. You must set a new password now.");
            step2.setVisible(true);
            step2.setManaged(true);
        });

        setBtn.setOnAction(ev -> {
            String p1 = np1.getText();
            String p2 = np2.getText();
            if (!p1.equals(p2)) {
                error("Passwords do not match.");
                return;
            }
            boolean ok = FoundationsMain.database.completePasswordReset(p1);
            if (!ok) {
                error("Failed to set password. Make sure it meets policy:\n" +
                      "8–32 chars, upper + lower + digit + special, no spaces.");
                return;
            }
            info("Password updated. Please login again using your new password.");
            dlg.close();
        });

        closeBtn.setOnAction(ev -> dlg.close());
        cancelBtn.setOnAction(ev -> dlg.close());

        dlg.show();
    }

    private static void info(String m){ new Alert(Alert.AlertType.INFORMATION, m, ButtonType.OK).showAndWait(); }
    private static void error(String m){ new Alert(Alert.AlertType.ERROR, m, ButtonType.OK).showAndWait(); }
}
