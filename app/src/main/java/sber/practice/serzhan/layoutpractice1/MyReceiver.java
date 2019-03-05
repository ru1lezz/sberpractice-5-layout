package sber.practice.serzhan.layoutpractice1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    private ViewCallback viewCallback;

    public MyReceiver() {}

    public MyReceiver(ViewCallback viewCallback) {
        this.viewCallback = viewCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        viewCallback.onUpdate(String.valueOf(intent.getStringExtra("VALUE")));
    }
}
