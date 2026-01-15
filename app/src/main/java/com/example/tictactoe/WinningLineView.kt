package com.example.tictactoe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class WinningLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.winning_line_green)
        strokeWidth = 16f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        pathEffect = null
    }
    
    private val shadowPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.button_shadow)
        strokeWidth = 20f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private var winningPositions: IntArray? = null
    private var buttonPositions: Array<Pair<Float, Float>?>? = null

    fun setWinningLine(positions: IntArray, buttonCenters: Array<Pair<Float, Float>?>) {
        winningPositions = positions
        buttonPositions = buttonCenters
        invalidate()
    }

    fun clearWinningLine() {
        winningPositions = null
        buttonPositions = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val positions = winningPositions
        val centers = buttonPositions

        if (positions != null && centers != null && positions.size >= 2) {
            // Draw line from first to last winning position
            val startPos = positions[0]
            val endPos = positions[positions.size - 1]

            val startCenter = centers[startPos]
            val endCenter = centers[endPos]

            if (startCenter != null && endCenter != null) {
                // Draw shadow first for depth
                canvas.drawLine(
                    startCenter.first + 2f,
                    startCenter.second + 2f,
                    endCenter.first + 2f,
                    endCenter.second + 2f,
                    shadowPaint
                )
                // Draw main line on top
                canvas.drawLine(
                    startCenter.first,
                    startCenter.second,
                    endCenter.first,
                    endCenter.second,
                    paint
                )
            }
        }
    }
}

