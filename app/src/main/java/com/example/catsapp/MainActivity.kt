package com.example.catsapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import androidx.appcompat.app.AppCompatActivity

import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.catsapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val breedList = listOf("Loading...") // Placeholder list
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, breedList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.breedSpinner.adapter = adapter

        binding.breedSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedBreed = parent?.getItemAtPosition(position).toString()
                onBreedSelected(selectedBreed)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        fetchBreeds()
    }


    private fun onBreedSelected(breed: String) {
        val url = "https://api.thecatapi.com/v1/breeds/search?q=$breed"
        binding.loadingIndicator.visibility = View.VISIBLE
        val request = JsonArrayRequest(url,
            { response ->
                if (response.length() > 0) {
                    val breedDetails = response.getJSONObject(0)
                    val breedName = breedDetails.getString("name")
                    val breedTemperament = breedDetails.getString("temperament")
                    val breedOrigin = breedDetails.getString("origin")
                    val breedLifeSpan = breedDetails.getString("life_span")
                    val breedDescription = breedDetails.getString("description")

                    val breedImageId = breedDetails.getString("reference_image_id")
                    val breedImageUrl = "https://cdn2.thecatapi.com/images/${breedImageId}.jpg"

                    // Update the UI with the breed details
                    binding.breedName.text = breedName
                    binding.breedTemperament.text = breedTemperament
                    binding.Origin.text = breedOrigin
                    binding.breedLifeSpan.text = breedLifeSpan
                    binding.breedDescription.text = breedDescription


                    Picasso.get().load(breedImageUrl).into(binding.breedImage)

                }
                binding.loadingIndicator.visibility = View.GONE
            },
            { error ->

                // Handle the error
                Log.e("CatsApp", "Error fetching breed details: ${error.message}")
                binding.loadingIndicator.visibility = View.GONE

            })

        Volley.newRequestQueue(this).add(request)
    }


    private fun fetchBreeds() {
        val apiKey = getString(R.string.cat_api_key)
        val url = "https://api.thecatapi.com/v1/breeds?api_key=$apiKey"
        binding.loadingIndicator.visibility = View.VISIBLE


        val request = JsonArrayRequest(url,
            { response ->
                val breedList = mutableListOf<String>()
                for (i in 0 until response.length()) {
                    val breed = response.getJSONObject(i)
                    val breedName = breed.getString("name")
                    breedList.add(breedName)
                }
                // Update the Spinner with the breed list
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, breedList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.breedSpinner.adapter = adapter
                binding.loadingIndicator.visibility = View.GONE

            },
            { error ->
                // Handle the error
                Log.e("CatsApp", "Error fetching breeds: ${error.message}")
                Snackbar.make(findViewById(android.R.id.content), "Error fetching cat breeds", Snackbar.LENGTH_LONG).show()
                binding.loadingIndicator.visibility = View.GONE
            })


        Volley.newRequestQueue(this).add(request)
    }

}