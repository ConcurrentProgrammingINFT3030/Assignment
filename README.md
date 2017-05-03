# Multiplayer Snake Game
*PLEASE CREATE A NEW BRANCH FOR EXPERIMENTING*

## LTSA MODEL
```markdown
//SOMEWHAT OF AN LTSA
//Direction moving
SNAKE = (up -> SNAKEUP | down -> SNAKEDOWN | left -> SNAKELEFT | right -> SNAKERIGHT),
SNAKEUP = (left -> SNAKELEFT | right -> SNAKERIGHT),
SNAKEDOWN  = (left -> SNAKELEFT | right -> SNAKERIGHT),
SNAKELEFT = (up -> SNAKEUP | down -> SNAKEDOWN),
SNAKERIGHT= (up -> SNAKEUP | down -> SNAKEDOWN).

THREAD = (wait -> BLOCKED | unblockAll -> THREAD),
BLOCKED = (unblockAll -> move -> THREAD).

||PLAYER = (SNAKE||THREAD/{up/move,down/move,left/move,right/move}).

SCHEDULER = RUNNINGALL,
RUNNINGALL = (snakeA.wait -> SNAKEA |snakeB.wait -> SNAKEB |{snakeA,snakeB}.notifyAll -> RUNNINGALL),
SNAKEA = (snakeB.wait -> SNAKEAB |snakeB.notifyAll -> unblockAll -> RUNNINGALL),
SNAKEB = (snakeA.wait -> SNAKEAB |snakeA.notifyAll -> unblockAll -> RUNNINGALL),
SNAKEAB = (unblockAll -> RUNNINGALL). // Should not happen

set Threads = {snakeA,snakeB} //the set of thread identifiers
||WAITSET = (Threads:PLAYER/{unblockAll/Threads.unblockAll} || SCHEDULER).
```

### ----------------

## TODO:
```markdown
- Implement concurrency
- Create a scoring system
- add delay to uncontrollable snakes random movement
```


## DONE:
```markdown
- Make the program act more like a server-client system
- Client auth
- Create snake game
- Ability to add multipule snakes that all have the same functionality
- Make 4 of the snakes controllable
- 
```



## Explanation of the Entity Class
The original snake game had two 2D arrays that contained the grid data and the snake's positions as Integers. Obviously this design doesn't easily allow more than one snake.
To remedy this I replaced the the grid of Integers with a grid of Entity objects. Entity objects can be of three types; *FOOD*, *SNAKE* or *EMPTY*. This allows the game board to contain multipule snake objects each knowing their location, size, id, etc. Thus we can easily see which two snakes collide and where.


## Explanation of concurrency






