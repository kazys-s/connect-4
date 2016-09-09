# Connect 4

## Building and starting application

```
mvn package && java -jar webapp/target/webapp-1.0.0-SNAPSHOT.jar
```

## Service endpoints

### Player endpoints

Endpoints share the same response object:
```json
{ "id": 89, "name": "John Smith" }
```

#### POST /players - register new player
Sample request:

```json
{ "name": "John Smith" }
```

#### GET /players - list all players
#### GET /players/:id - get particular player

### Game endpoints

*NOTICE* the following endpoints requires client to be authenticated. You can "authenticate" by sending a header "sso" containing the id of a player

Endpoints share the same response object:
```json
{
  "id": 1,
  "status": "IN_PROGRESS",
  "slots": [
    {
      "playerId": 1,
      "color": "BLUE"
    },
    {
      "playerId": 2,
      "color": "RED"
    }
  ],
  "board": [
    ".......",
    ".......",
    ".......",
    ".......",
    ".......",
    "......."
  ],
  "currentPlayer": 2
}
```

#### POST /games - create a new player
#### GET /games - list all games
#### GET /games/:id - get particular game
#### POST /games/:id?action=join - join game
Sample request:
```json
{ "color": "RED" }
```

400 will be returned in case:
- Game has already started or finished
- Player is trying to join the same game twice
- The color player is asking for is taken

#### POST /games/:id?action=dropDisc - drop disc
Sample request:
```json
{ "column": 0 }
```

400 will be returned in case:
- Game has not started yet or has finished already 
- Disc is being dropped into full or invalid column
- Player is trying to make a move when it's not his turn


