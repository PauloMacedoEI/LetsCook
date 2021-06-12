package estg.cm.letscook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import estg.cm.letscook.firebase.Adapter
import estg.cm.letscook.firebase.Recipe

class MainActivity : AppCompatActivity(), Adapter.OnRecipeClickListener {
    private lateinit var dbref : DatabaseReference
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val category = intent?.getStringExtra("EXTRA_CATEGORY")!!

        recipeRecyclerView = findViewById(R.id.recipeList)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()

        dbref = FirebaseDatabase.getInstance("https://let-s-cook-7ef9a-default-rtdb.europe-west1.firebasedatabase.app/").getReference(
            "Recipes"
        )
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (recipeSnapshot in snapshot.children) {
                        if (recipeSnapshot.child("category").value == category) {
                            val recipe = recipeSnapshot.getValue(Recipe::class.java)
                            recipeArrayList.add(recipe!!)
                        }
                    }
                    val adapter = Adapter(this@MainActivity, recipeArrayList)
                    recipeRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onRecipeClick(currentItem: Recipe) {
        val recipe =  arrayListOf<Recipe>()
        recipe.add(currentItem)

        val intent = Intent(this@MainActivity, RecipeInformation::class.java).apply {
            putExtra("EXTRA_RECIPE", recipe)
        }
        startActivity(intent)
    }

    override fun onStartClick(currentItem: Recipe) {
        val recipe =  arrayListOf<Recipe>()
        recipe.add(currentItem)

        val intent = Intent(this@MainActivity, RecipeStep::class.java).apply {
            putExtra("EXTRA_RECIPE", recipe)
        }
        startActivity(intent)
    }

}