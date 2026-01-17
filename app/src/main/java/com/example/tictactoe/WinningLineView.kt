package com.example.tictactoe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class WinningLineView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.colorWinningLine)
        strokeWidth = 15f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var winningPosition: IntArray? = null

    fun drawWinningLine(winningPosition: IntArray) {
        this.winningPosition = winningPosition
        invalidate()
    }

    fun clearWinningLine() {
        winningPosition = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        winningPosition?.let { pos ->
             // pos contains [row, col, winType]
             // winType: 0=row, 1=col, 2=diagonal
             // This is a simplified placeholder. Actual drawing logic depends on coordinates passed from MainActivity
             // For this assignment, assuming standard grid drawing.
             
             // However, strictly following the request which implies `drawWinningLine` is already implemented or we should implement it capable of drawing specific lines.
             // Given I am writing this file from scratch, I'll implement standard logic based on cell size.
             
             val width = width.toFloat()
             val height = height.toFloat()
             val cellWidth = width / 3
             val cellHeight = height / 3
             
             val type = pos[2] // 0 for row, 1 for col, 2 for diag
             val index = pos[0] // row/col index or diag index (0: top-left to bottom-right, 1: other)
             
             // This part needs to align with checkWin return values.
             // Let's assume:
             // Rows: 0, 1, 2 -> [index, 0, 0]
             // Cols: 0, 1, 2 -> [0, index, 1]
             // Diagonals: [0, 0, 2] (main), [0, 0, 3] (anti)
             
             // Let wait for MainActivity logic to define the contract. 
             // For now, I'll make a more generic drawing method exposed or just hardcode some paths.
             
             // Actually, simplest is to just expose start/end coordinates or row/col indices.
             // Let's implement based on standard 3x3 grid.
             
             if (type == 0) { // Row
                 val y = (pos[0] * cellHeight) + (cellHeight / 2)
                 canvas.drawLine(0f, y, width, y, paint)
             } else if (type == 1) { // Col
                 val x = (pos[0] * cellWidth) + (cellWidth / 2)
                 canvas.drawLine(x, 0f, x, height, paint)
             } else if (type == 2) { // Diagonal
                 if (pos[0] == 0) { // Main diagonal
                     canvas.drawLine(0f, 0f, width, height, paint)
                 } else { // Anti diagonal
                     canvas.drawLine(width, 0f, 0f, height, paint)
                 }
             }
        }
    }
}
