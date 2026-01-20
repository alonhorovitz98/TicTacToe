package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    // Game state variables
    private var isPlayerXTurn = true  // Player X starts first
    private var gameActive = true     // Game is active
    private val board = arrayOfNulls<String>(9)  // 3x3 board represented as array

    // UI references
    private lateinit var buttons: Array<Button>
    private lateinit var statusTextView: TextView
    private lateinit var playAgainButton: Button
    private lateinit var winningLineView: WinningLineView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI references
        statusTextView = findViewById(R.id.statusTextView)
        playAgainButton = findViewById(R.id.playAgainButton)
        winningLineView = findViewById(R.id.winningLineView)

        // Initialize buttons array
        buttons = arrayOf(
            findViewById(R.id.button0), findViewById(R.id.button1), findViewById(R.id.button2),
            findViewById(R.id.button3), findViewById(R.id.button4), findViewById(R.id.button5),
            findViewById(R.id.button6), findViewById(R.id.button7), findViewById(R.id.button8)
        )

        // Set up click listeners for all buttons
        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                onButtonClick(it as Button, i)
            }
        }

        // Set up Play Again button listener
        playAgainButton.setOnClickListener {
            resetGame()
        }

        // Wait for layout to be drawn before calculating button positions
        winningLineView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                winningLineView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // Layout is ready, button positions can now be calculated
            }
        })

        // Initialize status text
        updateStatusText()
    }

    private fun onButtonClick(button: Button, index: Int) {
        // Don't allow moves if game is not active or cell is already occupied
        if (!gameActive || board[index] != null) {
            return
        }

        // Set the cell with current player's mark
        val currentPlayer = if (isPlayerXTurn) "X" else "O"
        board[index] = currentPlayer
        
        // Clear text and set drawable instead for crisp rendering
        button.text = ""
        val drawable = if (isPlayerXTurn) {
            ContextCompat.getDrawable(this, R.drawable.ic_x_mark)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_o_mark)
        }
        
        // Set drawable size - use 70% of button size for proper scaling
        button.post {
            val size = (button.width * 0.7).toInt().coerceAtLeast(60)
            drawable?.setBounds(0, 0, size, size)
            button.setCompoundDrawables(drawable, null, null, null)
            button.compoundDrawablePadding = 0
        }

        // Check if current player has won
        val winningPositions = checkWin()
        if (winningPositions != null) {
            gameActive = false
            // Show winner message (current player just won)
            statusTextView.text = if (isPlayerXTurn) {
                getString(R.string.player_x_wins)
            } else {
                getString(R.string.player_o_wins)
            }
            // Draw winning line
            drawWinningLine(winningPositions)
            // Show Play Again button clearly on top of other views
            playAgainButton.visibility = View.VISIBLE
            playAgainButton.bringToFront()
            playAgainButton.requestLayout()
        } else if (isBoardFull()) {
            // Draw detection - board is full and no one won
            gameActive = false
            statusTextView.text = getString(R.string.draw)
            playAgainButton.visibility = View.VISIBLE
            playAgainButton.bringToFront()
            playAgainButton.requestLayout()
        } else {
            // Switch to next player
            isPlayerXTurn = !isPlayerXTurn
            updateStatusText()
        }
    }

    private fun checkWin(): IntArray? {
        // All possible winning combinations (rows, columns, diagonals)
        val winPositions = arrayOf(
            intArrayOf(0, 1, 2),  // Top row
            intArrayOf(3, 4, 5),  // Middle row
            intArrayOf(6, 7, 8),  // Bottom row
            intArrayOf(0, 3, 6),  // Left column
            intArrayOf(1, 4, 7),  // Middle column
            intArrayOf(2, 5, 8),  // Right column
            intArrayOf(0, 4, 8),  // Diagonal (top-left to bottom-right)
            intArrayOf(2, 4, 6)   // Diagonal (top-right to bottom-left)
        )

        // Check each winning combination
        for (positions in winPositions) {
            val first = board[positions[0]]
            val second = board[positions[1]]
            val third = board[positions[2]]

            // If all three positions have the same non-null value, it's a win
            if (first != null && first == second && first == third) {
                return positions
            }
        }
        return null
    }

    private fun isBoardFull(): Boolean {
        for (i in 0 until 9) {
            if (board[i] == null) {
                return false
            }
        }
        return true
    }

    private fun resetGame() {
        isPlayerXTurn = true
        gameActive = true
        
        // Clear board
        for (i in 0 until 9) {
            board[i] = null
        }
        
        // Clear all button drawables and text
        for (button in buttons) {
            button.text = ""
            button.setCompoundDrawables(null, null, null, null)
        }
        
        // Clear winning line
        winningLineView.clearWinningLine()
        
        // Reset status text
        updateStatusText()
        
        // Hide Play Again button
        playAgainButton.visibility = View.GONE
    }

    private fun drawWinningLine(winningPositions: IntArray) {
        // Post to ensure layout is complete before calculating positions
        winningLineView.post {
            // Calculate center positions of all buttons relative to winningLineView
            val buttonCenters = Array<Pair<Float, Float>?>(9) { null }
            
            for (i in buttons.indices) {
                val button = buttons[i]
                val buttonLocation = IntArray(2)
                button.getLocationOnScreen(buttonLocation)
                
                // Get the position of winningLineView
                val viewLocation = IntArray(2)
                winningLineView.getLocationOnScreen(viewLocation)
                
                // Calculate center relative to winningLineView
                val centerX = (buttonLocation[0] - viewLocation[0] + button.width / 2f)
                val centerY = (buttonLocation[1] - viewLocation[1] + button.height / 2f)
                
                buttonCenters[i] = Pair(centerX, centerY)
            }
            
            // Set the winning line
            winningLineView.setWinningLine(winningPositions, buttonCenters)
        }
    }

    private fun updateStatusText() {
        statusTextView.text = if (isPlayerXTurn) {
            getString(R.string.player_x_turn)
        } else {
            getString(R.string.player_o_turn)
        }
    }
}
