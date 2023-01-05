package ui.app.page.company.referencing.list;

import facade.Facade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import org.bson.types.ObjectId;
import persistence.entity.company.Company;
import persistence.entity.referencing.Referencing;
import ui.app.State;
import ui.app.page.company.referencing.ReferencingCard;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReferencingList implements Initializable {

    @FXML
    private GridPane cardHolder;
    private ObservableList<ReferencingCard> cardList = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<String> select, selectStatus;

    private ObjectId company;
    private String status;
    private final int nbColumn = 4;

    //TODO : faire disparaitre les card si status vide/entreprise vide

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(State.getInstance().getCurrentUser() != null){
            status = "Tous";
            company = null;

            List<Company> companies = Facade.getInstance().findAllCompaniesByUserId(State.getInstance().getCurrentUser().getId());
            if(select.getItems().size() == 0){
                for (Company c : companies){
                    select.getItems().add(c.getName());
                }
            }
            if(selectStatus.getItems().size() == 0){
                selectStatus.getItems().add("A venir");
                selectStatus.getItems().add("En cours");
                selectStatus.getItems().add("Passé");
                selectStatus.getItems().add("Tous");
            }

            selectStatus.setOnAction((event) -> {
                String selectedItem = selectStatus.getValue();
                status = selectedItem;
                if(company != null) {
                    list(company,status);
                }
            });

            select.setOnAction((event) -> {
                String selectedItem = select.getValue();
                for(Company c : companies){
                    if(c.getName().equals(selectedItem)){
                        company = c.getId();
                        list(c.getId(), status);
                    }
                }
            });
        }
    }

    public void list(ObjectId company, String status){
        cardList.clear();
        List<Referencing> referencingList;
        if(status.equals("Tous")){
            referencingList = Facade.getInstance().getReferencingsByCompany(company);
            referencingList.forEach(referencing -> cardList.add(new ReferencingCard(referencing)));

        } else {
            referencingList = Facade.getInstance().getReferencingsByCompanyByStatus(company, status);
            referencingList.forEach(referencing -> cardList.add(new ReferencingCard(referencing)));
        }

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
        for (ReferencingCard card : cardList) {
            cardHolder.add(card, count % nbColumn, count / nbColumn);
            count++;
        }
    }

    public void onAction() {
        this.initialize(null, null);
    }
}
