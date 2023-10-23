package com.android.boilerplate.view.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.boilerplate.R
import com.android.boilerplate.aide.utils.DialogUtils
import com.android.boilerplate.aide.utils.Utils
import com.android.boilerplate.base.model.data.remote.response.Result
import com.android.boilerplate.base.view.BaseFragment
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.FragmentUsersBinding
import com.android.boilerplate.model.data.local.database.entities.RandomUser
import com.android.boilerplate.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class UsersFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var adapter: RandomUsersAdapter
    private lateinit var binding: FragmentUsersBinding

    private val viewModel: MainViewModel by viewModels()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Utils.isTiramisuPlus()) {
            requestNotificationsPermission()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_users,
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
            swipeRefreshLayout.setOnRefreshListener(this@UsersFragment)
            viewModel.usersResultLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    // use it for custom view group based loader handling
                    is Result.Loading -> {}
                    is Result.Success -> {
                        it.data?.let { randomUsers ->
                            swipeRefreshLayout.isRefreshing = false
                            setupRandomUsersAdapters(randomUsers = randomUsers)
                        }
                    }
                    // use it for custom view group based error handling
                    is Result.Failure -> {}
                }
            }
            viewModel.getUsers()
        }
    }

    override fun loaderVisibility(visibility: Boolean) {
        binding.swipeRefreshLayout.apply {
            if (isRefreshing) {
                isRefreshing = visibility
            } else {
                super.loaderVisibility(visibility)
            }
        }
    }

    override fun onRefresh() {
        viewModel.getLatestUsers()
    }

    private fun setupRandomUsersAdapters(randomUsers: List<RandomUser>) {
        if (!::adapter.isInitialized) {
            adapter = RandomUsersAdapter(requireContext()) {
                findNavController().navigate(
                    UsersFragmentDirections.actionDestMainToDestUserDetails(it)
                )
            }
        }
        binding.apply {
            rvUsers.adapter = adapter
            adapter.submitList(randomUsers)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationsPermission() {
        if (!checkNotificationsPermission()) {
            notificationsPermissionResult.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val notificationsPermissionResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.values.contains(false)) {
            askToLaunchAppSettings()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askToLaunchAppSettings() {
        DialogUtils.createSimpleAlertDialog(
            context = requireContext(),
            titleId = R.string.n_p_title,
            messageId = R.string.n_p_message,
            positiveButtonText = R.string.open_app_settings,
            positiveClickListener = { _, _ -> launchAppSettings() },
            negativeButtonText = R.string.cancel
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun launchAppSettings() {
        try {
            appSettingsResult.launch(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                it.data = Uri.fromParts("package", context?.packageName, null)
            })
        } catch (exception: Exception) {
            Log.e(this::class.java.simpleName, exception.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val appSettingsResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it?.let {
            if (!checkNotificationsPermission()) {
                showToast(message = getString(R.string.n_p_not_granted))
            }
        }
    }
}