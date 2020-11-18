package com.example.smartwaiter.ui.auth.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.database.UserPreferences
import com.example.smartwaiter.R
import com.example.smartwaiter.repository.AuthRepository
import com.example.smartwaiter.ui.restaurant.RestaurantActivity
import com.example.smartwaiter.util.enable
import com.example.smartwaiter.util.handleApiError
import com.example.smartwaiter.util.startNewActivity
import com.example.smartwaiter.util.visible
import com.google.common.hash.Hashing
import hr.foi.air.webservice.util.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBarLogin.visible(false)
        btnLogin.enable(false)

        userPreferences = UserPreferences(requireContext())

        val repository = AuthRepository(userPreferences)
        val viewModelFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.myResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    progressBarLogin.visible(false)
                    lifecycleScope.launch {
                        viewModel.saveAuthToken(response.value[0].korisnicko_ime)
                        viewModel.saveUserType(response.value[0].tip_korisnika_id)
                        requireActivity().startNewActivity(RestaurantActivity::class.java)
                    }
                }

                is Resource.Loading -> progressBarLogin.visible(true)
                is Resource.Failure -> {
                    progressBarLogin.visible(false)
                    handleApiError(response) { login() }
                    Log.d("Response", response.toString())
                }
            }
        })

        editTextPassword.addTextChangedListener {
            val username = editTextUsername.text.toString().trim()
            btnLogin.enable(username.isNotEmpty() && it.toString().isNotEmpty())
        }

        btnLogin.setOnClickListener {
            login()
        }

        btnRegister.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun login(){
        val username = editTextUsername.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val encryptedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString()
        viewModel.getKorisnik(
            table = "Korisnik",
            method = "select",
            username,
            encryptedPassword
        )
    }
}


