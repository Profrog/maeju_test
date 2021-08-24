package com.example.logintest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReserveActivity_quiz extends AppCompatActivity {
    Boolean dailyNotify = false; // 무조건 알람을 사용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_quiz);
        Calendar calendar = Calendar.getInstance();

        //알람 확인
        TextView status=(TextView) findViewById(R.id.status);

        Intent alarmIntent = new Intent(this, AlarmReceiver_quiz.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE);

        if(pendingIntent==null){
            status.setText("상태: 꺼짐");
        }
        else{
            status.setText("상태: 켜짐");
        }

        Button buttonOn = (Button) findViewById(R.id.buttonOn);
        buttonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                status.setText("상태: 켜짐");
                dailyNotify=true;
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                calendar.set(Calendar.HOUR_OF_DAY, 21);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 7);
                }

                Date currentDateTime = calendar.getTime();
                String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                Toast.makeText(getApplicationContext(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

                //  Preference에 설정한 값 저장
                SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                editor.putLong("nextNotifyTime", (long) calendar.getTimeInMillis());
                editor.apply();

                diaryNotification(calendar);
            }
        });

        Button buttonOff = (Button) findViewById(R.id.buttonOff);
        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dailyNotify=false;
                status.setText("상태: 꺼짐");
                diaryNotification(calendar);
            }
        });
    }

    void diaryNotification(Calendar calendar) {
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver_quiz.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver_quiz.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                //long interval=1000*60;//1s=1000, 1m=1000*60, 1h=1000*60*60//AlarmManager.INTERVAL_DAY*7
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000*60, pendingIntent);
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
        else{
            if(alarmManager!=null){
                Toast.makeText(getApplicationContext(), "알람이 해제되었습니다!", Toast.LENGTH_SHORT).show();
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }
}