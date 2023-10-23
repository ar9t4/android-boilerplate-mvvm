package com.android.boilerplate.view.settings.theme

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.boilerplate.R
import com.android.boilerplate.base.view.BaseFragment
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.FragmentThemesBinding
import com.android.boilerplate.model.data.aide.Theme
import com.android.boilerplate.viewmodel.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class ThemesFragment : BaseFragment() {

    private var recyclerViewState: Parcelable? = null
    private lateinit var adapter: ThemesAdapter
    private lateinit var binding: FragmentThemesBinding

    private val viewModel: SettingsViewModel by activityViewModels()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_themes,
                container,
                false
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ibBack.setOnClickListener { findNavController().navigateUp() }
            // observers on live data
            viewModel.themes.observe(viewLifecycleOwner) {
                if (it == null) {
                    // fetch data
                    viewModel.fetchThemes()
                } else {
                    // setup data
                    setup(themes = it)
                }
            }
        }
    }

    private fun setup(themes: List<Theme>) {
        if (themes.isEmpty()) {
            // show no content view
        } else {
            setupThemesAdapters(themes = themes)
        }
    }

    private fun setupThemesAdapters(themes: List<Theme>) {
        if (!::adapter.isInitialized) {
            adapter = ThemesAdapter(requireContext()) {
                viewModel.setTheme(it)
                activity?.recreate()
            }
        }
        binding.apply {
            // save Recyclerview state before making change in data
            recyclerViewState = rvThemes.layoutManager?.onSaveInstanceState()
            rvThemes.adapter = adapter
            adapter.submitList(themes)
            // restore Recyclerview state after setting updated data
            recyclerViewState?.let {
                rvThemes.layoutManager?.onRestoreInstanceState(it)
            }
        }
    }
}