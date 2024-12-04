package com.cinemate.app.ui.fragments.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.cinemate.app.R
import com.cinemate.app.data.repositories.ImageRepository
import com.cinemate.app.databinding.FragmentFilmesCadastradosBinding
import com.cinemate.app.utils.FileUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class FilmesCadastradosFragment : Fragment() {
    private var _binding: FragmentFilmesCadastradosBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageRepository: ImageRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmesCadastradosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializando o ImageRepository
        imageRepository = ImageRepository(requireContext(), FirebaseFirestore.getInstance())

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_filmesCadastradosFragment_to_gestaoFilmeFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
