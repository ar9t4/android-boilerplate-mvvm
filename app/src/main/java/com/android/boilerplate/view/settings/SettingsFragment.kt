package com.android.boilerplate.view.settings

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
import com.android.boilerplate.databinding.FragmentSettingsBinding
import com.android.boilerplate.model.data.aide.SettingItem
import com.android.boilerplate.viewmodel.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var recyclerViewState: Parcelable? = null
    private lateinit var adapter: SettingsAdapter
    private lateinit var binding: FragmentSettingsBinding

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
                R.layout.fragment_settings,
                container,
                false
            )
            binding.listener = this
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val context = requireContext()
            // observers on live data
            viewModel.settingItems.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    // setup data
                    setup(settingsItems = it)
                }
            }
            viewModel.themes.observe(viewLifecycleOwner) {
                it?.let {
                    viewModel.updateSelectedTheme(context = context)
                }
            }
            viewModel.languages.observe(viewLifecycleOwner) {
                it?.let {
                    viewModel.updateSelectedLanguage(context = context)
                }
            }
            // fetch data
            viewModel.fetchSettingItems(context = context)
        }
    }

    private fun setup(settingsItems: List<SettingItem>) {
        if (settingsItems.isEmpty()) {
            // show no content view
        } else {
            setupMoreItemsAdapters(settingsItems = settingsItems)
        }
    }

    private fun setupMoreItemsAdapters(settingsItems: List<SettingItem>) {
        if (!::adapter.isInitialized) {
            adapter = SettingsAdapter(context = requireContext()) {
                when (it.id) {
                    1 -> {
                        // toggle notification
                        viewModel.toggleNotification(context = requireContext())
                    }

                    2 -> {
                        onThemeClicked()
                    }

                    3 -> {
                        onLanguageClicked()
                    }
                }
            }
        }
        binding.apply {
            // save Recyclerview state before making change in data
            recyclerViewState = rvSettings.layoutManager?.onSaveInstanceState()
            rvSettings.adapter = adapter
            adapter.submitList(settingsItems)
            // restore Recyclerview state after setting updated data
            recyclerViewState?.let {
                rvSettings.layoutManager?.onRestoreInstanceState(it)
            }
        }
    }

    private fun onThemeClicked() {
        findNavController().navigate(
            SettingsFragmentDirections.actionDestSettingsToDestThemes()
        )
    }

    private fun onLanguageClicked() {
        findNavController().navigate(
            SettingsFragmentDirections.actionDestSettingsToDestLanguages()
        )
    }
}