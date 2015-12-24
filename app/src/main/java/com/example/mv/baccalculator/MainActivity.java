/*
   Homework 1
   MainActivity.kava
   Yi Zhuo
   Miruthulavarshini Shanmugam
   Leif Brooks
 */





package com.example.mv.baccalculator;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    double bac = 0.0;
    double r = .73;
    double alcPercentage = 5;
    double W = 0;
    int drinkSize = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action_glowing_purple);

        Button save = (Button) findViewById(R.id.savebutton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText weight = (EditText) findViewById(R.id.weighttext);

                if (weight_validation(weight.getText().toString())) {
                    W = Double.parseDouble(weight.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                }
                Switch sw = (Switch) findViewById(R.id.genderswitch);
                if(sw.isChecked()){
                    r=.73;
                }else {
                    r=.66;
                }
                reset();
            }
        });

        Button reset = (Button) findViewById(R.id.resetbutton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch gender = (Switch) findViewById(R.id.genderswitch);
                gender.setChecked(true);
                r = .73;
                W = 0;
                EditText weight = (EditText) findViewById(R.id.weighttext);
                weight.setText("");
                reset();
            }
        });

        Switch gender = (Switch) findViewById(R.id.genderswitch);
        gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //r = isChecked ? .73 : .66;
                //reset();
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.check(R.id.radioButton1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        drinkSize = 1;
                        break;
                    case R.id.radioButton2:
                        drinkSize = 5;
                        break;
                    case R.id.radioButton3:
                        drinkSize = 12;
                        break;
                }
            }
        });

        SeekBar progress = (SeekBar) findViewById(R.id.seekBar);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress * 5;
                alcPercentage = progress;
                TextView tv = (TextView) findViewById(R.id.indicator);
                tv.setText(alcPercentage + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button addDrink = (Button) findViewById(R.id.adddrinkbutton);
        addDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(W!=0) {
                    calculate_bac();

                    TextView bacView = (TextView) findViewById(R.id.bacleveltextview);
                    bacView.setText("BAC Level: " + String.format("%.4f", bac));

                    ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
                    pb.setVisibility(View.VISIBLE);

                    pb.setProgress((int) (bac*100));
                }
                else {
                    EditText weight = (EditText) findViewById(R.id.weighttext);
                    weight.setError("You did not SET/SAVE the weight yet!");
                }


            }
        });

        EditText weight = (EditText) findViewById(R.id.weighttext);
       // weight.OnFocusChangeListener()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean weight_validation(String weight)
    {
        EditText w = (EditText) findViewById(R.id.weighttext);

        if(weight.isEmpty()){
            w.setError("Enter the weight in lbs");
        } else {
            try {
                int a = Integer.parseInt(weight);
                return( a > 0);
            } catch(Exception E) {
                w.setError("Enter numbers only");
                return false;
            }
        }

        return false;
    }

    public void reset(){
        bac = 0;

        drinkSize = 1;

        SeekBar sb = (SeekBar) findViewById(R.id.seekBar);
        sb.setProgress(1);
        alcPercentage = 1;

        ProgressBar pg = (ProgressBar) findViewById(R.id.progressBar);
        pg.setProgress((int) (bac * 100));
        TextView tv = (TextView) findViewById(R.id.bacleveltextview);
        tv.setText("BAC Level: 0.0");

        TextView status = (TextView) findViewById(R.id.statustextview);
        status.setText("You're safe");
        status.setBackgroundResource(R.drawable.greenrectangle);

        Button addDrink = (Button) findViewById(R.id.adddrinkbutton);
        addDrink.setEnabled(true);

        Button save = (Button) findViewById(R.id.savebutton);
        save.setEnabled(true);

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.check(R.id.radioButton1);

    }

    public void calculate_bac()
    {

        double A = drinkSize*(alcPercentage/100);

        double newBAC = (A*5.15)/(W*r);

        bac += newBAC;
        //bac = bac > .25 ? .25 : bac;
        setStatus();
    }

    public void setStatus() {
        TextView status = (TextView) findViewById(R.id.statustextview);


        if(bac <= .08) {
            status.setText("You're safe");
            status.setBackgroundResource(R.drawable.greenrectangle);
          //  Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();

        } else if(bac < .2) {
            status.setText("Be careful...");
            status.setBackgroundResource(R.drawable.orangerectangle);

        } else if(bac < .25){
            status.setText("Over the limit!!!");
            status.setBackgroundResource(R.drawable.redrectangle);

        } else {
            status.setText("Over the limit!!!");
            status.setBackgroundResource(R.drawable.redrectangle);

            Button addDrink = (Button) findViewById(R.id.adddrinkbutton);
            addDrink.setEnabled(false);

            Button save = (Button) findViewById(R.id.savebutton);
            save.setEnabled(false);


            Toast.makeText(getApplicationContext(),"No more drinks for you!!!",Toast.LENGTH_LONG).show();
        }
    }


}


