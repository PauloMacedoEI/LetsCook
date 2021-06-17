package estg.cm.letscook

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import estg.cm.letscook.firebase.Recipe
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class RecipeStep : AppCompatActivity(), TextToSpeech.OnInitListener, SensorEventListener {
    private lateinit var image: ImageView
    private lateinit var stepForward: ImageView
    private lateinit var stepBack: ImageView
    //
    private lateinit var titleText : TextView
    private lateinit var stepTitle: TextView
    private lateinit var stepDuration: TextView
    private lateinit var stepDescription: TextView
    private lateinit var countdown: TextView
    private lateinit var video: VideoView
    private lateinit var mediaController: MediaController

    private lateinit var timerRingSound: MediaPlayer
    private lateinit var timerProgressBar: ProgressBar

    private lateinit var timer: CountDownTimer
    private var tts: TextToSpeech? = null
    private var stepNumber = 1
    private var step = 0
    private var recipe: ArrayList<Recipe> = arrayListOf()


    private var time = System.currentTimeMillis()
    private var anterior: Long = 2500
    private var time1: Long = 0
    private var time2: Long = 0
    private val twoTouchs = 1000
    private lateinit var sensorManager: SensorManager
    private var proximity: Sensor? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_recipe)

        image = findViewById(R.id.recipeStep_image)
        stepForward = findViewById(R.id.recipeStep_stepForward)
        stepBack = findViewById(R.id.recipeStep_stepBack)

        titleText = findViewById(R.id.recipeStep_title)
        stepTitle = findViewById(R.id.recipeStep_step_title)
        stepDuration = findViewById(R.id.recipeStep_step_duration)
        stepDescription = findViewById(R.id.recipeStep_step_description)
        countdown = findViewById(R.id.recipeStep_step_countdown)
        timerRingSound = MediaPlayer.create(this, R.raw.alarme)
        timerProgressBar = findViewById(R.id.recipeStep_step_progressBar)
        video = findViewById(R.id.videoViewRecipe)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        recipe = intent?.getParcelableArrayListExtra("EXTRA_RECIPE")!!

        titleText.text = recipe[0].title
        val transformation: Transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(10f)
                .oval(false)
                .build()
        Picasso.get().load(recipe[0].image).resize(1000, 600).centerCrop().transform(transformation).into(image)

        mediaController = MediaController(this)
        mediaController.setAnchorView(video)

        populateStepFields(step)

        tts = TextToSpeech(this, this)

        stepForward.setOnClickListener {
            if (tts!!.isSpeaking) tts!!.stop()
            timer.cancel()
            step++

            if(step == recipe[0].steps.size - 1) {
                // Toast.makeText(this, "Nao existe mais passos!", Toast.LENGTH_SHORT).show()
                stepForward.isInvisible = true
                stepBack.isVisible = true
            } else {
                stepForward.isVisible = true
                stepBack.isVisible = true
            }

            if(step <= recipe[0].steps.size - 1) {
                stepNumber++
                populateStepFields(step)
            }

        }

        stepBack.setOnClickListener {
            if (tts!!.isSpeaking) tts!!.stop()
            timer.cancel()
            step--

            if(step == 0) {
                // Toast.makeText(this, "Este é o primeiro passo!", Toast.LENGTH_SHORT).show()
                stepForward.isVisible = true
                stepBack.isInvisible = true
            } else {
                stepForward.isVisible = true
                stepBack.isVisible = true
            }

            if(step >= 0) {
                stepNumber--
                populateStepFields(step)
            }
            //video.start()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSensorChanged(event: SensorEvent) {
        val distance = event.values[0]
        // Do something with this sensor data.

//        if (distance < proximity?.maximumRange!!) {
//            //Toast.makeText(this, "passou uma mao", Toast.LENGTH_SHORT).show()
//            window.decorView.setBackgroundColor(Color.GREEN)
//
//        } else {
//            window.decorView.setBackgroundColor(Color.BLACK)
//        }
        when (distance) {
            (0.0).toFloat() ->
                if (time1 == 0.toLong()) {
                    time1 = System.currentTimeMillis()
                    Log.i("time1", time1.toString())
                } else if (System.currentTimeMillis() - time1 > anterior) {
                    time1 = System.currentTimeMillis()
                    Log.i("time1", time1.toString())
                }
                else if (time2 == 0.toLong()/* && System.currentTimeMillis() - time1 < anterior*/) {
                    time2 = System.currentTimeMillis()
                    Log.i("time2", time2.toString())
                } /*else {
                    time1 = 0
                    time2 = 0
                }*/
        }

        if (time1 != 0.toLong()) {
            if (time2 != 0.toLong()) {
                if (time2 - time1 < twoTouchs) {
                    if (tts!!.isSpeaking) tts!!.stop()
                    timer.cancel()
                    step++

                    if(step == recipe[0].steps.size - 1) {
                        // Toast.makeText(this, "Nao existe mais passos!", Toast.LENGTH_SHORT).show()
                        stepForward.isInvisible = true
                        stepBack.isVisible = true
                    } else {
                        stepForward.isVisible = true
                        stepBack.isVisible = true
                    }

                    if(step <= recipe[0].steps.size - 1) {
                        stepNumber++
                        populateStepFields(step)
                    }

                    //Toast.makeText(this, "Proximo Passo", Toast.LENGTH_SHORT).show()
                    val resultado = time2 - time1;
                    Log.i("result", resultado.toString())
                    time1 = 0
                    time2 = 0
                } else if (time2 - time1 < anterior) {

                    if (tts!!.isSpeaking) tts!!.stop()
                    timer.cancel()
                    step--

                    if(step == 0) {
                        // Toast.makeText(this, "Este é o primeiro passo!", Toast.LENGTH_SHORT).show()
                        stepForward.isVisible = true
                        stepBack.isInvisible = true
                    } else {
                        stepForward.isVisible = true
                        stepBack.isVisible = true
                    }

                    if(step >= 0) {
                        stepNumber--
                        populateStepFields(step)
                    }

                    //Toast.makeText(this, "Passo anterior", Toast.LENGTH_SHORT).show()
                    val resultado = time2 - time1;
                    Log.i("result", resultado.toString())

                    time1 = 0
                    time2 = 0
                } else {
                    time1 = 0
                    time2 = 0
                }
            }

        }
    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()

        proximity?.also { proximity ->
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun populateStepFields(s: Int) {
        val step = recipe[0].steps[s]

        if(step.duration!! > 59 ){
            val hours = step.duration / 60
            val minutes = step.duration % 60
            stepTitle.text = stepNumber.toString()
                    .plus("º ")
                    .plus(getString(R.string.recipeStep_step_title))
                    .plus(" (")
            stepDuration.text = hours.toString()
                    .plus(getString(R.string.recipe_information_hours))
                    .plus(" ")
                    .plus(minutes.toString())
                    .plus(getString(R.string.recipe_information_minutes))
                    .plus(")")
        } else {
            stepTitle.text = stepNumber.toString()
                    .plus("º ")
                    .plus(getString(R.string.recipeStep_step_title))
                    .plus(" (")
            stepDuration.text = step.duration.toString()
                    .plus(getString(R.string.recipe_information_minutes))
                    .plus(")")
        }
        stepDescription.text = step.description


        val minutes = step.duration.toLong()
        val duration: Long = TimeUnit.MINUTES.toMillis(minutes)

        val seconds = step.duration * 60
        var secondsRemaining = seconds
        timerProgressBar.progress = 0
        timerProgressBar.max = seconds

        timer = object: CountDownTimer(duration, 1000) {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onTick(l: Long) {
                secondsRemaining--
                val sDuration = if(step.duration > 59) {
                    String.format(
                            Locale.ENGLISH,"%02d : %02d : %02d",
                            TimeUnit.MILLISECONDS.toHours(l),
                            TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                            TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))

                } else {
                    String.format(
                            Locale.ENGLISH,"%02d : %02d",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)))
                }
                countdown.text = sDuration
                timerProgressBar.progress = seconds - secondsRemaining
                when (sDuration) {
                    "00 : 00 : 20" -> twentySecWarning()
                    "00 : 00 : 00" -> finishWarning()
                    "00 : 20" -> twentySecWarning()
                    "00 : 00" -> finishWarning()
                }
            }

            override fun onFinish() {
                Toast.makeText(this@RecipeStep, getString(R.string.countdownFinishWarning), Toast.LENGTH_SHORT).show()
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (tts!!.isSpeaking) tts!!.stop()
            timer.cancel()
            timer.start()
        }, 1000)
        Handler(Looper.getMainLooper()).postDelayed({
            readDescription()
        }, 2000)

        val url = step.video
        val uri = Uri.parse(url)

        video.setMediaController(mediaController)
        video.setVideoURI(uri)
        video.requestFocus()

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
    private fun readDescription() {
        val text = stepDescription.text.toString()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun twentySecWarning() {
        val text = getString(R.string.twentySecWarning)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun finishWarning() {
        timerRingSound.start()
        Handler(Looper.getMainLooper()).postDelayed({
            val text = getString(R.string.finishWarning)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        }, 5000)
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        timer.cancel()
        super.onDestroy()
    }
}