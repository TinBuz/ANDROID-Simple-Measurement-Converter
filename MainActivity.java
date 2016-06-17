package com.example.tinbuzancic.measurementconverter;

/*
 * Purpose: Measurement Converter
 * Author: Tin Buzancic
 */

import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences.Editor;

public class MainActivity extends AppCompatActivity
implements OnEditorActionListener, OnItemSelectedListener {

    //define variables for widgets
    private Spinner conversionSpinner;
    private EditText measurementEditText;
    private TextView preTextView;
    private TextView postTextView;
    private TextView resultTextView;

    private String measurementString = "";
    private float result = 0f;
    private int conversionType = 0;

    //Saved Values holder
    private SharedPreferences saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get ref from widgets
        conversionSpinner = (Spinner) findViewById(R.id.conversionSpinner);
        measurementEditText = (EditText) findViewById(R.id.measurementEditText);
        preTextView = (TextView) findViewById(R.id.pre);
        postTextView = (TextView) findViewById(R.id.post);
        resultTextView = (TextView) findViewById(R.id.result);


        //set array adapter for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.measurements, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conversionSpinner.setAdapter(adapter);

        //set listeners
        measurementEditText.setOnEditorActionListener(this);
        conversionSpinner.setOnItemSelectedListener(this);

        //get SharedPreferences Object
        saved = getSharedPreferences("Saved Values", MODE_PRIVATE);
        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);
        int spinnerValue = sharedPref.getInt("userChoiceSpinner",-1);
        if(spinnerValue != -1) {
            // set the value of the spinner
            conversionSpinner.setSelection(spinnerValue);
        }
    }

    @Override
    public void onPause() {
        Editor editor = saved.edit();
        editor.putString("measurementString", measurementString);
        editor.putFloat("result", result);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        measurementString = saved.getString("measurementString", "");
        measurementEditText.setText(measurementString);
        result = saved.getFloat("result", result);
        resultTextView.setText(String.format("%.3f", result));

        setTexts();
        convert();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            convert();
        }
        return false;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        conversionType = position + 1;
        int userChoice = conversionSpinner.getSelectedItemPosition();
        SharedPreferences sharedPref = getSharedPreferences("FileName", 0);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("userChoiceSpinner",userChoice);
        prefEditor.commit();

        convert();
        setTexts();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    private void setTexts()
    {
        if(conversionType == 1)
        {
            //Miles to Kilometers
            preTextView.setText(R.string.miles);
            postTextView.setText(R.string.kilometers);
        }
        else
        {
            if(conversionType == 2)
            {
                //Kilometers to Miles
                preTextView.setText(R.string.kilometers);
                postTextView.setText(R.string.miles);
            }

            else
            {
                if(conversionType == 3)
                {
                    //Inches to Centimeters
                    preTextView.setText(R.string.inches);
                    postTextView.setText(R.string.centimeters);
                }

                else
                {
                    if(conversionType == 4)
                    {
                        //Centimeters to Inches
                        preTextView.setText(R.string.centimeters);
                        postTextView.setText(R.string.inches);
                    }
                }
            }
        }
    }

    private void convert() {
        measurementString = measurementEditText.getText().toString().trim();
        float measurement;
        if(measurementString.equals(" "))
        {
            measurement = 0;
        }
        else
        {
            measurement = Float.parseFloat(measurementString);
        }

        if(conversionType == 1)
        {
            //Miles to Kilometers
            measurement = measurement * 1.6093f;
            result = measurement;
            resultTextView.setText(String.format("%.3f", measurement));
        }
        else
        {
            if(conversionType == 2)
            {
                //Kilometers to Miles
                measurement = measurement * 0.6214f;
                result = measurement;
                resultTextView.setText(String.format("%.3f", measurement));
            }

            else
            {
                if(conversionType == 3)
                {
                    //Inches to Centimeters
                    measurement = measurement * 2.54f;
                    result = measurement;
                    resultTextView.setText(String.format("%.3f", measurement));
                }

                else
                {
                    if(conversionType == 4)
                    {
                        //Centimeters to Inches
                        measurement = measurement * 0.3937f;
                        result = measurement;
                        resultTextView.setText(String.format("%.3f", measurement));
                    }
                }
            }
        }
    }
}
