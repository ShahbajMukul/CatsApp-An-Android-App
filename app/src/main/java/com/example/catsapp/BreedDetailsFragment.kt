package com.example.catsapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.catsapp.databinding.FragmentBreedDetailsBinding
import com.squareup.picasso.Picasso

class BreedDetailsFragment : Fragment() {
    private var _binding: FragmentBreedDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBreedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun updateBreedDetails(breed: String) {
        // Move the breed details fetching code here
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
                    binding.loadingIndicator.visibility = View.GONE

                    binding.breedName.text = breedName
                    binding.breedTemperament.text = breedTemperament
                    binding.origin.text = breedOrigin
                    binding.breedLifeSpan.text = breedLifeSpan
                    binding.breedDescription.text = breedDescription

                    Picasso.get().load(breedImageUrl).into(binding.breedImage)
                }
            },
            { error ->
                // Handle the error
                binding.loadingIndicator.visibility = View.GONE

                Log.e("CatsApp", "Error fetching breed details: ${error.message}")
            })

        Volley.newRequestQueue(requireContext()).add(request)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}