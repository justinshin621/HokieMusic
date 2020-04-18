package com.example.hw4

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MusicService: Service() {

    var musicPlayer: MusicPlayer? = null

    companion object{
    val COMPLETE_INTENT = "complete intent"
    val MUSICNAME = "music name"}


    inner class MyBinder: Binder(){
        fun getService():MusicService{
            return this@MusicService
        }
    }

    private val iBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder? {
       return iBinder
    }

    fun onUpdateMusicName(musicName: String) {

        val intent = Intent(COMPLETE_INTENT)
        intent.putExtra(MUSICNAME, musicName)
        sendBroadcast(intent)
    }


    override fun onCreate() {
        super.onCreate()
        musicPlayer = MusicPlayer(this)
    }


    fun updateIndex(musicIndex : Int){
        musicPlayer?.setIndex(musicIndex)
    }

    fun updateIndexE1(effectIndex : Int){
        musicPlayer?.effectIndex(effectIndex)
    }
    fun updateIndexE2(effectIndex : Int){
        musicPlayer?.effectIndex2(effectIndex)
    }
    fun updateIndexE3(effectIndex : Int){
        musicPlayer?.effectIndex3(effectIndex)
    }

    // we figuring out this jauntz now
    fun playEffect1(seekBarPercent : Int) {
        musicPlayer?.playEffect1(seekBarPercent)
    }

    fun playEffect2(seekBarPercent : Int) {
        musicPlayer?.playEffect2(seekBarPercent)
    }

    fun playEffect3(seekBarPercent: Int){
        musicPlayer?.playEffect3(seekBarPercent)
    }

    fun getPoster() : Int{
        return musicPlayer!!.getPoster()
    }

    fun startMusic() {
        musicPlayer?.playMusic()
    }

    fun pauseMusic() {
        musicPlayer?.pauseMusic()
    }

    fun resumeMusic() {
        musicPlayer?.resumeMusic()
    }

    fun stopMusic() {
        musicPlayer?.stopMusic()
    }

    fun resetMusic(){
        musicPlayer?.resetMusic()
    }
    fun getPlayingStatus(): Int {
        return musicPlayer?.getMusicStatus() ?: -1
    }





}