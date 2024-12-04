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

        binding.btnUpload.setOnClickListener {
            selectImageFromGallery()
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_filmesCadastradosFragment_to_gestaoFilmeFragment)
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                val imagePath = FileUtils.getPath(requireContext(), imageUri) // Use um método para obter o caminho real
                if (imagePath != null) {
                    uploadImage(imagePath)
                } else {
                    Toast.makeText(context, "Erro ao obter o caminho da imagem.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImage(imagePath: String) {
        lifecycleScope.launch {
            val documentId = "kJNuxyEWjv3Dslhpjm4e" // Substitua pelo ID do documento
            val collectionName = "filmes" // Substitua pelo nome da coleção
            val imageUrl = imageRepository.uploadImage(imagePath, documentId, collectionName)
            if (imageUrl != null) {
                Toast.makeText(context, "Imagem enviada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao enviar imagem.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
