package com.example.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Game state variables
    private var isPlayerXTurn = true  // Player X starts first
    private var gameActive = true     // Game is active
    private val board = arrayOfNulls<String>(9)  // 3x3 board represented as array

    // UI references
    private lateinit var buttons: Array<Button>
    private lateinit var statusTextView: TextView
    private lateinit var playAgainButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI references
        statusTextView = findViewById(R.id.statusTextView)
        playAgainButton = findViewById(R.id.playAgainButton)

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
        button.text = currentPlayer

        // Check if current player has won
        if (checkWin()) {
            gameActive = false
            // Show winner message (current player just won)
            statusTextView.text = if (isPlayerXTurn) {
                getString(R.string.player_x_wins)
            } else {
                getString(R.string.player_o_wins)
            }
            // Play Again button will be shown in Step 5
        } else {
            // Switch to next player
            isPlayerXTurn = !isPlayerXTurn
            updateStatusText()
        }
    }

    private fun checkWin(): Boolean {
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
                return true
            }
        }
        return false
    }

    private fun updateStatusText() {
        statusTextView.text = if (isPlayerXTurn) {
            getString(R.string.player_x_turn)
        } else {
            getString(R.string.player_o_turn)
        }
    }
}
