package com.example.smartwaiter.managers

import android.app.Activity
import com.example.database.HashCodeListener
import com.example.database.HashInterface
import hr.foi.air.manualentry.FragmentManualEntry
import androidx.fragment.app.Fragment
import android.util.Log
import com.example.qrmodul.QrFragment

class ModuleManager (Listener: HashCodeListener){
    private var  modules: MutableList<HashInterface> = mutableListOf<HashInterface>()
    private var  moduleNames: MutableList<String> = mutableListOf<String>()
    private var listener: HashCodeListener = Listener



    fun loadModules() {
        modules.add(FragmentManualEntry())
        modules.add(QrFragment())

        modules.forEach {
            moduleNames.add(it.getModuleName())
        }
    }
    fun getModule(index : Int):Fragment{
        return modules[index].getFragment(listener)
    }

    fun ModuleNames():MutableList<String>{
        return moduleNames
    }
}