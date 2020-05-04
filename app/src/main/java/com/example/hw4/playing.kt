package com.example.hw4

import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.android.synthetic.main.fragment_playing.*
import java.util.Calendar

class playing : Fragment(), View.OnClickListener {

    var play_pause: Button? = null
    var restart: Button? = null

    var position : Int = 1
    var effectPosition1 : Int = 1
    var effectPosition2 : Int = 1
    var effectPosition3 : Int = 1

    var seekBarPercent1 : Int = 0
    var seekBarPercent2 : Int = 0
    var seekBarPercent3 : Int = 0

    var musicService: MusicService? = null
    var musicCompletionReceiver: MusicCompletionReceiver? = null
    var startMusicServiceIntent: Intent? = null
    var isInitialized = false
    var isBound = false


    companion object {
        const val TAG = "HW4_TAG"
        const val USERNAME = "justinshin"
        const val URL = "https://webhook.site/"
        const val ROUTE ="6fc64cf9-067d-437a-94ec-8a9d999b0d5d" //TODO: this is likely to expire. Try your own URL.
        const val INITIALIZE_STATUS = "intialization status"
        const val MUSIC_PLAYING = "music playing"
    }

    private val musicServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {

            val binder = iBinder as MusicService.MyBinder
            musicService = binder.getService()
            isBound = true
            when (musicService?.getPlayingStatus()) {
                0 -> play_pause?.setText("Start")
                1 -> play_pause?.setText("Pause")
                2 -> play_pause?.setText("Resume")
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            musicService = null
            isBound = false
        }
    }

    private fun appendEvent(event: String){

        WorkManager.getInstance().beginUniqueWork(TAG, ExistingWorkPolicy.KEEP, OneTimeWorkRequestBuilder<UploadWorker>().setInputData(
            workDataOf("username" to USERNAME, "event" to event, "date" to Calendar.getInstance().time.toString())
        )
            .build()).enqueue()
    }

    override fun onPause() {
        super.onPause()
        if (isBound) {
            context?.unbindService(musicServiceConnection)
            isBound = false
        }
        context?.unregisterReceiver(musicCompletionReceiver)

        appendEvent("onPause")
    }

    override fun onResume() {
        super.onResume()
        if (isInitialized && !isBound) {
            context?.bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE)
        }
        context?.registerReceiver(musicCompletionReceiver, IntentFilter(MusicService.COMPLETE_INTENT))
        appendEvent("onResume")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized)
        outState.putString(MUSIC_PLAYING, background_title?.text.toString())
        super.onSaveInstanceState(outState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var items : ArrayList<String> = arguments?.getStringArrayList("song")!!
        var effectItems1 : ArrayList<String> = arguments?.getStringArrayList("effect1")!!
        var effectItems2 : ArrayList<String> = arguments?.getStringArrayList("effect2")!!
        var effectItems3 : ArrayList<String> = arguments?.getStringArrayList("effect3")!!

        var seek1 : String = arguments?.getString("seek1")!!
        var seek2 : String = arguments?.getString("seek2")!!
        var seek3 : String = arguments?.getString("seek3")!!



        play_pause = view.findViewById(R.id.play_pause)

        position = items[1].toInt()
        effectPosition1 = effectItems1[1].toInt()
        effectPosition2 = effectItems2[1].toInt()
        effectPosition3 = effectItems3[1].toInt()
        seekBarPercent1 = seek1.toInt()
        seekBarPercent2 = seek2.toInt()
        seekBarPercent3 = seek3.toInt()



        play_pause?.setOnClickListener(this)

        if (savedInstanceState != null) {
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS)
        }
        startMusicServiceIntent = Intent(this.activity, MusicService::class.java)
        if (!isInitialized) {
            view.context.startService(startMusicServiceIntent)
            isInitialized = true
        }
        musicCompletionReceiver = MusicCompletionReceiver()



        val song = items[0]
        val songTitle : TextView = view.findViewById(R.id.background_title)
        val picture : ImageView = view.findViewById(R.id.background_poster)

        if (song == "Go Tech Go!"){
            picture.setImageResource(R.drawable.gotechgoposter)
        }
        else if (song == "Enter Sandman"){
            picture.setImageResource(R.drawable.entersandmanposter)
        }
        else if (song == "Victory March") {
            picture.setImageResource(R.drawable.victorymarchposter)
        }

        songTitle.text = song

        view.findViewById<Button>(R.id.toEdit).setOnClickListener{
            musicService?.stopMusic()
            view.findNavController().navigate(R.id.action_playing_to_editing)
            appendEvent("Moved to Edit screen")
        }

        view.findViewById<Button>(R.id.restart).setOnClickListener{
            musicService?.resetMusic()
            play_pause?.setText("Start")
            appendEvent("Restarted song")
        }


    }

    override fun onClick(v: View?) {
        if (isBound) {
            musicService?.updateIndex(position)
            musicService?.updateIndexE1(effectPosition1)
            musicService?.updateIndexE2(effectPosition2)
            musicService?.updateIndexE3(effectPosition3)

            when (musicService?.getPlayingStatus()) {
                0 -> {
                    musicService?.startMusic()
                    musicService?.playEffect1(seekBarPercent1)
                    musicService?.playEffect2(seekBarPercent2)
                    musicService?.playEffect3(seekBarPercent3)
                    play_pause?.setText("Pause")
                    appendEvent("Pressed Play")
                }
                1 -> {
                    musicService?.pauseMusic()

                    play_pause?.setText("Resume")
                    appendEvent("Pressed Pause")
                }
                2 -> {
                    musicService?.resumeMusic()
                    play_pause?.setText("Pause")
                    appendEvent("Pressed Resume")
                }
            }

            when (musicService?.getPoster()) {
                1 -> {
                    background_poster.setImageResource(R.drawable.clappingposter)
                }
                2 -> {
                    background_poster.setImageResource(R.drawable.cheeringposter)
                }
                3 -> {
                    background_poster.setImageResource(R.drawable.gohokiesposter)
                }

            }
        }
    }

}
