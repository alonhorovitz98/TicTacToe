package com.example.tictactoe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class WinningLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.winning_line_green)
        strokeWidth = 18f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
    }
    
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.winning_line_green)
        strokeWidth = 24f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        alpha = 100
    }
    
    private val path = Path()

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
                // Use Path for smoother rendering
                path.reset()
                path.moveTo(startCenter.first, startCenter.second)
                path.lineTo(endCenter.first, endCenter.second)
                
                // Draw glow effect first (wider, semi-transparent)
                canvas.drawPath(path, glowPaint)
                
                // Draw main line on top (crisp and bright)
                canvas.drawPath(path, paint)
            }
        }
    }
}

