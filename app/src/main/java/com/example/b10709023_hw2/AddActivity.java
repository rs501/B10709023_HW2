package com.example.b10709023_hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.b10709023_hw2.myDbUtil.MyDbContact;
import com.example.b10709023_hw2.myDbUtil.MyDbHelper;

public class AddActivity extends AppCompatActivity {

    Button btnOk,btnCancel;
    EditText edName,edNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);
        edName = findViewById(R.id.edName);
        edNumber = findViewById(R.id.edNumber);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final MyDbHelper h = new MyDbHelper(this);
        final SQLiteDatabase db = h.getWritableDatabase();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(MyDbContact.WaitList.COLUMN_NAME,edName.getText().toString());
                cv.put(MyDbContact.WaitList.COLUMN_SIZE,Integer.parseInt(edNumber.getText().toString()));
                db.insert(MyDbContact.WaitList.TABLE_NAME,null,cv);
                h.printTable();
                setResult(101);
                finish();
            }
        });
    }
}
