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

        // Switch to next player
        isPlayerXTurn = !isPlayerXTurn
        updateStatusText()
    }

    private fun updateStatusText() {
        statusTextView.text = if (isPlayerXTurn) {
            getString(R.string.player_x_turn)
        } else {
            getString(R.string.player_o_turn)
        }
    }
}
