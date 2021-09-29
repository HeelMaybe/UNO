# UNO
This app is the first individual project for the class **ITIS 5280** at UNCC, made by Graham Helton. It consists of a Mobile Application to play UNO, with support of:
- Authentication with your email
- Real-time playing with people using the app
- Create gamesrooms
- See who's turn it is
- See who wins the game


## Technologies

In order to develop this app, we used:
- An Android Application as the **front-end**
	- For Android 10 (API level 29)
	- Material Design as the style guideline
	- JetPack for the navigation handling
	- PrettyTime for friendly date-time formatting
- Firebase as the **back-end**
	- Firebase Authentication as the authentication provider
	- FireStore as the database

## Data Design
The main collections in the database are the following:
|Collection Name |Description                    		 |Properties				   |
|----------------|---------------------------------------|-------------------------------------------------------------------------------|
|`users`		 |Information of the users       		 | `Name`, `id`, `city`|
|`{GameId}.game`|Information of the game		     | `createdAt`, `currentCard`, `floorCardsDeck`, `gameId`, `player1CardsDeck`, `player2CardsDeck`,`player1Id`, `player1Name`, `player2Id`, `player2Name`, `status`, `whosTurn`|

## External Resources
- Video explaining the app: 
