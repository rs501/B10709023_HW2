package com.example.b10709023_hw2;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.b10709023_hw2.myDbUtil.MyDbContact;
import com.example.b10709023_hw2.myDbUtil.MyDbHelper;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    static RecyclerView rv;
    static MyDbHelper myDbHelper;
    static SQLiteDatabase myDb;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String a = "123";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDbHelper = new MyDbHelper(this);
        myDb = myDbHelper.getWritableDatabase();

        ShapeDrawable s = new ShapeDrawable();
        s.setShape(new OvalShape());


        myAdapter = new MyAdapter(this,getGuest(),s);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(myAdapter);

        final Context context = this;
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final long id = (long)viewHolder.itemView.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert")
                        .setMessage("確認刪除?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeGuest(id);
                                myAdapter.reset(getGuest());
                                System.out.println("sout : swiped");
                                myDbHelper.printTable();
                            }
                        });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);

                AlertDialog a = builder.create();
                a.show();
            }
        }).attachToRecyclerView(rv);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        setColor(sp.getString("color","R"));

        sp.registerOnSharedPreferenceChangeListener(this);
    }
    private Cursor getGuest(){
        return myDb.query(MyDbContact.WaitList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MyDbContact.WaitList.COLUMN_TIMESTAMP);
    }

    private void removeGuest(long id){
        //String[] a = {MyDbContact.WaitList._ID,id+""};
        //myDb.delete(MyDbContact.WaitList.TABLE_NAME,"?=?",a);
        myDb.execSQL(String.format("delete from %s where %s = %s",MyDbContact.WaitList.TABLE_NAME,
                                                                    MyDbContact.WaitList._ID,
                                                                    id+""));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.add){
            Intent intent = new Intent(this, AddActivity.class);
            startActivityForResult(intent,100);
            return true;
        }
        if(item.getItemId() == R.id.setting){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int request,int response,Intent data) {
        super.onActivityResult(request, response, data);
        if(request==100&&response==101) {
            myAdapter.reset(getGuest());
        }
    }
    private void setColor(String color){
        switch (color){
            case "R":
                myAdapter.setColor(getColor(R.color.red));
                break;
            case "G":
                myAdapter.setColor(getColor(R.color.green));
                break;
            case "B":
                myAdapter.setColor(getColor(R.color.blue));
                break;
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("color")){
            setColor(sharedPreferences.getString(key,"red"));
        }
    }
}
