package com.example.smartwaiter.managers

import android.app.Activity
import com.example.database.HashCodeListener
import com.example.database.HashInterface
import hr.foi.air.manualentry.FragmentManualEntry

class ModuleManager {
    private lateinit var  modules: MutableList<HashInterface>
    private lateinit var listener: HashCodeListener

    fun ModuleManager(Listener: HashCodeListener){
        listener = Listener
    }

    private fun loadModules() {
        modules.add(FragmentManualEntry())
        modules.forEach {
            it.getFragment(listener)
        }
    }
}