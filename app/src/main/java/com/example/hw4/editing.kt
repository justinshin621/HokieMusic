package com.example.hw4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.*
import kotlin.collections.ArrayList

class editing : Fragment() {



    private fun appendEvent(event: String){

        WorkManager.getInstance().beginUniqueWork(
            playing.TAG, ExistingWorkPolicy.KEEP, OneTimeWorkRequestBuilder<UploadWorker>().setInputData(
                workDataOf("username" to playing.USERNAME, "event" to event, "date" to Calendar.getInstance().time.toString())
            )
                .build()).enqueue()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_editing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backgroundMusicSpinner = view.findViewById<Spinner>(R.id.background_music)
        val effectSpinner1 = view.findViewById<Spinner>(R.id.effect_spinner1)
        val effectSpinner2 = view.findViewById<Spinner>(R.id.effect_spinner2)
        val effectSpinner3 = view.findViewById<Spinner>(R.id.effect_spinner3)

        val seek1 : SeekBar = view.findViewById(R.id.overlap_time1)
        val seek2 = view.findViewById<SeekBar>(R.id.overlap_time2)
        val seek3 = view.findViewById<SeekBar>(R.id.overlap_time3)


        val bundle = Bundle()

        bundle.putString("seek1", "0")
        bundle.putString("seek2", "0")
        bundle.putString("seek3", "0")


        seek1?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(this@editing.activity,
                    "Effect 1 when song at: " + seekBar?.progress + "%",
                    Toast.LENGTH_SHORT).show()

                var seekStuff1 : String = seekBar?.progress.toString()
                bundle.putString("seek1", seekStuff1)
                appendEvent("Set Effect 1 to begin at " + seekBar?.progress + "% of the song")
            }

        })

        seek2?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(this@editing.activity,
                    "Effect 2 when song at: " + seekBar?.progress + "%",
                    Toast.LENGTH_SHORT).show()
                var seekStuff2 : String = seekBar?.progress.toString()
                bundle.putString("seek2", seekStuff2)
                appendEvent("Set Effect 2 to begin at " + seekBar?.progress + "% of the song")

            }

        })

        seek3?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(this@editing.activity,
                    "Effect 3 when song at: " + seekBar?.progress + "%",
                    Toast.LENGTH_SHORT).show()
                var seekStuff3 : String = seekBar?.progress.toString()
                bundle.putString("seek3", seekStuff3)

                appendEvent("Set Effect 3 to begin at " + seekBar?.progress + "% of the song")

            }

        })




        backgroundMusicSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var stuff : ArrayList<String> =
                    mutableListOf<String>(parent?.getItemAtPosition(position).toString(), position.toString()) as ArrayList<String>
                bundle.putStringArrayList("song", stuff)
                appendEvent("Seleceted Song " + parent?.getItemAtPosition(position).toString())

            }

        }

        effectSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var stuff1 : ArrayList<String> =
                    mutableListOf<String>(parent?.getItemAtPosition(position).toString(), position.toString()) as ArrayList<String>
                bundle.putStringArrayList("effect1", stuff1)
                appendEvent("Seleceted Effect 1 " + parent?.getItemAtPosition(position).toString())

            }

        }
        effectSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var stuff2 : ArrayList<String> =
                    mutableListOf<String>(parent?.getItemAtPosition(position).toString(), position.toString()) as ArrayList<String>
                bundle.putStringArrayList("effect2", stuff2)

                appendEvent("Seleceted Effect 2 " + parent?.getItemAtPosition(position).toString())

            }

        }
        effectSpinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var stuff3 : ArrayList<String> =
                    mutableListOf<String>(parent?.getItemAtPosition(position).toString(), position.toString()) as ArrayList<String>
                bundle.putStringArrayList("effect3", stuff3)

                appendEvent("Seleceted Effect 3 " + parent?.getItemAtPosition(position).toString())

            }

        }


        view.findViewById<Button>(R.id.toPlay).setOnClickListener{
            view.findNavController().navigate(R.id.action_editing_to_playing, bundle)
            appendEvent("Moved to playing screen")

        }

    }    // onViewCreate



} //THE END
