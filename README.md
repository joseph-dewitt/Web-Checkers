# Web-Checkers
An online game of checkers, capable to instantiating multiple games between multiple players.

To start the server running (in Linux):
From the Webcheckers directory container bin, lib, and src, enter:
$ javac -d bin -sourcepath src -cp "lib/*" src/checkers/server/CheckersSocket.java
$ java -cp "bin:lib/*" checkers.server.CheckersSocket

To create a client, in the same directory enter:
$ javac -d bin -sourcepath src -cp "lib/*" src/checkers/client/CheckersClientEndpoint.java
$ java -cp "bin:lib/*" checkers.client.CheckersClientEndpoint

Create two clients to start a game!
