package estg.cm.letscook

import android.media.MediaPlayer
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import estg.cm.letscook.firebase.Recipe
import java.util.*
import java.util.concurrent.TimeUnit

class StepRecipe : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var titleText : TextView
    private lateinit var image: ImageView
    private lateinit var descriptionStep: TextView
    private lateinit var durationStep: TextView
    private lateinit var countdown: TextView
    private lateinit var timerRingSound: MediaPlayer
//
    private var tts: TextToSpeech? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_recipe)

        titleText = findViewById(R.id.item_title_steps)
        image = findViewById(R.id.item_image_steps)
        descriptionStep = findViewById(R.id.item_description_steps)
        durationStep = findViewById(R.id.item_duration_steps)
        countdown = findViewById(R.id.countdown)
        timerRingSound = MediaPlayer.create(this, R.raw.alarme)

        val recipe = intent?.getParcelableArrayListExtra<Recipe>("EXTRA_RECIPE")

        titleText.text = recipe?.get(0)?.title
        val transformation: Transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(10f)
                .oval(false)
                .build()
        Picasso.get().load(recipe?.get(0)?.image).resize(1000, 600).centerCrop().transform(transformation).into(image)
        descriptionStep.text = recipe?.get(0)?.steps?.get(0)?.description

        if(recipe?.get(0)?.steps?.get(0)?.duration!! > 59 ){
            val hours = recipe[0].steps[0].duration!! / 60
            val minutes = recipe[0].steps[0].duration!! % 60
            durationStep.text = hours.toString().plus(getString(R.string.recipe_information_hours)).plus(" ").plus(minutes.toString()).plus(getString(R.string.recipe_information_minutes))
        } else {
            durationStep.text = recipe[0]?.steps!![0].duration.toString().plus(getString(R.string.recipe_information_minutes))
        }

        var sDuration = ""
        val minutes = recipe[0].steps[0].duration?.toLong()!!
        val duration: Long = TimeUnit.MINUTES.toMillis(minutes)

        val timer = object: CountDownTimer(duration, 1000) {
            override fun onTick(l: Long) {
                sDuration = String.format(
                    Locale.ENGLISH,"%02d : %02d"
                    , TimeUnit.MILLISECONDS.toMinutes(l)
                    , TimeUnit.MILLISECONDS.toSeconds(l) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))
                countdown.text = sDuration
                when (sDuration) {
                    "00 : 20" -> twentySecWarning()
                    "00 : 00" -> finishWarning()
                }
            }

            override fun onFinish() {
                countdown.visibility = View.GONE

                Toast.makeText(this@StepRecipe, "Acabou", Toast.LENGTH_SHORT).show()
            }
        }

        tts = TextToSpeech(this, this)
        Handler(Looper.getMainLooper()).postDelayed({
            timer.start()
            speakOut()
        }, 2000)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.isLanguageAvailable(Locale("pt"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS","The Language specified is not supported!")
                }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun speakOut() {
        val text = descriptionStep.text.toString()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun twentySecWarning() {
        val text = "Falta 20 segundos"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun finishWarning() {
        timerRingSound.start()
        Handler(Looper.getMainLooper()).postDelayed({
            val text = "O tempo acabou"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        }, 5000)

    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}