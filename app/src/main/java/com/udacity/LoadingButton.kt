package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.os.CountDownTimer
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.udacity.MainActivity.Companion.downloadID
import kotlin.properties.Delegates

public enum class ButtonStating(val label: Int,val backGroundColor: Int) {
    CLICKED(R.string.button_name,R.color.colorBackgroundClick),
    LOADING(R.string.button_loading,R.color.colorBackgroundLoading),
    COMPLETED(R.string.downloaded,R.color.colorBackgroundCompleted);
}

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private var loadingForeColor = 0
    private var completedForeColor = 0
    private var clieckedForeColor = 0
    private var myCustomTextColor = 0
    private var myCustomProgressButton: Double = 0.0
    private lateinit var customButtonAnimator: ValueAnimator

    private val customListener = ValueAnimator.AnimatorUpdateListener {
        myCustomProgressButton = (it.animatedValue as Float).toDouble()
        invalidate()
        requestLayout()
    }

    // call after downloading is completed
    fun isCompleted() {
        // cancel the animation when file is downloaded
        customButtonAnimator.cancel()
        buttonStating = ButtonStating.COMPLETED
        val timer = object: CountDownTimer(500, 100) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                buttonStating = ButtonStating.CLICKED
            }
        }
        timer.start()
        invalidate()
        requestLayout()
    }

    companion object{
        public var buttonStating = ButtonStating.CLICKED
    }
    private var buttonText = ButtonStating.CLICKED.label
    var touchType = -1

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    var onClickListener: () -> Unit = {
        if(MainActivity.isSelected)
        {
//            animateLogo()
            startingAnim()
            if (buttonStating == ButtonStating.CLICKED) {
                buttonStating = ButtonStating.LOADING
                buttonText = ButtonStating.CLICKED.label
                changeingBackgroundColor(buttonStating.backGroundColor)
            } else if (buttonStating == ButtonStating.LOADING) {

                buttonStating = ButtonStating.COMPLETED
                buttonText = ButtonStating.LOADING.label
                changeingBackgroundColor(buttonStating.backGroundColor)
            } else if (buttonStating == ButtonStating.COMPLETED) {
                buttonStating = ButtonStating.CLICKED
                buttonText = ButtonStating.COMPLETED.label
                changeingBackgroundColor(buttonStating.backGroundColor)
            }
            invalidate()
        }
        else
        {

        }

    }

    private fun animateLogo() {
        val translationYFrom = 400f
        val translationYTo = 0f
        val valueAnimator = ValueAnimator.ofFloat(translationYFrom, translationYTo).apply {
            interpolator = LinearInterpolator()
            duration = 1000
        }
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            this?.translationY = value
        }
        valueAnimator.start()
    }
    private fun startingAnim()
    {
        customButtonAnimator.start()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        val value = super.onTouchEvent(e)

        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                /* Determine where the user has touched on the screen. */
                touchType = 1 // for eg.
                return true
            }
            MotionEvent.ACTION_UP -> {
                /* Now that user has lifted his finger. . . */
                when (touchType) {
                    1 -> onClickListener()
                }
            }
        }
        return value
    }

    init {
        isClickable = true

        customButtonAnimator = AnimatorInflater.loadAnimator(
            context,
            R.animator.loading_animation
        ) as ValueAnimator

        customButtonAnimator.addUpdateListener(customListener)

        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        )

        // button text color
        myCustomTextColor = attr.getColor(
            R.styleable.LoadingButton_text_color,
            ContextCompat.getColor(context, R.color.white)
        )

        //To Change the color of the fan controller
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            clieckedForeColor = getColor(R.styleable.LoadingButton_clicked_btn, 0)
            loadingForeColor = getColor(R.styleable.LoadingButton_loading_btn, 0)
            completedForeColor = getColor(R.styleable.LoadingButton_completed_btn, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = when (buttonStating) {
            ButtonStating.CLICKED -> clieckedForeColor
            ButtonStating.LOADING -> loadingForeColor
            ButtonStating.COMPLETED -> completedForeColor
            else -> {}
        } as Int

//        paint.color = Color.RED
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        if(buttonStating == ButtonStating.LOADING)
        {
            paint.color = clieckedForeColor
            canvas?.drawRect(
                0f, 0f,
                (width * (myCustomProgressButton / 100)).toFloat(), height.toFloat(), paint
            )
        }

        paint.color = myCustomTextColor

        canvas?.drawText(
            resources.getString(buttonStating.label),
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            paint
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        changeingBackgroundColor(R.color.colorBackgroundClick)
    }

    fun changeingBackgroundColor(backGround: Int) {
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(ResourcesCompat.getColor(resources, backGround, null))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}