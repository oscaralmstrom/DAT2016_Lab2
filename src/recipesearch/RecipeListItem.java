package recipesearch;

import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;

public class RecipeListItem extends AnchorPane {

  private RecipeSearchController parent;
  private Recipe recipe;
  @FXML
  private ImageView recipeImage;
  @FXML
  private Label recipeLabel;

  public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController) {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }

    this.recipe = recipe;
    parent = recipeSearchController;

    recipeLabel.setText(recipe.getName());
    recipeImage.setImage(recipe.getFXImage());
  }

  @FXML
  protected void onClick(Event event) {
    parent.openRecipeView(recipe);
  }
}
