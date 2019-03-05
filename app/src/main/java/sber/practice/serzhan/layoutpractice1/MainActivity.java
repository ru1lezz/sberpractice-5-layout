package sber.practice.serzhan.layoutpractice1;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean mBound;
    private static final String SEND_MESSAGE_FILTER = "sber.practice.serzhan.layoutpractice1.SEND_MESSAGES_FILTER";

    private IntentFilter intentFilter;
    private MyReceiver receiver;
    private Messenger mService;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private Button mRelativeButton1;
    private Button mRelativeButton2;
    private Button mRelativeButton3;
    private Button mRelativeButton4;
    private Button mRelativeButton5;
    private Button mConstraintButton1;
    private Button mConstraintButton2;
    private TextView mVerticalTextView1;
    private TextView mVerticalTextView2;
    private TextView mVerticalTextView3;
    private TextView mHorizontalTextView1;
    private TextView mHorizontalTextView2;
    private TextView mHorizontalTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(MyIntentService.newIntent(MainActivity.this));
        bindService();
        registerReceiver(receiver, intentFilter,null,null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(MyIntentService.newIntent(MainActivity.this));
        unbindService();
        unregisterReceiver(receiver);
    }

    private void initViews() {
        mRelativeButton1 = findViewById(R.id.relative_button1);
        mRelativeButton2 = findViewById(R.id.relative_button2);
        mRelativeButton3 = findViewById(R.id.relative_button3);
        mRelativeButton4 = findViewById(R.id.relative_button4);
        mRelativeButton5 = findViewById(R.id.relative_button5);
        mConstraintButton1 = findViewById(R.id.constraint_button1);
        mConstraintButton2 = findViewById(R.id.constraint_button2);
        mVerticalTextView1 = findViewById(R.id.vertical_textview1);
        mVerticalTextView2 = findViewById(R.id.vertical_textview2);
        mVerticalTextView3 = findViewById(R.id.vertical_textview3);
        mHorizontalTextView1 = findViewById(R.id.horizontal_textview1);
        mHorizontalTextView2 = findViewById(R.id.horizontal_textview2);
        mHorizontalTextView3 = findViewById(R.id.horizontal_textview3);
    }

    private void init() {
        receiver = new MyReceiver(new ViewCallbackLinear());
        intentFilter = new IntentFilter(SEND_MESSAGE_FILTER);
    }

    private void bindService() {
        Log.i("MainActivity", "bindService method");
        bindService(MyService.newIntent(MainActivity.this), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        Message msg = Message.obtain(null, MyService.MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;
        if(mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("MainActivity", "onServiceConnected method is starting");
            mService = new Messenger(service);
            Message msg = Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
                Log.i("MainActivity", "message sended");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    private class ViewCallbackLinear implements ViewCallback {
        @Override
        public void onUpdate(String value) {
            setTextForTextViews(value);
        }
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setTextForButtons(msg);
            rotateButton();
        }
    }

    private void setTextForTextViews(String value) {
        mHorizontalTextView1.setText(value);
        mHorizontalTextView2.setText(value);
        mHorizontalTextView3.setText(value);
        mVerticalTextView1.setText(value);
        mVerticalTextView2.setText(value);
        mVerticalTextView3.setText(value);
    }

    private void setTextForButtons(Message msg) {
        String value = msg.getData().getString("VALUE");
        mRelativeButton1.setText(value);
        mRelativeButton2.setText(value);
        mRelativeButton3.setText(value);
        mRelativeButton4.setText(value);
        mRelativeButton5.setText(value);
        mConstraintButton1.setText(value);
        mConstraintButton2.setText(value);
    }

    private void rotateButton() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mConstraintButton2.getLayoutParams();
        layoutParams.circleAngle += 15;
        mConstraintButton2.setLayoutParams(layoutParams);
    }
}
