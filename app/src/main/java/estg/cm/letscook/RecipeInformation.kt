package estg.cm.letscook

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
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
    private lateinit var video: ImageView
    private lateinit var rootLayout: ConstraintLayout

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_information)

        val recipe = intent?.getParcelableArrayListExtra<Recipe>("EXTRA_RECIPE")

        titleText = findViewById(R.id.item_title_information)
        image = findViewById(R.id.item_image_information)
        startButton = findViewById(R.id.item_start_icon_information)
        duration = findViewById(R.id.item_duration_information)
        servings = findViewById(R.id.item_servings_information)
        ingredients = findViewById(R.id.item_ingredients)
        steps = findViewById(R.id.item_steps)
        video = findViewById(R.id.item_youtube_icon_information)
        rootLayout = findViewById(R.id.recipe_information)

        startButton.setOnClickListener {
            val recipe2 =  arrayListOf<Recipe>()
            recipe?.get(0)?.let { it1 -> recipe2.add(it1) }

            val intent = Intent(this, RecipeStep::class.java).apply {
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

        video.setOnClickListener{

            rootLayout.setBackgroundColor(Color.GRAY)
            rootLayout.alpha = 0.5F

            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.popup_window,null)

            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            //popupWindow.setBackgroundDrawable(ColorDrawable(R.color.black))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut
            }

            val video = view.findViewById<VideoView>(R.id.videoViewRecipe)

            val mediaController = MediaController(this)
            mediaController.setAnchorView(video)

            val url = recipe?.get(0)?.video
            val uri = Uri.parse(url)

            video.setMediaController(mediaController)
            video.setVideoURI(uri)
            video.requestFocus()
            video.start()

            TransitionManager.beginDelayedTransition(rootLayout)
            popupWindow.showAtLocation(
                rootLayout, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )

            val closePopup = view.findViewById<ImageView>(R.id.closePopup)

            closePopup.setOnClickListener{
                popupWindow.dismiss()
                rootLayout.setBackgroundColor(Color.TRANSPARENT)
                rootLayout.alpha = 1F
            }
        }
    }
}