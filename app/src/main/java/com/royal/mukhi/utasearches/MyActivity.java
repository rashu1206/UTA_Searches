package com.royal.mukhi.utasearches;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.content.SharedPreferences;
import java.util.ArrayList;
import android.view.View;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MyActivity extends Activity {

    SharedPreferences sharedPreference;

    ImageButton button;
    EditText query;
    EditText tag;
    TextView tv;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> tags;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        query = (EditText) findViewById(R.id.editText);
        tag = (EditText) findViewById(R.id.editText2);
        button = (ImageButton) findViewById(R.id.save_button);
        listView = (ListView) findViewById(R.id.listView);
        tv = (TextView) findViewById(R.id.textView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tags);
        listView.setAdapter(adapter);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String input1 = query.getText().toString();
                String input = tag.getText().toString();

                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Enter UTA search query", input1);
                editor.putString("Tag your query", input);
                editor.commit();

                {
                    query.setText("");

                    loadPreferences();

                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE
                    )).hideSoftInputFromWindow(tag.getWindowToken(), 0);
                    if (null != input && input.length() > 0) {

                        if (tags.contains(input)) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                            alertDialogBuilder.setMessage("Tag already exist!");
                            alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            alertDialog.show();
                        } else {
                            tags.add(input);
                            adapter.notifyDataSetChanged();
                            tag.setText("");
                        }
                    }
                }

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Share, Edit or Delete the search tagged as" + listView.indexOf(position));

                listView.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    public void loadPreferences() {
        sharedPreference = getPreferences(MODE_PRIVATE);
        String pesonData = sharedPreference.getString("tag", String.valueOf(2));

    }

    public void onShareClick() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

