package estg.cm.letscook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import estg.cm.letscook.firebase.Adapter
import estg.cm.letscook.firebase.Ingredient
import estg.cm.letscook.firebase.Recipe
import estg.cm.letscook.firebase.Step

class MainActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipeRecyclerView = findViewById(R.id.recipeList)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()
        getRecipeData()
    }

    private fun getRecipeData() {

        dbref = FirebaseDatabase.getInstance("https://let-s-cook-7ef9a-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Recipes")
        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("getRecipeData", snapshot.toString())

                if(snapshot.exists()){
                    for(recipeSnapshot in snapshot.children){
                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                        Log.i("getRecipeData", recipeSnapshot.toString())
                        recipeArrayList.add(recipe!!)

                    }
                    recipeRecyclerView.adapter = Adapter(recipeArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}