package ui.app.page.guides.list;

import facade.Facade;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import persistence.entity.guide.Guide;
import ui.app.State;
import ui.app.helpers.services.CustomSceneHelper;
import ui.app.page.guides.guideDetails.GuideDetails;
import ui.app.page.guides.guideModification.GuideModification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class GuideCard extends Pane {
    protected final Label title;
    protected final Label description;
    protected final Button boutonModification;
    protected Date creationDate;
    private Button boutonSuppression;
    private final double preferredHeight, preferredWidth;
    private Guide guide;
    private CustomSceneHelper sceneHelper = new CustomSceneHelper();
    private GuideList guideList;

    public GuideCard(Guide guide, GuideList guideList, double preferredHeight, double preferredWidth) {
        this.guide = guide;
        this.guideList = guideList;
        this.preferredHeight = preferredHeight;
        this.preferredWidth = preferredWidth;

        title = new Label();
        description = new Label();

        boutonModification = new Button("Modifier");

        setPrefHeight(preferredHeight);
        setPrefWidth(preferredWidth);
        setStyle("-fx-background-color:#FFF; -fx-border-radius: 10px; -fx-background-radius: 10px;-fx-alignment: center; -fx-border-width: 1px");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setHeight(3);
        dropShadow.setWidth(3);
        dropShadow.setBlurType(BlurType.TWO_PASS_BOX);
        setEffect(dropShadow);

        title.setAlignment(Pos.CENTER);
        title.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        title.setLayoutX(0);
        title.setLayoutY(40);
        title.setPrefHeight(26.0);
        title.setPrefWidth(preferredWidth);
        title.setText(guide.getTitle());
        title.setFont(new Font(17.0));

        description.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        description.setLayoutX(10);
        description.setLayoutY(80);
        description.setPrefHeight(26.0);
        description.setPrefWidth(preferredWidth);
        description.setText(guide.getDescription());
        description.setFont(new Font(17.0));

        setOnMouseClicked(e -> {
            // Action you want to do
            State.getInstance().setCurrentGuide(guide);
            sceneHelper.bringNodeToFront(GuideDetails.class.getSimpleName());
        });

        getChildren().addAll(title, description);

        if(Facade.getInstance().isAdminLogged()){
            ImageView trash = new ImageView();
            try {
                trash.setImage(new Image(new FileInputStream(Objects.requireNonNull(getClass().getResource("../../../../assets/trash.png")).getPath())));
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            int photoSize = 20;
            trash.setFitHeight(photoSize);
            trash.setFitWidth(photoSize);
            trash.setLayoutX(10.0);
            trash.setLayoutY(10.0);
            getChildren().add(trash);

            trash.setOnMouseClicked(e -> {
                supprimerGuide();
            });
        }

        boutonModification.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                sceneHelper.bringNodeToFront(GuideModification.class.getSimpleName());
            }
        });
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuideList(GuideList guideList) {
        this.guideList = guideList;
    }

    public void supprimerGuide(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer"+guide.getTitle()+"?");
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() != ButtonType.CANCEL){
            Facade.getInstance().deleteOneGuide(guide.getId());
        }
        guideList.initialize(null, null);
    }
}
