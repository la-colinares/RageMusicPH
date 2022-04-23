package com.lacolinares.ragemusicph.custom

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.PowerManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import java.util.*


class EqualizerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val DEFAULT_DURATION = 3000
    private val DEFAULT_BAR_COUNT = 20
    val DEFAULT_ANIMATION_COUNT = 30
    private val DEFAULT_COLOR: Int = Color.DKGRAY
    private val DEFAULT_WIDTH = ViewGroup.LayoutParams.MATCH_PARENT
    private val DEFAULT_MARGIN_LEFT = 1
    private val DEFAULT_MARGIN_RIGHT = 1
    private val DEFAULT_RUN_IN_BATTERY_SAVE_MODE = false

    var mBars: ArrayList<View> = ArrayList()
    var mAnimators: ArrayList<Animator> = ArrayList()

    var mPlayingSet: AnimatorSet? = null
    var mStopSet: AnimatorSet? = null
    var isAnimating = false

    var mViewHeight = 0

    var mForegroundColor = DEFAULT_COLOR
    var mAnimationDuration = DEFAULT_DURATION
    var mBarCount = DEFAULT_BAR_COUNT
    var mBarWidth = DEFAULT_WIDTH
    var mMarginLeft = DEFAULT_MARGIN_LEFT
    var mMarginRight = DEFAULT_MARGIN_RIGHT
    var mRunInBatterySafeMode = DEFAULT_RUN_IN_BATTERY_SAVE_MODE

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_HORIZONTAL
    }

    fun setup(
        layoutHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
        runInBatterySaveMode: Boolean = DEFAULT_RUN_IN_BATTERY_SAVE_MODE,
        barColor: Int = DEFAULT_COLOR,
        animationDuration: Int = DEFAULT_DURATION,
        barCount: Int = DEFAULT_BAR_COUNT,
        barWidth: Int = DEFAULT_WIDTH,
        marginStart: Int = DEFAULT_MARGIN_LEFT,
        marginEnd: Int = DEFAULT_MARGIN_RIGHT,
    ) {
        this.minimumHeight = layoutHeight
        mRunInBatterySafeMode = runInBatterySaveMode
        mForegroundColor = barColor
        mAnimationDuration = animationDuration
        mBarCount = barCount
        mBarWidth = barWidth
        mMarginLeft = marginStart
        mMarginRight = marginEnd

        initViews()
    }

    private fun initViews() {
        for (i in 0 until mBarCount) {
            val v = View(context)

            val params = LayoutParams(mBarWidth, ViewGroup.LayoutParams.MATCH_PARENT)
            params.weight = if (mBarWidth <= -1) 1f else 0f
            params.setMargins(mMarginLeft, 0, mMarginRight, 0)
            v.layoutParams = params

            v.setBackgroundColor(mForegroundColor)
            addView(v)

            setPivots(v)
            mBars.add(v)
        }
    }

    private fun setPivots(v: View) {
        v.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            @SuppressLint("ObsoleteSdkInt")
            override fun onGlobalLayout() {
                if (v.height > 0) {
                    v.pivotY = v.height.toFloat()
                    if (Build.VERSION.SDK_INT >= 16) {
                        v.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            }
        })
    }

    @SuppressLint("ObsoleteSdkInt")
    fun animateBars() {
        isAnimating = true
        if (isInBatterySaveMode()) {
            if (mRunInBatterySafeMode) {
                Thread(mAnimationThread).start()
            }
        } else {
            if (mPlayingSet == null) {
                for (i in mBars.indices) {
                    val rand = Random()
                    val values = FloatArray(DEFAULT_ANIMATION_COUNT)
                    for (j in 0 until DEFAULT_ANIMATION_COUNT) {
                        values[j] = rand.nextFloat()
                    }
                    val scaleYbar = ObjectAnimator.ofFloat(mBars[i], "scaleY", *values)
                    scaleYbar.repeatCount = ValueAnimator.INFINITE
                    scaleYbar.repeatMode = ObjectAnimator.REVERSE
                    mAnimators.add(scaleYbar)
                }
                mPlayingSet = AnimatorSet()
                mPlayingSet!!.playTogether(mAnimators)
                mPlayingSet!!.duration = mAnimationDuration.toLong()
                mPlayingSet!!.interpolator = LinearInterpolator()
                mPlayingSet!!.start()
            } else if (Build.VERSION.SDK_INT < 19) {
                if (!mPlayingSet!!.isStarted) {
                    mPlayingSet!!.start()
                }
            } else {
                if (mPlayingSet!!.isPaused) {
                    mPlayingSet!!.resume()
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun stopBars() {
        isAnimating = false
        if (!isInBatterySaveMode()) {
            if (mPlayingSet != null && mPlayingSet!!.isRunning && mPlayingSet!!.isStarted) {
                if (Build.VERSION.SDK_INT < 19) {
                    mPlayingSet!!.end()
                } else {
                    mPlayingSet!!.pause()
                }
            }
            if (mStopSet == null) {
                mAnimators.clear()
                for (i in mBars.indices) {
                    mAnimators.add(ObjectAnimator.ofFloat(mBars[i], "scaleY", 0.1f))
                }
                mStopSet = AnimatorSet()
                mStopSet!!.playTogether(mAnimators)
                mStopSet!!.duration = 200
                mStopSet!!.start()
            } else if (!mStopSet!!.isStarted) {
                mStopSet!!.start()
            }
        } else {
            if (mRunInBatterySafeMode) {
                resetView()
                initViews()
            }
        }
    }

    private val mAnimationThread = Runnable {
        val rand = Random()
        while (isAnimating) {
            for (i in mBars.indices) {
                val barTmp = mBars[i]
                barTmp.post {
                    val value = rand.nextFloat() * mViewHeight
                    val params = barTmp.layoutParams as LayoutParams
                    params.height = value.toInt()
                    barTmp.layoutParams = params
                    barTmp.invalidate()
                }
            }
            try {
                Thread.sleep(80)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun isInBatterySaveMode(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && powerManager.isPowerSaveMode
    }

    private fun resetView() {
        removeAllViews()
        mBars.clear()
        mAnimators.clear()
        mPlayingSet = null
        mStopSet = null
    }

    /**
     * setBarColor
     *
     * @param mForegroundColor foreground color as hex string
     */
    fun setBarColor(mForegroundColor: String?) {
        this.mForegroundColor = Color.parseColor(mForegroundColor)
        resetView()
        initViews()
    }

    /**
     * setBarColor
     *
     * @param mForegroundColor foreground color as integer
     */
    fun setBarColor(mForegroundColor: Int) {
        this.mForegroundColor = mForegroundColor
        resetView()
        initViews()
    }

    /**
     * setBarCount
     *
     * @param mBarCount bar count
     */
    fun setBarCount(mBarCount: Int) {
        this.mBarCount = mBarCount
        resetView()
        initViews()
    }

    /**
     * setAnimationDuration
     *
     * @param mAnimationDuration duration in milliseconds
     */
    fun setAnimationDuration(mAnimationDuration: Int) {
        this.mAnimationDuration = mAnimationDuration
        resetView()
        initViews()
    }

    /**
     * setBarWidth
     *
     * @param mBarWidth bar width in pixel
     */
    fun setBarWidth(mBarWidth: Int) {
        this.mBarWidth = mBarWidth
        resetView()
        initViews()
    }

    /**
     * setMarginRight
     *
     * @param mMarginRight margin right in pixel
     */
    fun setMarginRight(mMarginRight: Int) {
        this.mMarginRight = mMarginRight
        resetView()
        initViews()
    }

    /**
     * setMarginLeft
     *
     * @param mMarginLeft margin left in pixel
     */
    fun setMarginLeft(mMarginLeft: Int) {
        this.mMarginLeft = mMarginLeft
        resetView()
        initViews()
    }


    fun setRunInBatterySafeMode(mRunInBatterySafeMode: Boolean) {
        this.mRunInBatterySafeMode = mRunInBatterySafeMode
    }

    fun isAnimating(): Boolean? {
        return isAnimating
    }
}