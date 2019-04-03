package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;

public class RecipeSearchController implements Initializable {

  @FXML
  private ComboBox<String> ingredientComboBox;
  @FXML
  private ComboBox<String> cuisineComboBox;
  @FXML
  private ToggleGroup difficultyToggleGroup; //FIXME is this used by SceneBuilder?
  @FXML
  private RadioButton allDifficultiesRadioButton;
  @FXML
  private RadioButton easyRadioButton;
  @FXML
  private RadioButton mediumRadioButton;
  @FXML
  private RadioButton hardRadioButton;
  @FXML
  private Spinner<Integer> priceSpinner;
  @FXML
  private Slider timeSlider;
  @FXML
  private FlowPane searchFlowPane;
  @FXML
  private Label minuteLabel;
  @FXML
  private ImageView detailedImageView;
  @FXML
  private Label detailedNameLabel;
  @FXML
  private Label detailedDescriptionLabel;
  @FXML
  private Label detailedInstructionLabel;
  @FXML
  private Button detailedViewCloseButton;
  @FXML
  private AnchorPane detailedViewAnchorPane;
  @FXML
  private SplitPane searchPane;

  private Map<String, RecipeListItem> recipeListItemMap;
  private RecipeBackendController backendController;
  private RecipeDatabase db = RecipeDatabase.getSharedInstance();

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    backendController = new RecipeBackendController();
    recipeListItemMap = initHashMap();
    updateRecipeList();
    setupIngredientComboBox();
    setupCuisineComboBox();
    setUpDifficultyRadioButtons();
    setupPriceSpinner();
    setupTimeSlider();
  }

  @FXML
  public void closeRecipeView() {
    searchPane.toFront();
  }

  void openRecipeView(Recipe recipe) {
    populateRecipeDetailView(recipe);
    detailedViewAnchorPane.toFront();
  }

  private void populateRecipeDetailView(Recipe recipe) {
    detailedNameLabel.setText(recipe.getName());
    detailedImageView.setImage(recipe.getFXImage());
    detailedInstructionLabel.setText(recipe.getInstruction());
    detailedDescriptionLabel.setText(recipe.getDescription());
  }

  private void setupCuisineComboBox() {
    cuisineComboBox.getItems()
        .addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika",
            "Frankrike");
    cuisineComboBox.getSelectionModel().select("Visa alla");
    cuisineComboBox.getSelectionModel().selectedItemProperty()
        .addListener(((observable, oldValue, newValue) -> {
              backendController.setCuisine(newValue);
              updateRecipeList();
            })
        );
  }

  private void setupIngredientComboBox() {
    ingredientComboBox.getItems()
        .addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
    ingredientComboBox.getSelectionModel().select("Visa alla");
    ingredientComboBox.getSelectionModel().selectedItemProperty()
        .addListener(((observable, oldValue, newValue) -> {
              backendController.setMainIngredient(newValue);
              updateRecipeList();
            })
        );
  }

  private void setUpDifficultyRadioButtons() {
    difficultyToggleGroup = new ToggleGroup();

    allDifficultiesRadioButton.setToggleGroup(difficultyToggleGroup);
    easyRadioButton.setToggleGroup(difficultyToggleGroup);
    mediumRadioButton.setToggleGroup(difficultyToggleGroup);
    hardRadioButton.setToggleGroup(difficultyToggleGroup);

    allDifficultiesRadioButton.setSelected(true);

    difficultyToggleGroup.selectedToggleProperty()
        .addListener(((observable, oldValue, newValue) -> {
              if (difficultyToggleGroup.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                backendController.setDifficulty(selected.getText());
                updateRecipeList();
              }
            })
        );
  }

  private void setupPriceSpinner() {
    SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.
        IntegerSpinnerValueFactory(0, 100, 100, 10);
    priceSpinner.setValueFactory(factory);
    priceSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
      backendController.setMaxPrice(newValue);
      updateRecipeList();
    }));

    priceSpinner.focusedProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue) {
      } else {
        int value = Integer.valueOf(priceSpinner.getEditor().getText());
        backendController.setMaxPrice(value);
        updateRecipeList();
      }
    }));
  }

  private void setupTimeSlider() {
    timeSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
      if (newValue != null && !newValue.equals(oldValue) && !timeSlider.isValueChanging()) {
        int value = (int) timeSlider.getValue();
        backendController.setMaxTime(value);
        updateRecipeList();
        updateMinuteLabel();
      }
    }));
    updateMinuteLabel();
  }

  private Map<String, RecipeListItem> initHashMap() {
    Map<String, RecipeListItem> map = new HashMap<>(backendController.getRecipes().size());
    for (Recipe recipe : backendController.getRecipes()) {
      RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
      map.put(recipe.getName(), recipeListItem);
    }
    return map;
  }

  private void updateRecipeList() {
    searchFlowPane.getChildren().clear();
    for (Recipe recipe : backendController.getRecipes()) {
      RecipeListItem item = recipeListItemMap.get(recipe.getName());
      searchFlowPane.getChildren().add(item);
    }
  }

  private void updateMinuteLabel() {
    int val = (int) timeSlider.getValue();
    minuteLabel.setText(val + " minuter");
  }
}