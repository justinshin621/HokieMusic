package com.example.hw4
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import java.io.IOException
import java.util.*


class MusicPlayer(val musicService: MusicService): MediaPlayer.OnCompletionListener {

    val MUSICPATH = arrayOf(R.raw.gotechgo, R.raw.sandman, R.raw.victorymarch)

    val MUSICNAME = arrayOf("Go Tech Go!", "Enter Sandman", "Victory March")

    val EFFECTPATH = arrayOf(R.raw.clapping, R.raw.cheering, R.raw.lestgohokies)
    val EFFECTPATH2 = arrayOf(R.raw.cheering, R.raw.clapping, R.raw.lestgohokies)
    val EFFECTPATH3 = arrayOf(R.raw.lestgohokies, R.raw.clapping, R.raw.cheering)

    lateinit var player: MediaPlayer
    lateinit var effect1: MediaPlayer
    lateinit var effect2: MediaPlayer
    lateinit var effect3: MediaPlayer

    var playerRelease = false
    var effect1Release = false
    var effect2Release = false
    var effect3Release = false

    var currentPosition = 0
    var currentPositionEffect1 = 0
    var currentPositionEffect2 = 0
    var currentPositionEffect3 = 0

    var musicIndex = 0
    var effectIndex1 = 0
    var effectIndex2 = 0
    var effectIndex3 = 0

    var effectPoster : Int = 0

    private var effectTimer: Timer? = null
    private var effectTimer2: Timer? = null
    private var effectTimer3: Timer? = null



    private var musicStatus = 0//0: before starts 1: playing 2: paused

    fun getPoster() : Int {
        return effectPoster
    }

    fun getMusicStatus(): Int {
        return musicStatus
    }

    fun getMusicName(): String {
        return MUSICNAME[musicIndex]
    }

    fun setIndex(index : Int){
        this.musicIndex = index
    }

    fun effectIndex(index : Int){
        this.effectIndex1 = index
    }

    fun effectIndex2(index: Int){
        this.effectIndex2 = index
    }

    fun effectIndex3(index: Int){
        this.effectIndex3 = index
    }

    fun playEffect1(seekBarPercent : Int) {

                effect1 = MediaPlayer.create(musicService.applicationContext, EFFECTPATH[effectIndex1])
                effect1Release = false
                effect1.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                try {
                    effect1.setOnCompletionListener{
                        effect1.release()
                        effect1Release = true
                    }
                    if (player.currentPosition == (player.duration * (seekBarPercent * 0.01)).toInt()){
                        effectPoster = 1
                        effect1.start()
                    }

                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
    }


    fun playEffect2(seekBarPercent: Int) {

                effect2 = MediaPlayer.create(musicService.applicationContext, EFFECTPATH2[effectIndex2])
                effect2Release = false
                effect2.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                try {
                    effect2.setOnCompletionListener{
                        effect2.release()
                        effect2Release = true
                    }
                    if (player.currentPosition == (player.duration * (seekBarPercent * 0.01)).toInt()){
                        effectPoster = 2
                        effect2.start()
                    }

                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
    }


    fun playEffect3(seekBarPercent: Int) {

        effect3 = MediaPlayer.create(musicService.applicationContext, EFFECTPATH3[effectIndex3])
        effect3Release = false
        effect3.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
        try {
            effect3.setOnCompletionListener{
                effect3.release()
                effect3Release = true
            }
            Log.d("where's player at now?", player.currentPosition.toString())
            Log.d("effect 3 length is ", player.duration.toString())
            Log.d("when effect 3 will come", (player.duration * (seekBarPercent * 0.01)).toString())
            if (player.currentPosition == (player.duration * (seekBarPercent * 0.01)).toInt()){
                effectPoster = 3
                effect3.start()
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun stopMusic() {
        if (!playerRelease) {
            player.stop()
        }
        if (!effect1Release) {
            effect1.stop()
        }
        if (!effect2Release) {
            effect2.stop()
        }
        if (!effect3Release) {
            effect3.stop()
        }
        musicStatus = 0
    }

    fun playMusic() {

        effectTimer = Timer()

        //effectTimer.
        player = MediaPlayer.create(musicService.applicationContext, MUSICPATH[musicIndex])
        playerRelease = false
        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
        try {
            player.setOnCompletionListener(this)
            player.start()

            musicService.onUpdateMusicName(getMusicName())
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        musicStatus = 1
    }

    fun pauseMusic() {



        if (player.isPlaying()) {
            player.pause()
            currentPosition = player.getCurrentPosition()
            musicStatus = 2
        }

        if (!effect1Release) {
            if (effect1.isPlaying()) {

                effect1.pause()
                currentPositionEffect1 = effect1.getCurrentPosition()
            }
        }

        if (!effect2Release){
            if (effect2.isPlaying()) {
                effect2.pause()
                currentPositionEffect2 = effect2.getCurrentPosition()
            }
        }

        if (!effect3Release) {
            if (effect3.isPlaying()) {
                effect3.pause()
                currentPositionEffect3 = effect3.getCurrentPosition()
            }
        }

    }

    fun resumeMusic() {
        player.seekTo(currentPosition)
        player.start()
        if (!effect1Release) {
            effect1.seekTo(currentPositionEffect1)
            effect1.start()
        }

        if (!effect2Release) {
            effect2.seekTo(currentPositionEffect2)
            effect2.start()
        }

        if (!effect3Release) {
            effect3.seekTo(currentPositionEffect3)
            effect3.start()
        }


        musicStatus = 1
    }

    fun resetMusic(){
        if (!playerRelease) {
            player.stop()
            player.seekTo(0)
        }
        if (!effect1Release) {
            effect1.stop()
            effect1.seekTo(0)  // NUMBERS HERE WILL CHANGE
        }

        if (!effect2Release) {
            effect2.stop()
            effect2.seekTo(0) // NUMBERS HERE WILL CHANGE
        }

        if (!effect3Release) {
            effect3.stop()
            effect3.seekTo(0)   // NUMBERS HERE WILL CHANGE
        }

        musicStatus = 0
    }

    // WE GOTTA LEARN HOW TO JUST END
    override fun onCompletion(mp: MediaPlayer?) {
        player.release()
        playerRelease = true
    }




}