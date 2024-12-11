package com.cinemate.app.ui.fragments.admin

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinemate.app.R
import com.cinemate.app.data.models.Response
import com.cinemate.app.data.models.Review
import com.cinemate.app.databinding.FragmentGestaoRepostasBinding
import com.cinemate.app.ui.adapters.RespostasAdapter
import com.cinemate.app.viewModel.ResponseViewModel
import com.google.firebase.firestore.FirebaseFirestore

class GestaoRepostasFragment : Fragment() {

    private var _binding: FragmentGestaoRepostasBinding? = null
    private val binding get() = _binding!!
    private val responseViewModel: ResponseViewModel by viewModels()
    private lateinit var reviewId: String  // Declare a variável reviewId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGestaoRepostasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Certifique-se de que o reviewId é obtido corretamente
        reviewId = arguments?.getString("reviewId") ?: return

        // Carregar as respostas para o reviewId
        responseViewModel.fetchRespostas(reviewId)

        responseViewModel.respostas.observe(viewLifecycleOwner) { respostas ->
            val adapter = RespostasAdapter(respostas)
            binding.recyclerViewRespostas.apply {
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter = adapter
            }

            // Configurar swipe para deletar
            setupSwipeToDelete(binding.recyclerViewRespostas, adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteResposta(respostaId: String, adapter: RespostasAdapter, position: Int) {
        val db = FirebaseFirestore.getInstance()
        db.collection("respostas")
            .document(respostaId) // Usando o campo id da resposta
            .delete()
            .addOnSuccessListener {
                // Excluir do Firestore foi bem-sucedido, agora remover do adapter
                adapter.removeAt(position)

                // Atualize a lista de respostas no ViewModel
                responseViewModel.fetchRespostas(reviewId)

                Log.d("GestaoRepostasFragment", "Resposta deletada com sucesso.")
                // Exibir a mensagem de sucesso
                Toast.makeText(requireContext(), "Resposta deletada com sucesso", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Log.e("GestaoRepostasFragment", "Erro ao deletar resposta: ${e.message}")
                // Exibir a mensagem de erro
                Toast.makeText(requireContext(), "Falha ao deletar a resposta", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView, adapter: RespostasAdapter) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val resposta = adapter.getRespostaAt(position)

                // Aqui, você deve usar o campo correto para deletar (como `resposta.id` ou outro ID de resposta).
                deleteResposta(resposta.id, adapter, position) // Ajuste aqui para o ID correto
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.RED)
                val deleteIcon = recyclerView.context.getDrawable(R.drawable.ic_delete)

                // Configuração do fundo vermelho
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                // Configuração do ícone de lixeira
                deleteIcon?.let {
                    val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                    val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    val iconTop = itemView.top + iconMargin
                    val iconBottom = itemView.bottom - iconMargin
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(c)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}



