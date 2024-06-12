package com.example.storeslist.presentation.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeslist.databinding.FragmentListBinding
import com.example.storeslist.presentation.adapter.StoreAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StoreAdapter
    private val viewModel: StoreViewModel by viewModels()
    private var isLoading = false
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
        observeViewModel()
        setupSwipeRefresh()
        viewModel.fetchStores(PER_PAGE, INITIAL_PAGE)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stores.collect { storeList ->
                    Log.d("ListFragment", "Store list updated: $storeList")
                    adapter.updateStores(storeList)
                    isLoading = false // Reset loading state after data is loaded
                }
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipe.isRefreshing = isLoading
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

                if (!isLoading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    isLoading = true
                    viewModel.fetchStores(
                        PER_PAGE,
                        viewModel.getCurrentPage() + 1
                    ) // Fetch next page of data
                }
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.swipe.setOnRefreshListener {
            viewModel.fetchStores(PER_PAGE, INITIAL_PAGE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PER_PAGE = 10
        private const val INITIAL_PAGE = 1
    }

}