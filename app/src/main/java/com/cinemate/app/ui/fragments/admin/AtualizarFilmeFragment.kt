package com.cinemate.app.ui.fragments.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemate.app.R
import com.cinemate.app.data.models.Movie
import com.cinemate.app.data.repositories.ImageRepository
import com.cinemate.app.data.repositories.MovieRepository
import com.cinemate.app.databinding.FragmentAtualizarFilmeBinding
import com.cinemate.app.ui.adapters.MoviesAdapter
import com.cinemate.app.utils.FileUtils
import com.cinemateapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AtualizarFilmeFragment: Fragment() {

    private var _binding: FragmentAtualizarFilmeBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null
    private lateinit var imageRepository: ImageRepository
    private lateinit var movieRepository: MovieRepository

    private val selectedGeneros = mutableListOf<String>()
    private val selectedPlataformas = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAtualizarFilmeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageRepository = ImageRepository(requireContext(), FirebaseFirestore.getInstance())
        movieRepository = MovieRepository(FirebaseFirestore.getInstance())

        setupGeneroRecyclerView()
        setupOndeAssistirRecyclerView()

        binding.btnUpload.setOnClickListener {
            openImagePicker()
        }

        binding.btnAtualizarFilme.setOnClickListener {
            updateMovie()
        }
    }

    private fun setupGeneroRecyclerView() {
        val generosAdapter = MoviesAdapter.GenerosAdapter(Constants.generosList) { genero, isChecked ->
            if (isChecked) {
                selectedGeneros.add(genero)
            } else {
                selectedGeneros.remove(genero)
            }
        }
        binding.generoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = generosAdapter
        }
    }

    private fun setupOndeAssistirRecyclerView() {
        val ondeAssistirAdapter = MoviesAdapter.OndeAssistirAdapter(Constants.ondeAssistirList) { plataforma, isChecked ->
            if (isChecked) {
                selectedPlataformas.add(plataforma.nome)
            } else {
                selectedPlataformas.remove(plataforma.nome)
            }
        }
        binding.ondeAssistirRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ondeAssistirAdapter
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                binding.btnUpload.setImageURI(uri)
            }
        }
    }

    private fun updateMovie() {
        val titulo = binding.tituloInput.text.toString().trim()
        val duracao = binding.duracaoInput.text.toString().trim()
        val faixaEtariaString = binding.faixaEtariaInput.text.toString().trim()
        val anoString = binding.anoInput.text.toString().trim()
        val sinopse = binding.sinopseInput.text.toString().trim()
        val elenco = binding.elencoInput.text.toString().trim()

        if (titulo.isEmpty() || duracao.isEmpty() || faixaEtariaString.isEmpty() || anoString.isEmpty() || sinopse.isEmpty() || elenco.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedGeneros.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, selecione pelo menos um gênero.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedPlataformas.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, selecione pelo menos uma plataforma.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedImageUri == null) {
            Toast.makeText(requireContext(), "Selecione uma imagem para o filme.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val imagePath = FileUtils.getPath(requireContext(), selectedImageUri!!) ?: throw Exception("Erro ao obter o caminho da imagem")

                val imageUrl = imageRepository.uploadImage(
                    imagePath = imagePath,
                    documentId = titulo,
                    collectionName = "filmes"
                ) ?: throw Exception("Erro ao fazer upload da imagem")

                val faixaEtaria = try {
                    faixaEtariaString.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Faixa etária deve ser um número válido.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val ano = try {
                    anoString.toInt()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Faixa etária deve ser um número válido.", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val movie = Movie(
                    titulo = titulo,
                    duracao = duracao,
                    faixaEtaria = faixaEtaria,
                    ano = ano,
                    sinopse = sinopse,
                    atoresPrincipais = elenco,
                    imagemUrl = imageUrl,
                    genero = selectedGeneros,
                    ondeAssistir = selectedPlataformas
                )

                movieRepository.addMovie(movie)

                Toast.makeText(requireContext(), "Filme cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Erro ao cadastrar o filme: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 101
    }
}
