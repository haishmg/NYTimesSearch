package com.example.hganeshmurthy.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.hganeshmurthy.nytimessearch.DatePickerFragment;
import com.example.hganeshmurthy.nytimessearch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {
    @Bind(R.id.etDate) EditText etDate;
    @Bind(R.id.spOrder) Spinner spOrder;
    @Bind(R.id.cbArt) CheckBox cbArt;
    @Bind(R.id.cbFashion) CheckBox cbFashion;
    @Bind(R.id.cbSports) CheckBox cbSports;


    boolean bolArt=false;
    boolean bolFashion=false;
    boolean bolSports=false;
    String date;
    String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        date = getIntent().getExtras().getString("date");
        order =  getIntent().getExtras().getString("order");
        bolArt =  getIntent().getExtras().getBoolean("arts");
        bolFashion =  getIntent().getExtras().getBoolean("fashion");
        bolSports =  getIntent().getExtras().getBoolean("sports");

        if (date != null)
         etDate.setText(date);

        if (order != null) {
            int selection = 0;
            if (order.equals("Medium"))
                selection = 1;
            else if (order.equals("Low"))
                selection = 2;
            spOrder.setSelection(selection);
        }

        if (bolArt == true)
            cbArt.setChecked(true);
        if(bolFashion == true)
            cbFashion.setChecked(true);
        if(bolSports == true)
            cbSports.setChecked(true);


    }

    public void callSave(View v)
    {
        Intent i = new Intent();
        i.putExtra("date",etDate.getText().toString());
        i.putExtra("order",spOrder.getSelectedItem().toString());
        i.putExtra("arts",bolArt);
        i.putExtra("fashion",  bolFashion);
        i.putExtra("sports", bolSports);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbArt:
                if (checked)
                    bolArt = true;
                else
                    bolArt = false;
                    break;
            case R.id.cbFashion:
                if (checked)
                    bolFashion = true;
                else
                    bolFashion = false;
                    break;
            case R.id.cbSports:
                if (checked)
                    bolSports = true;
                else
                    bolSports = false;
                    break;

        }
    }


    public void getDateFragment(View v)
    {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        String day=""+dayOfMonth;
        String month=""+monthOfYear;
        if(dayOfMonth < 10)
             day = "0"+dayOfMonth;

        if(monthOfYear < 10)
             month = "0"+monthOfYear;

        etDate.setText(month+"/"+day+"/"+year);
    }

}
