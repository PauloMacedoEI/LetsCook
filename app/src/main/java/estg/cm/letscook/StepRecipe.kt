package estg.cm.letscook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import estg.cm.letscook.firebase.Recipe

class StepRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_recipe)

        val recipe = intent?.getParcelableArrayListExtra<Recipe>("EXTRA_RECIPE")
        Log.i("cucu", recipe.toString())
    }
}