package brianslho.gyrotest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor grvSensor, smSensor, pSensor;
    private SensorEventListener mEventListener;
    private ImageView ua1, ua2, ua3, da1, da2, da3, la1, la2, la3, ra1, ra2, ra3;
    private TextView txt;
    private ImageButton startStop, lightBtn, soundBtn;
    private Boolean start = true;
    private Integer triggerMode = 2;    // 0 is light, 1 is sound
    public OutputSignal outSig = new OutputSignal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Initialize elements
        txt = findViewById(R.id.txt);
        startStop = findViewById(R.id.stop_btn);
        lightBtn = findViewById(R.id.light_btn);
        soundBtn = findViewById(R.id.sound_btn);
        ua1 = findViewById(R.id.ua1);
        ua2 = findViewById(R.id.ua2);
        ua3 = findViewById(R.id.ua3);
        da1 = findViewById(R.id.da1);
        da2 = findViewById(R.id.da2);
        da3 = findViewById(R.id.da3);
        la1 = findViewById(R.id.la1);
        la2 = findViewById(R.id.la2);
        la3 = findViewById(R.id.la3);
        ra1 = findViewById(R.id.ra1);
        ra2 = findViewById(R.id.ra2);
        ra3 = findViewById(R.id.ra3);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        grvSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        smSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        pSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Set arrow opacity
        ua1.setImageAlpha(100);ua2.setImageAlpha(100);ua3.setImageAlpha(100);
        da1.setImageAlpha(100);da2.setImageAlpha(100);da3.setImageAlpha(100);
        la1.setImageAlpha(100);la2.setImageAlpha(100);la3.setImageAlpha(100);
        ra1.setImageAlpha(100);ra2.setImageAlpha(100);ra3.setImageAlpha(100);

        //Set mode button opacity
        lightBtn.setImageAlpha(100);
        soundBtn.setImageAlpha(100);

        //Set Light Mode
        lightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                triggerMode = 0;
                if(!start){
                    txt.setText("Light Mode now active.");
                    lightBtn.setImageAlpha(255);
                    soundBtn.setImageAlpha(100);
                }
            }
        });

        //Set Sound Mode
        soundBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                triggerMode = 1;
                if(!start){
                    txt.setText("Sound Mode now active.");
                    lightBtn.setImageAlpha(100);
                    soundBtn.setImageAlpha(255);
                }
            }
        });

        //Start and stop car movement
        startStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(start){
                    start = false;
                    startStop.setImageResource(R.drawable.start_btn);
                    if(triggerMode == 0){
                        txt.setText("Light Mode now active.");
                        lightBtn.setImageAlpha(255);
                    } else if(triggerMode == 1){
                        txt.setText("Sound Mode now active.");
                        soundBtn.setImageAlpha(255);
                    } else {
                        txt.setText("Select a Mode.");
                    }
                    outSig.straight = 0;
                    outSig.turn = 0;
                    ua1.setImageAlpha(100);ua2.setImageAlpha(100);ua3.setImageAlpha(100);
                    da1.setImageAlpha(100);da2.setImageAlpha(100);da3.setImageAlpha(100);
                    la1.setImageAlpha(100);la2.setImageAlpha(100);la3.setImageAlpha(100);
                    ra1.setImageAlpha(100);ra2.setImageAlpha(100);ra3.setImageAlpha(100);
                }
                else{
                    start = true;
                    startStop.setImageResource(R.drawable.stop_btn);
                    lightBtn.setImageAlpha(100);
                    soundBtn.setImageAlpha(100);
                    outSig.modeTrigger = 2; // Not necessary here, just for safety.
                }
            }
        });

        mEventListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent sensorEvent){
                if(start){
                    outSig.modeTrigger = 2;
                    //Error msg for no missing sensor
                    if(grvSensor == null){
                        txt.setText("Missing sensor(s) for rotation vector.");
                    }
                    if(sensorEvent.sensor.getType()==Sensor.TYPE_GAME_ROTATION_VECTOR){
                        outSig.straight = sensorEvent.values[1];
                        outSig.turn = sensorEvent.values[0];
                        //Show values in text
                        String value0 = Float.toString(sensorEvent.values[0]);
                        String value1 = Float.toString(sensorEvent.values[1]);
                        txt.setText("v[0]="+value0+" v[1]="+value1);
                        //Opacity settings for UI representation
                        if(sensorEvent.values[1]<0.1 && sensorEvent.values[1]>-0.1){
                            ua1.setImageAlpha(100);ua2.setImageAlpha(100);ua3.setImageAlpha(100);
                            da1.setImageAlpha(100);da2.setImageAlpha(100);da3.setImageAlpha(100);
                        }
                        else if(sensorEvent.values[1]>0.1 && sensorEvent.values[1]<0.2){
                            ua1.setImageAlpha(100);ua2.setImageAlpha(100);ua3.setImageAlpha(255);
                            da1.setImageAlpha(100);da2.setImageAlpha(100);da3.setImageAlpha(100);
                        }
                        else if(sensorEvent.values[1]>0.2 && sensorEvent.values[1]<0.3){
                            ua1.setImageAlpha(100);ua2.setImageAlpha(255);ua3.setImageAlpha(255);
                            da1.setImageAlpha(100);da2.setImageAlpha(100);da3.setImageAlpha(100);
                        }
                        else if(sensorEvent.values[1]>0.3){
                            ua1.setImageAlpha(255);ua2.setImageAlpha(255);ua3.setImageAlpha(255);
                            da1.setImageAlpha(100);da2.setImageAlpha(100);da3.setImageAlpha(100);
                        }
                        else if(sensorEvent.values[1]<-0.1 && sensorEvent.values[1]>-0.2){
                            ua1.setImageAlpha(100);ua2.setImageAlpha(100);ua3.setImageAlpha(100);
                            da1.setImageAlpha(100);da2.setImageAlpha(100);da3.setImageAlpha(255);
                        }
                        else if(sensorEvent.values[1]<-0.2 && sensorEvent.values[1]>-0.3){
                            ua1.setImageAlpha(100);ua2.setImageAlpha(100);ua3.setImageAlpha(100);
                            da1.setImageAlpha(100);da2.setImageAlpha(255);da3.setImageAlpha(255);
                        }
                        else if(sensorEvent.values[1]<-0.3){
                            ua1.setImageAlpha(100);ua2.setImageAlpha(100);ua3.setImageAlpha(100);
                            da1.setImageAlpha(255);da2.setImageAlpha(255);da3.setImageAlpha(255);
                        }

                        if(sensorEvent.values[0]<0.08 && sensorEvent.values[0]>-0.08){
                            la1.setImageAlpha(100);la2.setImageAlpha(100);la3.setImageAlpha(100);
                            ra1.setImageAlpha(100);ra2.setImageAlpha(100);ra3.setImageAlpha(100);
                        }
                        else if(sensorEvent.values[0]>0.08 && sensorEvent.values[0]<0.16){
                            la1.setImageAlpha(100);la2.setImageAlpha(100);la3.setImageAlpha(100);
                            ra1.setImageAlpha(100);ra2.setImageAlpha(100);ra3.setImageAlpha(255);
                        }
                        else if(sensorEvent.values[0]>0.16 && sensorEvent.values[0]<0.24){
                            la1.setImageAlpha(100);la2.setImageAlpha(100);
                            ra1.setImageAlpha(100);ra2.setImageAlpha(255);
                        }
                        else if(sensorEvent.values[0]>0.24){
                            la1.setImageAlpha(100);la2.setImageAlpha(100);la3.setImageAlpha(100);
                            ra1.setImageAlpha(255);ra2.setImageAlpha(255);ra3.setImageAlpha(255);
                        }
                        else if(sensorEvent.values[0]<-0.08 && sensorEvent.values[0]>-0.16){
                            la1.setImageAlpha(100);la2.setImageAlpha(100);la3.setImageAlpha(255);
                            ra1.setImageAlpha(100);ra2.setImageAlpha(100);ra3.setImageAlpha(100);
                        }
                        else if(sensorEvent.values[0]<-0.16 && sensorEvent.values[0]>-0.24){
                            la1.setImageAlpha(100);la2.setImageAlpha(255);la3.setImageAlpha(255);
                            ra1.setImageAlpha(100);ra2.setImageAlpha(100);ra3.setImageAlpha(100);
                        }
                        else if(sensorEvent.values[0]<-0.24){
                            la1.setImageAlpha(255);la2.setImageAlpha(255);la3.setImageAlpha(255);
                            ra1.setImageAlpha(100);ra2.setImageAlpha(100);ra3.setImageAlpha(100);
                        }
                    }
                }
                else{ //Stopped >> Modes

                    //Significant motion sensor trigger
                    if(smSensor == null)txt.setText("Missing sensor for significant motion.");
                    if(sensorEvent.sensor.getType() == Sensor.TYPE_SIGNIFICANT_MOTION){
                        if(triggerMode == 0){
                            outSig.modeTrigger = 0;
                            txt.setText("Light Mode triggered.");
                        }
                        else if(triggerMode == 1){
                            outSig.modeTrigger = 1;
                            txt.setText("Sound Mode triggered.");
                        }
                    }

                    //Proximity sensor trigger
                    if(pSensor == null)txt.setText("Missing sensor for proximity.");
                    if(sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY){
                        String value0 = Float.toString(sensorEvent.values[0]);
                        txt.setText("value[0]=" + value0);
                        if(sensorEvent.values[0] < 1){
                            txt.setText("SECRET MODE ACTIVATED.");
                        }
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i){
                //No function planned
            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(mEventListener, grvSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(mEventListener);
    }
}
