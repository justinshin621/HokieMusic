package com.example.hw4
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import androidx.fragment.app.Fragment

class MusicCompletionReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val musicName = intent.getStringExtra(MusicService.MUSICNAME)
         //mainActivity?.updateName(musicName)
    }
}