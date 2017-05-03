# Multiplayer Snake Game
*PLEASE CREATE A NEW BRANCH FOR EXPERIMENTING*


### ----------------

##TODO:
```markdown
- Implement concurrency
- Create a scoring system
- add delay to uncontrollable snakes random movement
```


##DONE:
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
Inside the *Game.java* class there is a function called mainLoop(), which contains a while loop that runs until a snake collides or the game is paused. This loop also controls all of the movement and actions a snake takes. It currently is not concurrent and each snake's movement is calculated one after the other (as it iterates the playerList), concurrency needs to be implemented within the mainLoop function.





