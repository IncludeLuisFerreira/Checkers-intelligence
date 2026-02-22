# Checkers Intelligence

Artificial Intelligence implementation for Checkers (Draughts) on a reduced 6x6 board, developed in Java as an academic project.

## Board Preview

<img src="https://i.imgur.com/g0S09uJ.png" width="400" alt="6x6 Checkers Board">

## Piece Identification

| Value | Piece |
|:-----:|:-----:|
| 0 | Empty square |
| 1 | White piece |
| 2 | Black piece |
| 3 | White king |
| 4 | Black king |

## Game Rules

| Rule | Description |
|------|-------------|
| **Mandatory capture** | When a capture is available, the player must take it |
| **Capture direction** | Regular pieces can only capture **forward** |
| **Multiple capture** | A piece can capture multiple pieces in sequence, provided the first capture is forward |
| **King movement** | Can move any number of squares diagonally |
| **King capture** | Can capture backward and perform multiple captures |
| **King landing** | After capturing, the king lands on the square **immediately after** the last captured piece (in the capture direction) |

## Implemented Features

### Core Game Mechanics
- **Interactive GUI**: 6x6 graphical board using Swing with alternating beige and green squares
- **Piece Selection**: Click to select a piece (highlighted in yellow)
- **Move Visualization**: Possible moves shown in gray; capturable enemy pieces shown in red
- **Turn System**: Alternating turns between White (pieces 1, 3) and Black (pieces 2, 4)
- **Piece Promotion**: Regular pieces promote to King when reaching the opposite end of the board

### Piece Movements
- **Simple Pieces**: Diagonal forward movement (1 square) and forward-only capture
- **Kings**: Multi-square diagonal movement in any direction with capture capabilities

### User Interface Features
- **Visual Feedback**: Color-coded squares indicating selected pieces, valid moves, and capture opportunities
- **Move Cancellation**: Click the same piece again to deselect and cancel move preview
- **Real-time Board Sync**: Graphical interface updates immediately after each valid move

## Project Structure

```
src/
├── Main.java                    # Entry point of the application
├── Entities/
│   ├── Tabuleiro.java           # Board representation and initialization
│   └── CasaBotao.java           # Custom button for board squares
├── Logic/
│   └── LogicTabuleiro.java      # Game logic and move validation
└── UI/
├── MainInterfaceGrafica.java # Main GUI window and event handling
└── PaintTabuleiro.java       # Board rendering and move highlighting
```

## How to Run

1. Compile all Java files:
   ```bash
   javac Main.java Entities/*.java Logic/*.java UI/*.java
   ```

2. Run the application:
   ```bash
   java Main
   ```

## Licence

This project is licensed under the MIT Licence - see the *LICENCE* file for details.