package com.example.eventdicobyarafin.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventdicobyarafin.data.api.responses.ListEventsItem
import com.example.eventdicobyarafin.data.utils.Result
import com.example.eventdicobyarafin.databinding.FragmentFinishedBinding
import com.example.eventdicobyarafin.ui.adapter.EventAdapter
import com.example.eventdicobyarafin.ui.detail.EventDetailActivity
import com.example.eventdicobyarafin.ui.viewmodels.FinishedViewModel

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinishedViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter
    private lateinit var searchAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        eventAdapter = EventAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        searchAdapter = EventAdapter { event ->
            binding.searchView.hide()
            navigateToDetail(event)
        }
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView.editText.addTextChangedListener { editable ->
            val query = editable?.toString().orEmpty()
            viewModel.fetchFinishedEvents(query)
        }
    }

    private fun observeViewModel() {
        viewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    eventAdapter.submitList(result.data)
                    searchAdapter.submitList(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToDetail(event: ListEventsItem) {
        val eventId = event.id ?: return
        val intent = Intent(requireContext(), EventDetailActivity::class.java).apply {
            putExtra(EventDetailActivity.EXTRA_EVENT_ID, eventId)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}