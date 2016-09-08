host="http://localhost:8080"

player1=$(curl -X POST --silent --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "name": "Player 1" }' $host/players | jq ".id")
echo Player 1 registered with id $player1

player2=$(curl -X POST --silent --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "name": "Player 2" }' $host/players | jq ".id")
echo Player 2 registered with id $player2

game=$(curl -X POST --silent --header 'Content-Type: application/json' --header "sso: $player1" --header 'Accept: application/json' $host/games | jq ".id")
echo Created game with id $game

function printGameState {
	echo -n "Game state: "
	curl -X GET --silent --header 'Content-Type: application/json' --header "sso: $player1" --header 'Accept: application/json' $host/games/$game | jq "."
}
	 
printGameState

echo Player 1 is about to join the game
curl -X POST --silent --header 'Content-Type: application/json' --header "sso: $player1" --header 'Accept: application/json' -d '{ "color": "BLUE" }' $host/games/$game?action=join > /dev/null
printGameState

echo Player 2 is about to join the game
curl -X POST --silent --header 'Content-Type: application/json' --header "sso: $player2" --header 'Accept: application/json' -d '{ "color": "RED" }' $host/games/$game?action=join > /dev/null
printGameState


status=NONE
while  [ "$status" != next ]; do
	echo Player 2:
	read column
	curl -X POST --silent --header 'Content-Type: application/json' --header "sso: $player2" --header 'Accept: application/json' -d "{ \"column\": $column }" $host/games/$game?action=dropDisc > /dev/null
	printGameState

	echo Player 1:
	read column
	curl -X POST --silent --header 'Content-Type: application/json' --header "sso: $player1" --header 'Accept: application/json' -d "{ \"column\": $column }" $host/games/$game?action=dropDisc > /dev/null
	printGameState
done

