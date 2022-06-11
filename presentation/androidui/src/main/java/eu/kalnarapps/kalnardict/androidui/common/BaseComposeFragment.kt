package eu.kalnarapps.kalnardict.androidui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.kalnarapps.kalnardict.androidui.databinding.BaseComposeFragmentBinding

abstract class BaseComposeFragment<UiModel> : BaseFragment<UiModel>() {

    private var _binding: BaseComposeFragmentBinding? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BaseComposeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}