package com.example.eventdicobyarafin.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventdicobyarafin.data.api.responses.ListEventsItem
import com.example.eventdicobyarafin.data.utils.Result
import com.example.eventdicobyarafin.databinding.FragmentUpcomingBinding
import com.example.eventdicobyarafin.ui.adapter.EventAdapter
import com.example.eventdicobyarafin.ui.detail.EventDetailActivity
import com.example.eventdicobyarafin.ui.viewmodels.UpcomingViewModel

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpcomingViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    eventAdapter.submitList(result.data)
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