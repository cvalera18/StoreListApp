package com.example.storeslist.presentation.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeslist.databinding.FragmentListBinding
import com.example.storeslist.presentation.adapter.StoreAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StoreAdapter
    private val viewModel: StoreViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
        viewModel.fetchStores()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stores.collect { storeList ->
                    adapter.updateStores(storeList)
                }
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipe.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("Cerrar") {
                    snackbar.dismiss()
                }
                // Multiline for the snackbar
                val snackbarView = snackbar.view
                val textView =
                    snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                textView.maxLines = 5
                snackbar.show()
                viewModel.clearError()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = StoreAdapter(stores = emptyList())
        val llmanager = LinearLayoutManager(requireContext())
        binding.recyclerStoreList.layoutManager = llmanager
        binding.recyclerStoreList.adapter = adapter

        // Adding ScrollListener for the pagination

        binding.recyclerStoreList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = llmanager.childCount
                val totalItemCount = llmanager.itemCount
                val pastVisibleItems = llmanager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading.value!! && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    viewModel.fetchStores() // Fetch next page of data
                }
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipe.setOnRefreshListener {
            viewModel.fetchStores()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}