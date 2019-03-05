package sber.practice.serzhan.layoutpractice1;

import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyService extends Service {

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    private List<Messenger> mClients = new ArrayList<>();
    private Messenger mMessenger = new Messenger(new IncomingHandler());

    public MyService() {}

    @Override
    public void onCreate() {
        sendMessageToClients();
    }

    private void sendMessageToClients() {
        new Thread(() -> {
            while(true) {
                for(Messenger client: mClients) {
                    try {
                        client.send(createMessageWithData());
                        Log.i("MyService", "message sended");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Message createMessageWithData() {
        Message msg = new Message();
        Bundle value = new Bundle();
        value.putString("VALUE", getRandomValue());
        msg.setData(value);
        return msg;
    }

    private String getRandomValue() {
        return String.valueOf(new Random().nextInt(100));
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_REGISTER_CLIENT:
                    Log.v("MyService", "registering client");
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MyService", "onBind method");
        return mMessenger.getBinder();
    }

    public static final Intent newIntent(Context context) {
        return new Intent(context, MyService.class);
    }

}
