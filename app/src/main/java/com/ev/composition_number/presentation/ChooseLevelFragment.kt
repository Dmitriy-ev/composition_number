package com.ev.composition_number.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ev.composition_number.databinding.FragmentChooseLevelBinding

class ChooseLevelFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        FragmentChooseLevelBinding.inflate(inflater, container, false).root
}