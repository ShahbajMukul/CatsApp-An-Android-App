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
    private lateinit var breedDetailsFragment: BreedDetailsFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create an instance of BreedDetailsFragment
        breedDetailsFragment = BreedDetailsFragment()

        // Add the fragment to the container
        supportFragmentManager.beginTransaction()
            .replace(R.id.breedDetailsContainer, breedDetailsFragment)
            .commit()

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
        // Call the updateBreedDetails method in the fragment
        breedDetailsFragment.updateBreedDetails(breed)
    }


// reference: KOTLIN Retrofit Tutorial | Part 1 | Simple GET Request | For Beginners | Easiest Way to API Call
// https://www.youtube.com/watch?v=5gFrXGbQsc8&ab_channel=YashNagayach
    private fun fetchBreeds() {
        val apiKey = getString(R.string.cat_api_key)
        val url = "https://api.thecatapi.com/v1/breeds?api_key=$apiKey"

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
            },
            { error ->
                // Handle the error
                Log.e("CatsApp", "Error fetching breeds: ${error.message}")
                Snackbar.make(findViewById(android.R.id.content), "Error fetching cat breeds", Snackbar.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(this).add(request)
    }


}