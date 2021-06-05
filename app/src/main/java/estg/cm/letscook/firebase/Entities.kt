package estg.cm.letscook.firebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<Step> = emptyList(),
    val servings: Int? = null,
    val duration: Int? = null,
    val image: String = "",
    val video: String = ""
): Parcelable

@Parcelize
class Ingredient(
    val name: String = "",
    val quantity: String = ""
): Parcelable

@Parcelize
class Step(
    val description: String = "",
    val duration: Int? = null,
    val video: String = ""
): Parcelable

