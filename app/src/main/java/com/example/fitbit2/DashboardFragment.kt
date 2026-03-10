package com.example.fitbit2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitbit2.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSleepEntries()
    }

    private fun observeSleepEntries() {
        val database = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch {
            database.sleepDao().getAll().collectLatest { entries ->
                if (entries.isNotEmpty()) {
                    val average = entries.mapNotNull { it.hours }.average()
                    val min = entries.mapNotNull { it.hours }.minOrNull()
                    val max = entries.mapNotNull { it.hours }.maxOrNull()

                    binding.textViewAverageHours.text = "Average Hours: ${String.format("%.2f", average)}"
                    binding.textViewMinHours.text = "Min Hours: $min"
                    binding.textViewMaxHours.text = "Max Hours: $max"
                } else {
                    binding.textViewAverageHours.text = "Average Hours: 0"
                    binding.textViewMinHours.text = "Min Hours: 0"
                    binding.textViewMaxHours.text = "Max Hours: 0"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
