package sea.mytunes.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistFormController
{

    // This name matches the fx:id in Scene Builder.
    @FXML
    private TextField txtName;

    @FXML
    private void clickSave()
    {
        // Read the name and remove spaces from its beginning and end.
        String name = txtName.getText().trim();

        if (name.isEmpty())
        {
            new Alert(Alert.AlertType.WARNING, "Please enter a playlist name.")
                    .showAndWait();
            return;
        }

        // Keep the valid name before closing the form.
        savedName = name;
        closeForm();
    }

    // Keep the name only after the user presses Save.
    private String savedName;

    public String getSavedName() {
        return savedName;
    }

    public void fillForm(String playlistName)
    {
        // Show the current playlist name in the text field.
        txtName.setText(playlistName);
    }

    @FXML
    private void clickCancel()
    {
        closeForm();
    }

    private void closeForm()
    {
        // Find the window that contains the name field and close it.
        Stage window = (Stage) txtName.getScene().getWindow();
        window.close();
    }
}