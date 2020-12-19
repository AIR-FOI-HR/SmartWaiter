package com.example.smartwaiter.ui.restaurant.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartwaiter.R
import com.example.smartwaiter.repository.Add_mealRepository
import com.example.smartwaiter.ui.auth.MainActivity
import com.example.smartwaiter.ui.guest.menu_guest.MealGuestListAdapter
import com.example.smartwaiter.util.handleApiError
import com.example.smartwaiter.util.visible
import hr.foi.air.webservice.util.Resource
import kotlinx.android.synthetic.main.fragment_meni.*
import kotlinx.android.synthetic.main.fragment_meni_guest.*

class MenuFragment : Fragment(R.layout.fragment_meni) {
    private lateinit var lokal: String

    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_meni, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        load()
        btnCallAddMeal.setOnClickListener{
            findNavController().navigate(MenuFragmentDirections.actionMeniFragmentToAddMealFragment(lokal.toInt()))
        }
        viewModel.myResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    progressBarMenu.visible(false)
                    if (response != null) {
                        val odgovor = response.value
                        recycleViewMenu.layoutManager = LinearLayoutManager(activity)
                        recycleViewMenu.adapter = MealListAdapter(odgovor, this)
                    }
                }
                is Resource.Loading -> {
                    progressBarMenu.visible(true)
                }
                is Resource.Failure -> {
                    progressBarMenu.visible(true)
                    handleApiError(response) { load() }
                    Log.d("Response", response.toString())
                }
            }
        })

    }

    fun callEditMeal(mealId: String){
        val meal = mealId
        val action = MenuFragmentDirections.actionMeniFragmentToEditMealFragment2(meal)
        findNavController().navigate(action)
    }
    fun load(){
        val repository = Add_mealRepository()
        val viewModelFactory = MenuModelFactory(repository)
        lokal = requireArguments().getInt("restaurant_id").toString()
        viewModel = ViewModelProvider(this, viewModelFactory).get(MenuViewModel::class.java)
        viewModel.getMeal(table = "Stavka_jelovnika", method = "select", lokal)
    }
}