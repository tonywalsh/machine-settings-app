package com.freefoam.productionreports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LineIndexActivity extends Activity {
	ListView productLines;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setContentView(R.layout.activity_line_index);
        
        productLines = (ListView) findViewById(R.id.productionLines);
        addLines(this.getIntent().getExtras());
        productLines.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
				Bundle b=new Bundle();
				b.putString("line",((TextView) view).getText().toString());
				Intent intent = new Intent(LineIndexActivity.this, MachineSettingsActivity.class);
				intent.putExtras(b);
				startActivity(intent);
            }
          });

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_line_index, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void addLines(Bundle b){
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, b.getStringArray("lines"));
    	productLines.setAdapter(adapter);	
    }

}
