package com.example.calendary
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class ApplicationRepository {
    companion object {
        public lateinit var currentFragment : Fragment
        public lateinit var fragmentManager : FragmentManager
        fun hasFragment() : Boolean {
            return this::currentFragment.isInitialized
        }
    }
}
