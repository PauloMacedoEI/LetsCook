package estg.cm.letscook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import estg.cm.letscook.firebase.Recipe

class RecipeInformation : AppCompatActivity() {
    private lateinit var titleText : TextView
    private lateinit var image: ImageView
    private lateinit var startButton : ImageView
    private lateinit var servings: TextView
    private lateinit var duration: TextView
    private lateinit var ingredients: TextView
    private lateinit var steps: TextView
//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_information)

        val recipe = intent?.getParcelableArrayListExtra<Recipe>("EXTRA_RECIPE")
        Log.i("cucu", recipe.toString())


        titleText = findViewById(R.id.item_title_information)
        image = findViewById(R.id.item_image_information)
        startButton = findViewById(R.id.item_start_icon_information)
        duration = findViewById(R.id.item_duration_information)
        servings = findViewById(R.id.item_servings_information)
        ingredients = findViewById(R.id.item_ingredients)
        steps = findViewById(R.id.item_steps)


        startButton.setOnClickListener {
            val recipe2 =  arrayListOf<Recipe>()
            recipe?.get(0)?.let { it1 -> recipe2.add(it1) }
            Log.i("cucu3", recipe2[0].title)

            val intent = Intent(this, StepRecipe::class.java).apply {
                putExtra("EXTRA_RECIPE", recipe2)
            }
            startActivity(intent)


        }

        titleText.text = recipe?.get(0)?.title
        val transformation: Transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(10f)
                .oval(false)
                .build()
        Picasso.get().load(recipe?.get(0)?.image).resize(1000, 600).centerCrop().transform(transformation).into(image)
        if(recipe?.get(0)?.duration!! > 59 ){
            val hours = recipe[0].duration!! / 60
            val minutes = recipe[0].duration!! % 60
            duration.text = hours.toString().plus(getString(R.string.recipe_information_hours)).plus(" ").plus(minutes.toString()).plus(getString(R.string.recipe_information_minutes))
        } else {
            duration.text = recipe[0]?.duration.toString().plus(getString(R.string.recipe_information_minutes))
        }
        servings.text = recipe[0]?.servings.toString().plus(" ").plus(getString(R.string.recipe_information_servings))
        for (ingredient in recipe[0].ingredients){
            ingredients.text = ingredients.text.toString().plus(ingredient.name.plus(" - ").plus(ingredient.quantity).plus("\n"))
        }

        var number = 1
        for (step in recipe[0].steps){
            steps.text = steps.text.toString().plus("$number - ").plus(step.description.plus("\n"))
            number++
        }




    }
}