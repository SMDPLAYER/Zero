package ru.smd.zero

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var timer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tv_digit.setValue(timer)
        clickListener()
    }

    private fun clickListener() {
        btn_scroll_up.setOnClickListener {
            if (timer != 0) {
                timer--
                tv_digit.setValue(timer)
            }
        }
        btn_scroll_down.setOnClickListener {
            timer++
            tv_digit.setValue(timer)
        }
    }


}

private const val ANIMATION_DURATION = 200

class JustText : FrameLayout {
    var currentTextView: TextView? = null
    var nextTextView: TextView? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.just_text, this)
        currentTextView = rootView.findViewById<View>(R.id.currentTextView) as TextView
        nextTextView = rootView.findViewById<View>(R.id.nextTextView) as TextView
        nextTextView!!.translationY = height.toFloat()
        setValue(0)
    }

    fun setValue(desiredValue: Int) {
        if (currentTextView!!.text == null || currentTextView!!.text.isEmpty()) {
            currentTextView!!.text = String.format(Locale.getDefault(), "%d", desiredValue)
        }
        val oldValue = currentTextView!!.text.toString().toInt()
        if (oldValue > desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue - 1)
            currentTextView!!.animate().translationY(-height.toFloat()).setDuration(
                ANIMATION_DURATION.toLong()
            ).start()
            nextTextView!!.translationY = nextTextView!!.height.toFloat()
            nextTextView!!.animate().translationY(0f).setDuration(ANIMATION_DURATION.toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        currentTextView!!.text = String.format(
                            Locale.getDefault(),
                            "%d",
                            oldValue - 1
                        )
                        currentTextView!!.translationY = 0f
                        if (oldValue - 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                }).start()
        } else if (oldValue < desiredValue) {
            nextTextView?.text = String.format(Locale.getDefault(), "%d", oldValue + 1)
            currentTextView!!.animate().translationY(height.toFloat()).setDuration(
                ANIMATION_DURATION.toLong()
            ).start()
            nextTextView!!.translationY = -nextTextView!!.height.toFloat()
            nextTextView!!.animate().translationY(0f).setDuration(ANIMATION_DURATION.toLong())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        currentTextView!!.text = String.format(
                            Locale.getDefault(),
                            "%d",
                            oldValue + 1
                        )
                        currentTextView!!.translationY = 0f
                        if (oldValue + 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                }).start()
        }
    }

}