package com.cinemate.app.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentGestaoReviewBinding
import com.cinemate.app.ui.adapters.ReviewsAdapter
import com.cinemate.app.viewModel.ReviewViewModel

class GestaoReviewFragment : Fragment() {

    private var _binding: FragmentGestaoReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGestaoReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmId = arguments?.getString("filmId") ?: return

        viewModel.fetchReviews(filmId)

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            binding.recyclerViewReviews.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ReviewsAdapter(reviews) { review ->
                    val bundle = Bundle().apply {
                        putString("reviewId", review.id)
                    }
                    findNavController().navigate(R.id.action_gestaoReviewFragment_to_gestaoRepostasFragment, bundle)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



