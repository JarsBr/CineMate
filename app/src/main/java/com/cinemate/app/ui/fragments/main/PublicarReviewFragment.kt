package com.cinemate.app.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinemate.app.R
import com.cinemate.app.databinding.FragmentDetalhesFilmeBinding
import com.cinemate.app.databinding.FragmentPublicarReviewBinding
import com.cinemate.app.ui.adapters.ReviewsAdapter
import com.cinemate.app.viewModel.ReviewViewModel


class PublicarReviewFragment : Fragment() {
    private var _binding: FragmentPublicarReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicarReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getString("movieId") ?: return

        viewModel.fetchReviews(movieId)

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            binding.recyclerViewReviews.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = ReviewsAdapter(reviews) { review ->
                    val bundle = Bundle().apply {
                        putString("reviewId", review.id)
                    }
                    openRespostasReviewFragment(bundle)
                }
            }
        }

        binding.buttonAddReview.setOnClickListener {
            val bundle = Bundle().apply {
                putString("movieId", movieId)
            }
            openAdicionarReviewFragment(bundle)
        }

    }

    private fun openAdicionarReviewFragment(bundle: Bundle) {
        val adicionarReviewFragment = AdicionarReviewFragment().apply {
            arguments = bundle
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, adicionarReviewFragment, "AdicionarReview")
            .addToBackStack("AdicionarReview")
            .commit()
    }

    private fun openRespostasReviewFragment(bundle: Bundle) {
        val respostasReviewFragment = RespostasReviewFragment().apply {
            arguments = bundle
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, respostasReviewFragment, "RespostasReview")
            .addToBackStack("RespostasReview")
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        activity?.findViewById<View>(R.id.bottomNavigation)?.visibility = View.VISIBLE
        _binding = null
    }
}


