# Multiplayer Snake Game
*PLEASE CREATE A NEW BRANCH FOR EXPERIMENTING*

## LTSA MODEL
```markdown
BUFFER(N=5) = COUNT[0],
COUNT[i:0..N]
      = (when (i<N) putMove->COUNT[i+1]
        |when (i>0) getMove->COUNT[i-1]
        ).
 
SERVER=(start->LOOP),
LOOP=(processLogin->HANDLECLIENT),
HANDLECLIENT=(getMove->processMove->HANDLECLIENT | processLogout->LOOP).
 
PLAYER=(login->PLAY),
PLAY=(putMove->PLAY | logout->PLAYER).
 
||SERVERPLAYER = (PLAYER||BUFFER(2)||SERVER)/{processLogin/login,processLogout/logout}.
```

### ----------------
