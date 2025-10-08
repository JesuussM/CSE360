package guiOtp;

import applicationMain.FoundationsMain;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Simple admin dialog to issue a one-time code for any existing username.
 * Returns and shows the plaintext code (the DB stores only a salted hash).
 */
public class AdminIssueOtpWindow {

    public static void show(Stage owner) {
        Stage dlg = new Stage();
        dlg.initModality(Modality.WINDOW_MODAL);
        if (owner != null) dlg.initOwner(owner);
        dlg.setTitle("Issue One-Time Password");

        Label uLbl = new Label("Target username:");
        TextField userTf = new TextField();
        userTf.setPromptText("e.g., vansh123");

        Label ttlLbl = new Label("TTL (minutes):");
        TextField ttlTf = new TextField("20");

        Button issueBtn = new Button("Issue OTP");
        Button closeBtn = new Button("Close");

        HBox btns = new HBox(10, issueBtn, closeBtn);
        issueBtn.getStyleClass().addAll("success");
        closeBtn.getStyleClass().addAll("danger", "button-outlined");
        btns.setPadding(new Insets(10, 0, 0, 0));

        VBox root = new VBox(8, uLbl, userTf, ttlLbl, ttlTf, btns);
        root.setPadding(new Insets(14));
        Scene scene = new Scene(root, 360, 200);
        dlg.setScene(scene);

        issueBtn.setOnAction(ev -> {
            String target = userTf.getText().trim();
            if (target.isEmpty()) {
                alert(Alert.AlertType.ERROR, "Please enter a username.");
                return;
            }
            int ttl = 20;
            try {
                ttl = Integer.parseInt(ttlTf.getText().trim());
            } catch (NumberFormatException ignored) { }

            try {
                String admin = FoundationsMain.database.getCurrentUsername();
                String code = FoundationsMain.database.issueOtpForUser(admin, target, ttl);
                alert(Alert.AlertType.INFORMATION,
                        "One-time code for '" + target + "': " + code +
                        "\n\nShare this with the user (expires in ~" + ttl + " min).");
            } catch (Exception e) {
                alert(Alert.AlertType.ERROR, "Failed to issue OTP:\n" + e.getMessage());
            }
        });

        closeBtn.setOnAction(ev -> dlg.close());
        dlg.show();
    }

    private static void alert(Alert.AlertType t, String m) {
        new Alert(t, m, ButtonType.OK).showAndWait();
    }
}
