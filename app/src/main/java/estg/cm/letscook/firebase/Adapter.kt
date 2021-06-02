package estg.cm.letscook.firebase

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import estg.cm.letscook.R


class Adapter(private val userList: ArrayList<Recipe>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_item,
                parent,
                false
            )
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val currentItem = userList[position]

            holder.title.text = currentItem.title
            val transformation: Transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(10f)
                .oval(false)
                .build()
            Picasso.get().load(currentItem.image).resize(1000, 600).centerCrop().transform(transformation).into(holder.image)
            holder.persons.text = currentItem.totalPeople
            holder.duration.text = currentItem.duration

        }

        override fun getItemCount(): Int {
            return userList.size
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val title : TextView = itemView.findViewById(R.id.recyclerview_item_title)
            val image : ImageView = itemView.findViewById(R.id.recyclerview_item_image)
            val persons : TextView = itemView.findViewById(R.id.recyclerview_item_servings)
            val duration : TextView = itemView.findViewById(R.id.recyclerview_item_duration)
        }
}