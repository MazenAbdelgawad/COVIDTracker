package iti.intake40.covidtracker

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import iti.intake40.covidtracker.view.activites.HomeActivity
import kotlinx.android.synthetic.main.activity_splach.*

class SplachActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach)

        val valueAnimator = ValueAnimator.ofFloat(0f, 360f)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            imageView.rotation = value
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1500
        valueAnimator.start()


        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                startActivity(
                    Intent(
                        applicationContext,
                        HomeActivity::class.java
                    )
                )
                finish()
            }

        })

    }
}
