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
import com.example.eventdicobyarafin.databinding.FragmentHomeBinding
import com.example.eventdicobyarafin.ui.adapter.CarouselAdapter
import com.example.eventdicobyarafin.ui.adapter.EventAdapter
import com.example.eventdicobyarafin.ui.detail.EventDetailActivity
import com.example.eventdicobyarafin.ui.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        carouselAdapter = CarouselAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvUpcomingEvent.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = carouselAdapter
        }

        eventAdapter = EventAdapter { event ->
            navigateToDetail(event)
        }
        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }
    }

    private fun observeViewModel() {
        var isUpcomingLoading = true
        var isFinishedLoading = true

        fun updateProgress() {
            if (isUpcomingLoading || isFinishedLoading) {
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.progressIndicator.visibility = View.GONE
            }
        }

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    isUpcomingLoading = true
                    updateProgress()
                }

                is Result.Success -> {
                    isUpcomingLoading = false
                    updateProgress()
                    carouselAdapter.submitList(result.data)
                }

                is Result.Error -> {
                    isUpcomingLoading = false
                    updateProgress()
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    isFinishedLoading = true
                    updateProgress()
                }

                is Result.Success -> {
                    isFinishedLoading = false
                    updateProgress()
                    eventAdapter.submitList(result.data)
                }

                is Result.Error -> {
                    isFinishedLoading = false
                    updateProgress()
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