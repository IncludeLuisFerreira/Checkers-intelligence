# Checkers Intelligence

Artificial Intelligence implementation for Checkers (Draughts) on a reduced 6x6 board, developed in Java as an academic project.

## Board Preview

<img src="https://i.imgur.com/xMjmPeg.png" width="400" alt="6x6 Checkers Board">;

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

## Licence

This project is licensed under the MIT Licence - see the *LICENCE* file fpr details.