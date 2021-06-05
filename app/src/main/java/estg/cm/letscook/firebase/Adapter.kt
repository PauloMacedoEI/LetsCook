package estg.cm.letscook.firebase

import android.content.Intent
import android.graphics.Color
import android.net.wifi.hotspot2.pps.HomeSp
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import estg.cm.letscook.MainActivity
import estg.cm.letscook.R
import estg.cm.letscook.RecipeInformation


class Adapter(private val listener: onRecipeClickListener, private val recipeList: ArrayList<Recipe>) : ListAdapter<Recipe, Adapter.MyViewHolder>(RecipesComparator())  {
        private lateinit var currentItem: Recipe

        class RecipesComparator : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_item,
                parent,
                false
            )
            return MyViewHolder(itemView)


        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            currentItem = recipeList[position]


            holder.title.text = currentItem.title
            val transformation: Transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(10f)
                .oval(false)
                .build()
            Picasso.get().load(currentItem.image).resize(1000, 600).centerCrop().transform(transformation).into(holder.image)
            holder.persons.text = currentItem.totalPeople
            holder.duration.text = currentItem.duration

            val id: String = currentItem.id

        }

        override fun getItemCount(): Int {
            return recipeList.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
            val title : TextView = itemView.findViewById(R.id.recyclerview_item_title)
            val image : ImageView = itemView.findViewById(R.id.recyclerview_item_image)
            val persons : TextView = itemView.findViewById(R.id.recyclerview_item_servings)
            val duration : TextView = itemView.findViewById(R.id.recyclerview_item_duration)

            init {
                itemView.findViewById<ImageView>(R.id.recyclerview_item_recipe_icon).setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                if (v?.findViewById<ImageView>(R.id.recyclerview_item_recipe_icon)?.isClickable == true){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener.onRecipeClick(recipeList[position])
                    }
                }
            }


        }

        interface onRecipeClickListener{
            fun onRecipeClick(currentItem: Recipe)
        }

}
