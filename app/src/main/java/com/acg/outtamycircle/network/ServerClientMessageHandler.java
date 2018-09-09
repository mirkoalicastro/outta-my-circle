package com.acg.outtamycircle.network;

import android.support.annotation.NonNull;
import android.support.v4.util.Pools;
import android.util.Log;

import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.network.googleimpl.MessageReceiver;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

public class ServerClientMessageHandler implements NetworkMessageHandler {
    private static final int MAX_BUFFER_SIZE = 400; //TODO
    private static final byte ENDING_CHAR = 127;
    private static final int DEFAULT_CAPACITY = 50;

    private MessageReceiver first, second;
    private Pools.Pool<GameMessage> pool;

    //TODO visibility
    byte[] buffer = new byte[MAX_BUFFER_SIZE];
    private int currentBufferSize = 0;

    private final MyGoogleRoom myGoogleRoom;

    public ServerClientMessageHandler(MyGoogleRoom myGoogleRoom) {
        this.myGoogleRoom = myGoogleRoom;
        pool = new Pools.SimplePool<>(DEFAULT_CAPACITY);
        for(int i=0;i<DEFAULT_CAPACITY; i++)
            pool.release(new GameMessage());
    }

    //TODO change
    public ServerClientMessageHandler setReceivers(MessageReceiver first, MessageReceiver second) {
        this.first = first;
        this.second = second;
        return this;
    }

    final String TAG = "JUAN";

    private final RealTimeMultiplayerClient.ReliableMessageSentCallback mywtf = new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
        @Override
        public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientParticipantId) {
            Log.d(TAG, "RealTime message sent");
            Log.d(TAG, "  statusCode: " + statusCode);
            Log.d(TAG, "  tokenId: " + tokenId);
            Log.d(TAG, "  recipientParticipantId: " + recipientParticipantId);
        }
    };

    @Override
    public void sendReliable(final String playerId) {
        byte toSend[] = Arrays.copyOf(buffer,currentBufferSize); //TODO no allocation!
        final Task<Integer> sendTask = myGoogleRoom.getRealTimeMultiplayerClient()
                .sendReliableMessage(toSend, myGoogleRoom.getRoom().getRoomId(), playerId, mywtf);
        //TODO Check
        sendTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Impossibile inviare il messaggio " + e);
            }
        });
        sendTask.addOnCompleteListener(new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(@NonNull Task<Integer> task) {
                Log.d(TAG, "Completato!!!!!");
            }
        });
        sendTask.addOnCanceledListener(new OnCanceledListener() {
               @Override
               public void onCanceled() {
                   Log.d(TAG, "Canceled");
               }
        });
    }


    @Override
    public void sendUnreliable(String player) {
        myGoogleRoom.getRealTimeMultiplayerClient().sendUnreliableMessage(buffer, myGoogleRoom.getRoom().getRoomId(), player);
    }

    @Override
    public void broadcastReliable(){
        //TODO
        for(Participant player: myGoogleRoom.getRoom().getParticipants()) {
            String playerId = player.getParticipantId();
            if(!playerId.equals(myGoogleRoom.getPlayerId())) {
                sendReliable(playerId);
            }
        }
    }

    @Override
    public void broadcastUnreliable() {
        final Task<Void> task = myGoogleRoom.getRealTimeMultiplayerClient().sendUnreliableMessageToOthers(buffer, myGoogleRoom.getRoom().getRoomId());
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"failure " + e);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "complete and success? E' succies che magg appiciat! A casa se distrutta e nu bell mensil che tenevo stipat nu teng chiu" + task.isSuccessful());
            }
        });
    }

    @Override
    public void putInBuffer(GameMessage message) {
        //TODO se ci sono troppi messaggi? RuntimeException?
        GameMessage.Type type = message.getType();
        message.putInBuffer(buffer, currentBufferSize);
        currentBufferSize += type.length;
        buffer[currentBufferSize] = ENDING_CHAR;
    }

    @Override
    public synchronized Iterable<GameMessage> getMessages() {
        MessageReceiver tmp = first;
        second.clear();
        first = second;
        second = tmp;
        return tmp.getMessages();
    }

    @Override
    public synchronized void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
        byte[] messageData = realTimeMessage.getMessageData();
        int cursor = 0;

        while( cursor < messageData.length && messageData[cursor]!=ENDING_CHAR) {
            GameMessage gameMessage = new GameMessage();//pool.acquire(); //TODO pool handle
            gameMessage.setSender(realTimeMessage.getSenderParticipantId());
            int length = GameMessage.Type.values()[messageData[cursor]].length;
            gameMessage.copyBuffer(messageData, cursor, cursor + length - 1); //TODO check
            first.storeMessage(gameMessage);
            cursor += length;
        }
    }

    @Override
    public void clearBuffer(){
        currentBufferSize = 0;
        buffer[0] = ENDING_CHAR;
    }
}
