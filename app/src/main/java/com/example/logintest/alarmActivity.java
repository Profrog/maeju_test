package com.example.logintest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

public class alarmActivity extends AppCompatActivity {
    //알람 상태 표시
    Boolean dailyNotify_attend = false;
    Boolean dailyNotify_quiz = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_set);

        //출석 달력
        Calendar calendar_attend = Calendar.getInstance();
        //퀴즈 달력
        Calendar calendar_quiz = Calendar.getInstance();

        //알람 확인
        //출석
        Intent alarmIntent_attend = new Intent(this, AlarmReceiver_attend.class);
        PendingIntent pendingIntent_attend = PendingIntent.getBroadcast(this, 0, alarmIntent_attend, PendingIntent.FLAG_NO_CREATE);
        //퀴즈
        Intent alarmIntent_quiz = new Intent(this, AlarmReceiver_quiz.class);
        PendingIntent pendingIntent_quiz = PendingIntent.getBroadcast(this, 0, alarmIntent_quiz, PendingIntent.FLAG_NO_CREATE);

        //출석
        Button btn_attend = (Button) findViewById(R.id.btn_attend);
        if(pendingIntent_attend==null){
            btn_attend.setText("ON");
            btn_attend.setBackgroundColor(Color.parseColor("#A2C97F"));
            dailyNotify_attend=true;
        }
        else{
            btn_attend.setText("OFF");
            btn_attend.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        //퀴즈
        Button btn_quiz = (Button) findViewById(R.id.btn_quiz);
        if(pendingIntent_quiz==null){
            btn_quiz.setText("ON");
            btn_quiz.setBackgroundColor(Color.parseColor("#A2C97F"));
            dailyNotify_quiz=true;
        }
        else{
            btn_quiz.setText("OFF");
            btn_quiz.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        //버튼 눌렀을 때
        //출석
        btn_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //알람의 상태
                if(dailyNotify_attend){//알람을 켤 경우
                    btn_attend.setText("OFF");
                    btn_attend.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    dailyNotify_attend=true;
                    calendar_attend.setTimeInMillis(System.currentTimeMillis());
                    calendar_attend.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    calendar_attend.set(Calendar.HOUR_OF_DAY, 21);
                    calendar_attend.set(Calendar.MINUTE, 0);
                    calendar_attend.set(Calendar.SECOND, 0);

                    // 이미 지난 시간을 지정했다면 일주일 뒤 같은 시간으로 설정
                    if (calendar_attend.before(Calendar.getInstance())) {
                        calendar_attend.add(Calendar.DATE, 7);
                    }

                    Date currentDateTime = calendar_attend.getTime();
                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분", Locale.getDefault()).format(currentDateTime);
                    Toast.makeText(getApplicationContext(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

                    //  Preference에 설정한 값 저장
                    SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                    editor.putLong("nextNotifyTime", (long) calendar_attend.getTimeInMillis());
                    editor.apply();

                    diaryNotification_attend(calendar_attend);
                }
                else{//알람을 해제할 경우
                    dailyNotify_attend=false;
                    btn_attend.setBackgroundColor(Color.parseColor("#A2C97F"));
                    btn_attend.setText("ON");
                    diaryNotification_attend(calendar_attend);
                }
            }
        });
        //퀴즈
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //알람의 상태
                if(dailyNotify_quiz){//알람을 켤 경우
                    btn_quiz.setText("OFF");
                    btn_quiz.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    dailyNotify_quiz=true;
                    calendar_quiz.setTimeInMillis(System.currentTimeMillis());
                    calendar_quiz.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
                    calendar_quiz.set(Calendar.HOUR_OF_DAY, 21);
                    calendar_quiz.set(Calendar.MINUTE, 0);
                    calendar_quiz.set(Calendar.SECOND, 0);

                    // 이미 지난 시간을 지정했다면 일주일 뒤 같은 시간으로 설정
                    if (calendar_quiz.before(Calendar.getInstance())) {
                        calendar_quiz.add(Calendar.DATE, 7);
                    }

                    Date currentDateTime = calendar_quiz.getTime();
                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분", Locale.getDefault()).format(currentDateTime);
                    Toast.makeText(getApplicationContext(), date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

                    //  Preference에 설정한 값 저장
                    SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                    editor.putLong("nextNotifyTime", (long) calendar_quiz.getTimeInMillis());
                    editor.apply();

                    diaryNotification_quiz(calendar_quiz);
                }
                else{//알람을 해제할 경우
                    dailyNotify_attend=false;
                    btn_quiz.setText("ON");
                    btn_quiz.setBackgroundColor(Color.parseColor("#A2C97F"));
                    diaryNotification_quiz(calendar_quiz);
                }
            }
        });


    }
    //출석
    void diaryNotification_attend(Calendar calendar) {
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver_attend.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver_attend.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify_attend) {
            if (alarmManager != null) {
                //long interval=1000*60;//1s=1000, 1m=1000*60, 1h=1000*60*60//AlarmManager.INTERVAL_DAY*7
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7, pendingIntent);
            }
            dailyNotify_attend=false;
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
            dailyNotify_attend=true;
            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }
    //퀴즈
    void diaryNotification_quiz(Calendar calendar) {
        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver_quiz.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver_quiz.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify_quiz) {
            if (alarmManager != null) {
                //long interval=1000*60;//1s=1000, 1m=1000*60, 1h=1000*60*60//AlarmManager.INTERVAL_DAY*7
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7, pendingIntent);
            }
            dailyNotify_quiz=false;
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
            dailyNotify_quiz=true;
            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }
}




