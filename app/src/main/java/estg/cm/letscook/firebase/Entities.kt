package estg.cm.letscook.firebase

class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val ingredients: List<Ingredient>,
    val steps: List<Step>,
    val duration: String,
    val video: String
)

class Ingredient(
    val name: String,
    val quantity: String
)

class Step(
    val description: String,
    val duration: String,
    val video: String
)

