package com.ev.composition_number.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ev.composition_number.R
import com.ev.composition_number.databinding.FragmentGameFinishedBinding
import com.ev.composition_number.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinished == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseGameResult()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPressed()
        buttonRetry()
        bindViews()
    }

    private fun buttonRetry() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun backPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        })
    }

    private fun bindViews() {
        with(binding) {
            ivResult.setImageResource(getImageId())
            tvRequiredAnswers.text = String.format(
                getString(R.string.required_score),
                gameResult.gameSettings.minCountOfRightAnswers
            )
            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                gameResult.countOfRightAnswers
            )
            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameResult.gameSettings.minPercentOfRightAnswers
            )
            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                getPercentOfRightAnswered()
            )
        }
    }

    private fun getPercentOfRightAnswered() = with(gameResult) {
        if (countOfQuestions == 0) 0
        else ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun getImageId(): Int {
        return if (gameResult.winner) {
            R.drawable.good
        } else {
            R.drawable.wasted
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseGameResult() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager
            .popBackStack(GameFragment.NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    companion object {
        private const val KEY_GAME_RESULT = "gema_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}
