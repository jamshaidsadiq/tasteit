package com.i360ihrd.tasteit.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i360ihrd.tasteit.CartStatus;
import com.i360ihrd.tasteit.Common.Common;
import com.i360ihrd.tasteit.Model.Request;
import com.i360ihrd.tasteit.R;

public
class ListenOrder extends Service implements ChildEventListener {

    FirebaseDatabase database;
    DatabaseReference requests;
    public
    ListenOrder() {
    }

    @Override
    public
    IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public
    void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");


    }

    @Override
    public
    int onStartCommand(Intent intent, int flags, int startId) {
        requests.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public
    void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public
    void onChildChanged(DataSnapshot dataSnapshot, String s) {

        Request req = dataSnapshot.getValue(Request.class);
        showNotification(dataSnapshot.getKey(),req);

    }

    private
    void showNotification(String key, Request req) {
        Intent intent = new Intent(getBaseContext(), CartStatus.class);
        intent.putExtra("userPhone",req.getPhone());
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("360 IHRD")
                .setContentInfo("Your order was updated")
                .setContentText("Order #"+key+" was update status to "+ Common.convertCode(req.getStatus()))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());
    }

    @Override
    public
    void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public
    void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public
    void onCancelled(DatabaseError databaseError) {

    }
}
