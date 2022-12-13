package ui.app.page.user.login;

import exception.BadCredentialException;
import facade.Facade;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import ui.app.State;
import ui.app.component.form.Form;
import ui.app.component.labelfield.LabelField;
import ui.app.component.labelfield.labelfieldmasked.LabelFieldMasked;

import java.net.URL;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Login implements Initializable, Observer {

    @FXML
    private AnchorPane loginPage;

    @FXML
    private Form formController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formController.addObserver(this);

        formController.clearLabelFieldList();

        formController.addLabelField(new LabelField("Nom", true));
        formController.addLabelField(new LabelFieldMasked("Mot de passe", true));

        formController.initialize(null, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        Map<String, Object> labelFieldMap = (Map<String, Object>) arg;

        try {
            // Try to log in.
            State.getInstance().setCurrentUser(
                    Facade.getInstance()
                            .login(labelFieldMap.get("Nom").toString(), labelFieldMap.get("Mot de passe").toString())
            );
        }
        catch (BadCredentialException e) {
            // Can not log in due to bad credentials.
            formController.showErrorLabel("Identifiant ou mot de passe incorrect. ");
        }
    }

    public void setVisible(boolean visible) {
        loginPage.setVisible(visible);
    }
}
