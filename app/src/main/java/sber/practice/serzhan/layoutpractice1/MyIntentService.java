package sber.practice.serzhan.layoutpractice1;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.Random;

public class MyIntentService extends IntentService {

    private static final String SEND_MESSAGE_FILTER = "sber.practice.serzhan.layoutpractice1.SEND_MESSAGES_FILTER";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while(true) {
            Intent broadcastIntent = new Intent(SEND_MESSAGE_FILTER);
            broadcastIntent.putExtra("VALUE", getRandomValue());
            sendBroadcast(broadcastIntent);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRandomValue() {
        return String.valueOf(new Random().nextInt(100));
    }

    public static final Intent newIntent(Context context) {
        return new Intent(context, MyIntentService.class);
    }
}
