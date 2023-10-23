package com.android.boilerplate.view.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.android.boilerplate.R
import com.android.boilerplate.aide.utils.BrowserUtils
import com.android.boilerplate.base.view.BaseFragment
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.FragmentMoreBinding
import com.android.boilerplate.model.data.aide.MoreItem
import com.android.boilerplate.view.main.MainActivity
import com.android.boilerplate.viewmodel.more.MoreViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class MoreFragment : BaseFragment() {

    private var recyclerViewState: Parcelable? = null
    private lateinit var adapter: MoreAdapter
    private lateinit var binding: FragmentMoreBinding

    private val viewModel: MoreViewModel by viewModels()

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
                R.layout.fragment_more,
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
            // observers on live data
            viewModel.moreItems.observe(viewLifecycleOwner) {
                if (it == null) {
                    // fetch data
                    viewModel.fetchMoreItems()
                } else {
                    // setup data
                    setup(moreItems = it)
                }
            }
        }
    }

    private fun setup(moreItems: List<MoreItem>) {
        if (moreItems.isEmpty()) {
            // show no content view
        } else {
            setupMoreItemsAdapters(moreItems = moreItems)
        }
    }

    private fun setupMoreItemsAdapters(moreItems: List<MoreItem>) {
        if (!::adapter.isInitialized) {
            adapter = MoreAdapter(
                context = requireContext(),
                listener = {
                    when (it.id) {
                        1 -> {
                            onSettingsClicked()
                        }
                        2 -> {
                            onFeedbackClicked()
                        }
                        4 -> {
                            onPolicyClicked()
                        }
                        5 -> {
                            onShareClicked()
                        }
                        6 -> {
                            onRateUsClicked()
                        }
                        7 -> {
                            onMoreAppsClicked()
                        }
                        8 -> {
                            onVersionClicked()
                        }
                    }
                }
            )
        }
        binding.apply {
            // save Recyclerview state before making change in data
            recyclerViewState = rvMore.layoutManager?.onSaveInstanceState()
            rvMore.adapter = adapter
            adapter.submitList(moreItems)
            // restore Recyclerview state after setting updated data
            recyclerViewState?.let {
                rvMore.layoutManager?.onRestoreInstanceState(it)
            }
        }
    }

    private fun onSettingsClicked() {
        activity?.let {
            if (it is MainActivity) {
                it.selectBottomNavigationTab(R.id.dest_settings)
            }
        }
    }

    private fun onFeedbackClicked() {
        activity?.let {
            if (it is MainActivity) {
                it.selectBottomNavigationTab(R.id.dest_feedback)
            }
        }
    }

    private fun onPolicyClicked() {
        context?.let {
            BrowserUtils.openInAppBrowser(it, getString(R.string.privacy_policy_link))
        }
    }

    private fun onShareClicked() {
        context?.let {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.share_message, getString(R.string.app_name), it.packageName)
                )
            }
            startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.share)))
        }
    }

    private fun onRateUsClicked() {
        context?.let {
            try {
                val uriString = "market://details?id=${it.packageName}"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))
            } catch (e: Exception) {
                val url = "http://play.google.com/store/apps/details?id=${it.packageName}"
                BrowserUtils.openInAppBrowser(it, url)
            }
        }
    }

    private fun onMoreAppsClicked() {
        context?.let {
            try {
                val uriString = "market://search?q=pub:XYZ"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))
            } catch (e: Exception) {
                val url = "http://play.google.com/store/search?q=pub:XYZ"
                BrowserUtils.openInAppBrowser(it, url)
            }
        }
    }

    private fun onVersionClicked() {

    }
}