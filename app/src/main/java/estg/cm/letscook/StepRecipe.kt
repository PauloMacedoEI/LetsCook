package estg.cm.letscook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import estg.cm.letscook.firebase.Recipe
import org.w3c.dom.Text

class StepRecipe : AppCompatActivity() {
    private lateinit var titleText : TextView
    private lateinit var image: ImageView
    private lateinit var descriptionStep: TextView
    private lateinit var durationStep: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_recipe)

        titleText = findViewById(R.id.item_title_steps)
        image = findViewById(R.id.item_image_steps)
        descriptionStep = findViewById(R.id.item_description_steps)
        durationStep = findViewById(R.id.item_duration_steps)


        val recipe = intent?.getParcelableArrayListExtra<Recipe>("EXTRA_RECIPE")
        Log.i("cucu", recipe.toString())


        titleText.text = recipe?.get(0)?.title
        val transformation: Transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(10f)
                .oval(false)
                .build()
        Picasso.get().load(recipe?.get(0)?.image).resize(1000, 600).centerCrop().transform(transformation).into(image)
        descriptionStep.text = recipe?.get(0)?.steps?.get(0)?.description
        if(recipe?.get(0)?.steps?.get(0)?.duration!! > 59 ){
            val hours = recipe[0].steps[0].duration!! / 60
            val minutes = recipe[0].steps[0].duration!! % 60
            durationStep.text = hours.toString().plus(getString(R.string.recipe_information_hours)).plus(" ").plus(minutes.toString()).plus(getString(R.string.recipe_information_minutes))
        } else {
            durationStep.text = recipe[0]?.steps!![0].duration.toString().plus(getString(R.string.recipe_information_minutes))
        }

    }
}