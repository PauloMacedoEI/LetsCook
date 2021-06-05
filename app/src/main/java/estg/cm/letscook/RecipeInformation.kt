package estg.cm.letscook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_information)

        val recipe = intent?.getParcelableArrayListExtra<Recipe>("EXTRA_RECIPE")
        Log.i("cucu", recipe.toString())


        titleText = findViewById(R.id.item_title_information)
        image = findViewById(R.id.item_image_information)
        startButton = findViewById(R.id.item_start_icon_information)

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



    }
}