package com.example.database
import androidx.fragment.app.Fragment

interface HashInterface {
    fun getFragment(listener : HashCodeListener): Fragment
    fun getModuleName(): String
}