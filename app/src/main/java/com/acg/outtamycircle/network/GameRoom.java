package com.acg.outtamycircle.network;

import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;

public class GameRoom {
    private String sessionHolder;
    private final RealTimeMultiplayerClient client;
    private final GoogleSignInAccount account;
    private static final int MAX_PLAYERS = 4;

    private GameRoom(AndroidGame game, GoogleSignInAccount account){
        if(account == null)
            throw new IllegalArgumentException("GoogleSignInAccount cannot be null");
        if (game == null)
            throw new IllegalArgumentException("Game cannot be null");
        this.account = account;
        client = Games.getRealTimeMultiplayerClient(game, account);
    }

    /**
     * Create a room with specified minimum and maximum number of players
     * @param game
     * @param account
     * @param minPlayers
     * @param maxPlayers
     * @return
     */
    public static GameRoom createRoom(AndroidGame game, GoogleSignInAccount account, int minPlayers, int maxPlayers){
        if(minPlayers<2)
            throw new IllegalArgumentException("Players must be at least 2");
        if(maxPlayers<minPlayers)
            throw new IllegalArgumentException("Max number of players must be grater than minimum");
        if(maxPlayers>MAX_PLAYERS)
            throw new IllegalArgumentException("Max number of players can't exceed "+MAX_PLAYERS);

        GameRoom gm = new GameRoom(game, account);

        return gm;
    }

    public static GameRoom joinRoom(AndroidGame game, GoogleSignInAccount account){
        GameRoom gm = new GameRoom(game, account);
            //TODO see invitations
        return gm;
    }

    /**
     *
     *
     * @return
     */
    public static GameRoom joinRandomRoom(AndroidGame game, GoogleSignInAccount account){
        GameRoom gm = new GameRoom(game, account);

        return gm;
    }


}
