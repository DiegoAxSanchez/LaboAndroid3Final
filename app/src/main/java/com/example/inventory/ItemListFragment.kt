/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.databinding.ItemListFragmentBinding
import java.io.*
import java.util.*

/**
 * Main fragment displaying details for all items in the database.
 */
class ItemListFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val entrees = File("res/raw/produits.txt").readLines()
//        for (entree in entrees){
//            val info: List<String> = entree.split(";")
//            viewModel.addNewItem(info[0],info[1],info[2],info[3])
//        }
        _binding = ItemListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ItemListAdapter {
            val action =
                ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }

            viewModel.allCategories.observe(this.viewLifecycleOwner) { categories ->
                categories.let {
                    // Create an ArrayAdapter using the string array and a default spinner layout
                    binding.categorySpinner.adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
                }
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }
        binding.btnTotal.setOnClickListener {
            viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
                items.let {
                    val newFragment = TotalFragment(items)
                    newFragment.show(parentFragmentManager, "total")
                }
            }
        }
        binding.categorySpinner.onItemSelectedListener= object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (parentView != null) {

                    viewModel.searchByCategory(parentView.getItemAtPosition(position) as String)
                        .observe(viewLifecycleOwner) { items ->
                            items.let {
                                val newFragment = CategoryFragment(items)
                                newFragment.show(parentFragmentManager, "category")
                            }
                        }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

    }


}


