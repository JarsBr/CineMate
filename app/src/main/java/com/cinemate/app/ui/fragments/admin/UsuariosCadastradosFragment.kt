package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemate.app.databinding.FragmentUsuariosCadastradosBinding
import com.cinemate.app.ui.adapters.UsersAdapter
import com.cinemate.app.viewModel.UserViewModel

class UsuariosCadastradosFragment : Fragment() {

    private var _binding: FragmentUsuariosCadastradosBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuariosCadastradosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o adapter com uma lista vazia
        adapter = UsersAdapter(emptyList())

        // Configura o RecyclerView
        binding.recyclerViewUsuarios.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewUsuarios.adapter = adapter

        // Observa a lista de usuários e atualiza o adapter
        userViewModel.users.observe(viewLifecycleOwner) { users ->
            if (users.isNotEmpty()) {
                adapter.updateUsers(users)  // Atualiza a lista do adapter
            } else {
                // Exibe uma mensagem ou algo se a lista estiver vazia
                Toast.makeText(context, "Nenhum usuário encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        // Carrega os usuários do ViewModel
        userViewModel.fetchUsers()

        // Filtro de pesquisa
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterUsers(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterUsers(query: String) {
        val filteredList = userViewModel.users.value?.filter { user ->
            val name = user["nome"].toString().lowercase()
            val email = user["email"].toString().lowercase()
            name.contains(query.lowercase()) || email.contains(query.lowercase())
        }
        filteredList?.let { adapter.updateUsers(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
