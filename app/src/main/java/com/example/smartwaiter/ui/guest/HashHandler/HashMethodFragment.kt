package com.example.smartwaiter.ui.guest.HashHandler

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.database.HashCodeListener
import com.example.smartwaiter.R
import com.example.smartwaiter.managers.ModuleManager
import kotlinx.android.synthetic.main.fragment_hash_method.*


class HashMethodFragment : Fragment(R.layout.fragment_hash_method) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var moduleManager: ModuleManager = ModuleManager(activity as HashCodeListener)

        moduleManager.loadModules()
        val LayoutButtons = ButtonsLayout


        var Modules = moduleManager.ModuleNames()
        for (a in 0..(Modules.size-1)){


            val button = Button(requireContext())
            button.layoutParams = LayoutButtons.layoutParams


            button.text = Modules[a]
            button.setOnClickListener(View.OnClickListener {
                val newFragment: Fragment = moduleManager.getModule(a)
                val transaction = parentFragmentManager.beginTransaction()
                transaction.addToBackStack(null)
                transaction.replace(R.id.nav_host_fragment_guest,  newFragment)
                transaction.commit()
            })

            button.setLayoutParams(
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
            button.width= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics)
                .toInt()
            button.height=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics)
                .toInt()

            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            val param = button.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,20,0,0)
            button.layoutParams = param
            button.setBackgroundResource(R.drawable.button_modules)
            LayoutButtons.addView(button);

        }



        btnCallMap.setOnClickListener {
            val action =
                HashMethodFragmentDirections.actionHashHandlerFragmentToMapFragment()
            findNavController().navigate(action)

        }

    }


}