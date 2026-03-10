package com.example.fitbit2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitbit2.databinding.FragmentSleepListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SleepListFragment : Fragment() {

    private var _binding: FragmentSleepListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sleepAdapter: SleepAdapter
    private val sleepEntries = mutableListOf<SleepEntry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        binding.buttonAddEntry.setOnClickListener {
            val intent = Intent(requireContext(), AddEntryActivity::class.java)
            startActivity(intent)
        }

        observeSleepEntries()
    }

    private fun setupRecyclerView() {
        sleepAdapter = SleepAdapter(sleepEntries)
        binding.recyclerView.adapter = sleepAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeSleepEntries() {
        val database = AppDatabase.getDatabase(requireContext())
        lifecycleScope.launch {
            database.sleepDao().getAll().collectLatest { entries ->
                sleepEntries.clear()
                sleepEntries.addAll(entries)
                sleepAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
