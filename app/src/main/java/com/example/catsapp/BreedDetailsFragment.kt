package com.example.catsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.catsapp.databinding.FragmentBreedDetailsBinding

class BreedDetailsFragment : Fragment() {
    private var _binding: FragmentBreedDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBreedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun updateBreedDetails(breed: Breed) {
        // Update the views with the breed details
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}