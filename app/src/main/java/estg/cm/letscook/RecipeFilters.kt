package estg.cm.letscook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import estg.cm.letscook.firebase.Recipe

class RecipeFilters : AppCompatActivity() {

    private lateinit var appetizers : ImageView
    private lateinit var breakFast : ImageView
    private lateinit var desserts : ImageView
    private lateinit var drinks : ImageView
    private lateinit var vegan : ImageView
    private lateinit var mainDishes : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_filters)


        appetizers = findViewById(R.id.recipeFilters_appetizers)
        breakFast = findViewById(R.id.recipeFilters_breakfast)
        desserts = findViewById(R.id.recipeFilters_desserts)
        drinks = findViewById(R.id.recipeFilters_drinks)
        vegan = findViewById(R.id.recipeFilters_vegan)
        mainDishes = findViewById(R.id.recipeFilters_mainDishes)

        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/let-s-cook-7ef9a.appspot.com/o/Filters%2Fmain_dishes.jpeg?alt=media&token=a039316b-66f3-4fa5-9884-7376bfbf39db").resize(150, 150).centerCrop().into(mainDishes)
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/let-s-cook-7ef9a.appspot.com/o/Filters%2Fappetizers.jpg?alt=media&token=c8ef57e2-5166-48b6-8006-dea1ec2a4854").resize(150, 150).centerCrop().into(appetizers)
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/let-s-cook-7ef9a.appspot.com/o/Filters%2Fbreakfast.png?alt=media&token=f11d7d38-2568-4482-a2b9-d7bb27264eb2").resize(150, 150).centerCrop().into(breakFast)
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/let-s-cook-7ef9a.appspot.com/o/Filters%2Fdessert.jpg?alt=media&token=64bf15a2-1a2d-4be3-a98c-2db9a17de096").resize(150, 150).centerCrop().into(desserts)
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/let-s-cook-7ef9a.appspot.com/o/Filters%2Fdrinks.jpg?alt=media&token=4173f881-a6ff-44fb-ab1b-679f4cf52737").resize(150, 150).centerCrop().into(drinks)
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/let-s-cook-7ef9a.appspot.com/o/Filters%2Fvegan.jpg?alt=media&token=d35472b7-2e73-4f49-b8a3-8dbebb2572d2").resize(150, 150).centerCrop().into(vegan)

        var category = ""
        appetizers.setOnClickListener {
            category = "APPETIZERS"
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_CATEGORY", category)
            }
            startActivity(intent)
        }
        breakFast.setOnClickListener {
            category = "BREAKFAST"
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_CATEGORY", category)
            }
            startActivity(intent)
        }
        desserts.setOnClickListener {
            category = "DESSERTS"
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_CATEGORY", category)
            }
            startActivity(intent)
        }
        drinks.setOnClickListener {
            category = "DRINKS"
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_CATEGORY", category)
            }
            startActivity(intent)
        }
        vegan.setOnClickListener {
            category = "VEGAN"
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_CATEGORY", category)
            }
            startActivity(intent)
        }
        mainDishes.setOnClickListener {
            category = "MAINDISHES"
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_CATEGORY", category)
            }
            startActivity(intent)
        }

    }
}