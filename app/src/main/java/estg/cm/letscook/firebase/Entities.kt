package estg.cm.letscook.firebase

class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<Step> = emptyList(),
    val totalPeople: String = "",
    val duration: String = "",
    val image: String = "",
    val video: String = ""
)

class Ingredient(
    val name: String = "",
    val quantity: String = ""
)

class Step(
    val description: String = "",
    val duration: String = "",
    val video: String = ""
)

