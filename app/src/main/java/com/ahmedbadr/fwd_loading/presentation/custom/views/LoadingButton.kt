package com.ahmedbadr.fwd_loading.presentation.custom.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ahmedbadr.fwd_loading.R
import com.ahmedbadr.fwd_loading.data.models.ButtonState
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var arcWidth = 0.0f
    private var arcSAngle = 0.0f
    private var animValue = 0.0f

    private var buttonText: String

    private val idealButtonPainting : Paint

    private val loadingButtonPainting : Paint

    private val centerTextPainting : Paint

    private var circlePainting : Paint

    private var valueAnimator = ValueAnimator()

    init {
        buttonText = context.getString(R.string.button_download)

        isClickable = true

        idealButtonPainting = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColor(R.color.colorPrimary)
        }

        loadingButtonPainting = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColor(R.color.colorPrimaryDark)
        }

        centerTextPainting = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = resources.getDimension(R.dimen.default_text_size)
            textAlign = Paint.Align.CENTER
            color = Color.WHITE
        }

        circlePainting = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = context.getColor(R.color.colorAccent)
        }
    }

    var loadingButtonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Ideal) { _, _, new ->

        when (new) {
            ButtonState.Ideal ->{
                buttonText = context.getString(R.string.button_download)
                circlePainting.color = context.getColor(R.color.colorAccent)
            }

            ButtonState.Completed -> {
                buttonText = context.getString(R.string.button_download)
                circlePainting.color = context.getColor(R.color.colorPrimaryDark)

                valueAnimator.cancel()
                animValue = 0f
                invalidate()
            }

            ButtonState.Loading -> {
                buttonText = context.getString(R.string.button_loading)
                circlePainting.color = context.getColor(R.color.colorAccent)
                valueAnimator = ValueAnimator.ofFloat(0.0f, measuredWidth.toFloat()).setDuration(2500).apply {
                        addUpdateListener { valueAnimator ->
                            animValue = valueAnimator.animatedValue as Float
                            arcSAngle = animValue / 8
                            arcWidth = animValue * 4
                            invalidate()
                        }
                    }
                valueAnimator.start()
            }
        }

    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), idealButtonPainting)
            drawRect(0f, 0f, arcWidth, heightSize.toFloat(), loadingButtonPainting)

            drawText(buttonText, widthSize.toFloat() / 2, heightSize.toFloat() / 2 , centerTextPainting)

            drawArc(
                widthSize - 145f,
                heightSize / 2 - 35f,
                widthSize - 75f,
                heightSize / 2 + 35f,
                0F,
                arcWidth,
                true,
                circlePainting
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


}