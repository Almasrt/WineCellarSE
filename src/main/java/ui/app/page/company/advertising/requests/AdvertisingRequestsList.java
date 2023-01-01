package ui.app.page.company.advertising.requests;

import facade.Facade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import persistence.entity.advertising.Advertising;
import ui.app.State;
import ui.app.page.company.advertising.AdvertisingCard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdvertisingRequestsList implements Initializable {

    @FXML
    private GridPane cardHolder;
    private ObservableList<AdvertisingCard> cardList = FXCollections.observableArrayList();

    private final int nbColumn = 4;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Advertising> advertisingList = Facade.getInstance().getNotValidatedAdvertisings();
        cardList.clear();

        int maxWidth = 1280;
        int gapBetweenCard = 20;
        double preferredHeight = 230.0;
        double preferredWidth = (maxWidth - (nbColumn + 1) * gapBetweenCard) / nbColumn;
        advertisingList.forEach(advertising -> cardList.add(new AdvertisingCard(advertising, preferredHeight, preferredWidth, "advertisingRequestsList")));


        cardHolder.setAlignment(Pos.CENTER);
        cardHolder.setVgap(20.00);
        cardHolder.setHgap(20.00);
        cardHolder.setStyle("-fx-padding:10px;");

        onSearch();
    }

    @FXML
    public void onSearch() {
        int count = 0;

        cardHolder.getChildren().clear();
        for (AdvertisingCard card : cardList) {
            cardHolder.add(card, count % nbColumn, count / nbColumn);
            count++;
        }
    }

    public void onAction() {
        this.initialize(null, null);
    }
}
