package com.android.boilerplate.view.feedback

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.android.boilerplate.R
import com.android.boilerplate.base.view.BaseFragment
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.FragmentFeedbackBinding
import com.android.boilerplate.viewmodel.feedback.FeedbackViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class FeedbackFragment : BaseFragment() {

    private lateinit var binding: FragmentFeedbackBinding

    private val viewModel: FeedbackViewModel by viewModels()

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
                R.layout.fragment_feedback,
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
            layoutImproveDesign.root.setOnClickListener {
                etFeedback.error = null
                it.isSelected = !it.isSelected
                viewModel.improveDesign.value = it.isSelected
            }
            layoutImproveExperience.root.setOnClickListener {
                etFeedback.error = null
                it.isSelected = !it.isSelected
                viewModel.improveExperience.value = it.isSelected
            }
            layoutImproveFunctionality.root.setOnClickListener {
                etFeedback.error = null
                it.isSelected = !it.isSelected
                viewModel.improveFunctionality.value = it.isSelected
            }
            layoutImprovePerformance.root.setOnClickListener {
                etFeedback.error = null
                it.isSelected = !it.isSelected
                viewModel.improvePerformance.value = it.isSelected
            }
            etFeedback.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, co: Int, a: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let {
                        if (it.isNotEmpty()) {
                            etFeedback.error = null
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            btnSubmitFeedback.setOnClickListener { onSubmitRatingClicked() }
        }
    }

    private fun onSubmitRatingClicked() {
        if (validate()) {
            var description = ""
            viewModel.apply {
                improveDesign.value?.let {
                    if (it) {
                        description += "${getString(R.string.improve_design)} \n\n"
                    }
                }
                improveExperience.value?.let {
                    if (it) {
                        description += "${getString(R.string.improve_experience)} \n\n"
                    }
                }
                improveFunctionality.value?.let {
                    if (it) {
                        description += "${getString(R.string.improve_functionality)} \n\n"
                    }
                }
                improvePerformance.value?.let {
                    if (it) {
                        description += "${getString(R.string.improve_performance)} \n\n"
                    }
                }
            }
            if (!TextUtils.isEmpty(binding.etFeedback.text?.trim())) {
                description += "\n\n ${getString(R.string.detailed_feedback)}"
                description += "\n\n ${binding.etFeedback.text.toString()}"
            }
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    "${getString(R.string.feedback)} - ${getString(R.string.app_name)}"
                )
                putExtra(Intent.EXTRA_TEXT, description)
            }
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.submit_feedback)))
            } catch (e: ActivityNotFoundException) {
                showToast(getString(R.string.no_email_app_installed))
            }
        }
    }

    private fun validate(): Boolean {
        binding.apply {
            if (TextUtils.isEmpty(etFeedback.text) &&
                !layoutImproveDesign.root.isSelected &&
                !layoutImproveExperience.root.isSelected &&
                !layoutImproveFunctionality.root.isSelected &&
                !layoutImprovePerformance.root.isSelected
            ) {
                etFeedback.error = getString(R.string.invalid_feedback)
                return false
            }
            return true
        }
    }
}