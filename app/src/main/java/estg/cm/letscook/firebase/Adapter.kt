package estg.cm.letscook.firebase

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import estg.cm.letscook.R
import java.io.File
import java.net.URI


class Adapter(private val userList: ArrayList<Recipe>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item,
                    parent, false)
            return MyViewHolder(itemView)

        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            val currentitem = userList[position]

            holder.title.text = currentitem.title

            val storageRef: StorageReference = FirebaseStorage.getInstance("gs://let-s-cook-7ef9a.appspot.com/").reference.child("Images/${currentitem.image}")

//            val imgFile = File(storageRef.path)
//
//            Log.i("storage1", imgFile.toString())
//
//            if (imgFile.exists()) {
//                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                holder.image.setImageBitmap(myBitmap)
//            }
            Picasso.get().load(currentitem.image).into(holder.image)

            //holder.image.setImageURI(Uri.parse(currentitem.image))
            Log.i("storage", storageRef.toString())
            holder.persons.text = currentitem.totalPeople
            holder.duration.text = currentitem.duration

        }

        override fun getItemCount(): Int {

            return userList.size
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val title : TextView = itemView.findViewById(R.id.recipeTitle)
            val image : ImageView = itemView.findViewById(R.id.recipeImage)
            val persons : TextView = itemView.findViewById(R.id.recipePeople)
            val duration : TextView = itemView.findViewById(R.id.recipeTime)

        }

}