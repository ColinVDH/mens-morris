# Men's Morris 


<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">1. Rules </a></li>
<li><a href="#sec-2">2. Features </a></li>
<li><a href="#sec-3">3. Game Logic </a></li>
</div>
</div>



## Rules<a id="sec-1" name="sec-1"></a>

Men's Morris games are played on a grid of interconnected points. Each player is given pieces or "men" to place on the board. 
To win the game, you must to reduce your opponent to two men, or block all of his legal moves. The game proceeds in two phases:

1. Placement: Randomly determine who goes first, then alternate placing one piece on the board.
2. Movement: If you have finished placing all your men, you now pick a man on the board to move. The man is (normally) only allowed to move to an empty place connected by a line from its current position.

In either phase, if you manage to form a "mill" of three men in a row, you may pick one enemy piece to remove. 
You are not allowed to remove a piece that is part of a mill, unless there are no other options.

### Game Modes
#### Six Men's Morris
- Simplest variation
- Each player is given 6 men at the beginning of the game
- Played on a 16-point grid
- Normal rules apply
  
#### Nine Men's Morris
- Most common variation.
- Each player is given 9 men at the beginning of the game
- Played on a 24-point grid
- If a player is reduced to 3 or fewer men, they are now allowed to "fly". On their turn, they may move a man to any empty spot on the board, not only adjacent spots

#### Twelve Men's Morris
- Popular in South Africa where it is referred to as "Morabaraba"
- Each player is given 12 men at the beginning of the game
- Played on a 24-point grid with extra connections
- "Flying" is allowed, subject to the same rules as Nine Men's Morris

## Features<a id="sec-2" name="sec-2"></a>

- Three distinct game-modes: Six Men's Morris, Nine Men's Morris, Twelve Men's Morris
- Player vs. Computer, Player vs. Player 
- Save Game Progress to file.

## Game Logic <a id="sec-3" name="sec-3"></a>

In Player vs. Computer mode, move decisions by the computer are made using a Monte Carlo Tree Search (MCTS) algorithm. 
MCTS is a heuristic search algorithm that combines tree search and random simulations. 
It has been shown to converge to the minimax optimal move as iterations increase. 

1.	Selection. Starting at the root node (current game state), select the optimal child nodes recursively, using Upper Confidence Bounds 1 applied to Trees (UCT) formula.
2.	Expansion. If the leaf node is not terminal (end game state), create all child nodes and randomly select one. 
3.	Simulation. Run a randomly simulated “playout” from this child node.
4.	Backpropagation. Move back up the tree, update all nodes with the simulation result, until you reach the root. 
5.	Perform X repeats of steps 1 to 4. 
