package com.android.boilerplate.view.settings.language

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
import com.android.boilerplate.databinding.FragmentLanguagesBinding
import com.android.boilerplate.model.data.aide.Language
import com.android.boilerplate.viewmodel.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class LanguagesFragment : BaseFragment() {

    private var recyclerViewState: Parcelable? = null
    private lateinit var adapter: LanguagesAdapter
    private lateinit var binding: FragmentLanguagesBinding

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
                R.layout.fragment_languages,
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
            viewModel.languages.observe(viewLifecycleOwner) {
                if (it == null) {
                    // fetch data
                    viewModel.fetchLanguages(context = requireContext())
                } else {
                    // setup data
                    setup(languages = it)
                }
            }
        }
    }

    private fun setup(languages: List<Language>) {
        if (languages.isEmpty()) {
            // show no content view
        } else {
            setupLanguagesAdapters(languages = languages)
        }
    }

    private fun setupLanguagesAdapters(languages: List<Language>) {
        if (!::adapter.isInitialized) {
            adapter = LanguagesAdapter(requireContext()) {
                viewModel.setLanguage(context = requireContext(), it)
                activity?.recreate()
            }
        }
        binding.apply {
            // save Recyclerview state before making change in data
            recyclerViewState = rvLanguages.layoutManager?.onSaveInstanceState()
            rvLanguages.adapter = adapter
            adapter.submitList(languages)
            // restore Recyclerview state after setting updated data
            recyclerViewState?.let {
                rvLanguages.layoutManager?.onRestoreInstanceState(it)
            }
        }
    }
}