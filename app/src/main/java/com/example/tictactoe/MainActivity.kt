package com.example.tictactoe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var playAgainButton: Button
    private lateinit var winningLineView: WinningLineView
    private val buttons = arrayOfNulls<Button>(9)

    private var isPlayerXTurn = true
    private var gameActive = true
    // Board representation: 0-8 indices. null = empty, "X" or "O" = occupied
    private val board = arrayOfNulls<String>(9)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.statusTextView)
        playAgainButton = findViewById(R.id.playAgainButton)
        winningLineView = findViewById(R.id.winningLineView)

        // Initialize buttons
        for (i in 0 until 9) {
            val buttonID = "button$i"
            val resID = resources.getIdentifier(buttonID, "id", packageName)
            buttons[i] = findViewById(resID)
            buttons[i]?.setOnClickListener(::onButtonClick)
        }

        playAgainButton.setOnClickListener {
            resetGame()
        }
        
        updateStatusText()
    }

    fun onButtonClick(view: View) {
        if (!gameActive) return

        val button = view as Button
        val position = button.tag.toString().toInt()

        if (board[position] != null) return

        // Place move
        if (isPlayerXTurn) {
            board[position] = "X"
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_x)
            button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null) // Clear previous if any (good practice) or just set background
             // Spec says "placing X (red vector drawable) ... on buttons". 
             // Usually this is set as src or background or compound drawable. 
             // Let's use foreground or background or compound drawable. Attempts to be safe: compound drawable
             // Actually, `setCompoundDrawables` is requested in reset ("Clears all button drawables ... using setCompoundDrawables(null, null, null, null)")
             // So we should use setCompoundDrawablesWithIntrinsicBounds for setting it.
             // We need to resize or assume drawable is fit. The vector is 24dp. Button is large.
             // Let's use setForeground if API >= 23 or just setCompoundDrawables.
             // Let's stick to setCompoundDrawablesWithIntrinsicBounds which is standard for buttons with icons, or maybe just set background?
             // "Players alternate placing X ... on buttons"
             // Given the "Clears all button drawables ... using setCompoundDrawables" instruction, I will use setCompoundDrawables.
             
             // However, centering might be an issue. Let's use `setCompoundDrawablesWithIntrinsicBounds` and center in XML or code?
             // Buttons in XML just have text size.
             // Let's try setting it as the icon.
             button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
             // Wait, standard is (left, top, right, bottom). To center, often usage is specific or just background.
             // Let's use background for simplicity if not strictly enforced, but "reset" uses setCompoundDrawables.
             // I will use setCompoundDrawablesWithIntrinsicBounds(null, null, null, null) to clear, so I should set it similarly.
             // Let's put it in the Center? Button doesn't support center drawable easily without tricks.
             // Maybe I'll set it as the *background* of the button?
             // "Clears all button drawables (set compound drawables to null...)" implies we ARE using compound drawables.
             // I'll set it to the Top or Bottom? Or maybe just Left/Right?
             // Let's use `foreground` for higher APIs or `background`. 
             // BUT checking the instruction "clears button drawables ... using setCompoundDrawables(null, null, null, null)".
             // This strongly suggests we are using compound drawables.
             // Just setting to `bottom` (last arg) or `top` won't center it.
             // Whatever, I'll set it to `bottom` or similar just to show logic works, OR better:
             // I'll set it as the background drawable and `setCompoundDrawables` is just a reset cleanup instruction that might be generic.
             // Actually, wait. "X (red vector drawable) and O (blue vector drawable) on buttons"
             // If I use `setBackground`, `setCompoundDrawables` won't clear it. `setBackgroundResource(0)` would.
             // The instruction specifically asks to clear compound drawables.
             // So I MUST use compound drawables. I will set it to the *bottom* for now? 
             // Or maybe the user meant `setBackground` but asked for `setCompoundDrawables` cleanup?
             // Let's assume standard behavior: `setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)` puts it left.
             // I will try to be smart: I'll set it as the content of the button if I can?
             // A Button is a TextView. 
             // I will use `setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)` for reset.
             // For setting, I will use `setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0)` maybe?
             // Let's just use `setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, drawable)` (bottom).
             // Actually, if I look at standard implementations, often they use ImageButtons. But here it says "Buttons".
             
             // Re-reading: "Clears all button drawables (set compound drawables to null for all buttons using setCompoundDrawables(null, null, null, null))"
             // This implies `setCompoundDrawables` is the way.
             // I'll just put it in `top` position? Or `left`?
             // I'll use `setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)` to clear.
             // To Set: `button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)` (bottom)
             
             // Wait, the XML has `textSize="40sp"`. This suggests maybe text is used for X and O?
             // But requirement says "X (red vector drawable) ... on buttons" and "Clears button text (set to empty string)".
             // So maybe BOTH are cleared just in case?
             // I'll set the drawable. I will use `setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)` (Top).
             // Ideally it should be centered. With gravity center (default for Button), top/bottom drawables are centered horizontally.
             
             button.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
             
        } else {
            board[position] = "O"
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_o)
            button.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
        }

        // Check for win
        val winInfo = checkWin()
        if (winInfo != null) {
            gameActive = false
            winningLineView.drawWinningLine(winInfo)
            winningLineView.visibility = View.VISIBLE
            
            // Show winner text
            statusTextView.text = if (isPlayerXTurn) getString(R.string.player_x_wins) else getString(R.string.player_o_wins)
            
            playAgainButton.visibility = View.VISIBLE
        } else if (isBoardFull()) {
            // Draw detection
            gameActive = false
            statusTextView.text = getString(R.string.draw)
            playAgainButton.visibility = View.VISIBLE
        } else {
            // Switch turns
            isPlayerXTurn = !isPlayerXTurn
            updateStatusText()
        }
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
        for (i in 0 until 9) {
            board[i] = null
            buttons[i]?.text = ""
            buttons[i]?.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        winningLineView.clearWinningLine()
        winningLineView.visibility = View.VISIBLE // Keep it visible as view, but content cleared
        updateStatusText()
        playAgainButton.visibility = View.GONE
    }

    private fun updateStatusText() {
        if (isPlayerXTurn) {
            statusTextView.text = getString(R.string.player_x_turn)
        } else {
            statusTextView.text = getString(R.string.player_o_turn)
        }
    }

    // Returns array [index, type, winType]
    // winType: 0 = Row, 1 = Col, 2 = Diagonal
    private fun checkWin(): IntArray? {
        val winPositions = arrayOf(
            intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Rows
            intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Cols
            intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)                        // Diagonals
        )

        for ((index, pos) in winPositions.withIndex()) {
            val (a, b, c) = pos
            if (board[a] != null && board[a] == board[b] && board[a] == board[c]) {
                // Determine win type for drawing
                // 0-2: Rows (type 0)
                // 3-5: Cols (type 1)
                // 6-7: Diagonals (type 2)
                
                var type = 0 
                var positionIndex = 0
                
                if (index < 3) {
                    type = 0 // Row
                    positionIndex = index // Row 0, 1, 2
                } else if (index < 6) {
                    type = 1 // Col
                    positionIndex = index - 3 // Col 0, 1, 2
                } else {
                    type = 2 // Diagonal
                    positionIndex = if (index == 6) 0 else 1 // 0: Main, 1: Anti
                }
                
                return intArrayOf(positionIndex, 0, type)
            }
        }
        return null
    }
}
