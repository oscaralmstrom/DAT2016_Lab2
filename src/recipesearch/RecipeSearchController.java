
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;

public class RecipeSearchController implements Initializable {

  private RecipeDatabase db = RecipeDatabase.getSharedInstance();

  @FXML
  private ComboBox<String> ingredientComboBox;
  @FXML
  private ComboBox<String> cuisineComboBox;
  @FXML
  private ToggleGroup difficultyToggleGroup;
  @FXML
  private RadioButton allDifficultiesRadioButton;
  @FXML
  private RadioButton easyRadioButton;
  @FXML
  private RadioButton mediumRadioButton;
  @FXML
  private RadioButton hardRadioButton;
  @FXML
  private Spinner<String> priceSpinner;
  @FXML
  private Slider timeSlider;
  @FXML
  private FlowPane searchFlowPane;

  private RecipeBackendController backendController;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    backendController = new RecipeBackendController();
    updateRecipeList();
  }

  private void updateRecipeList() {
    searchFlowPane.getChildren().clear();
    for (Recipe recipe : backendController.getRecipes()) {
      RecipeListItem item = new RecipeListItem(recipe, this);
      searchFlowPane.getChildren().add(item);
    }
  }
}