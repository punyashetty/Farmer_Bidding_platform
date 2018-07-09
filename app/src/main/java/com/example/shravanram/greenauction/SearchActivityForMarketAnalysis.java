package com.example.shravanram.greenauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivityForMarketAnalysis extends AppCompatActivity {
    ListView search_prod;
    ArrayAdapter<String> adapter;
    ArrayList<String> produce=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_search_for_market_analysis);
                search_prod=(ListView)findViewById(R.id.search_prod);

                //go to res->values->strings.xml
        //to add any more items to be searched go to res->values->strings.xml
                produce.addAll(Arrays.asList(getResources().getStringArray(R.array.prod)));
                adapter=new ArrayAdapter<String>(
                    SearchActivityForMarketAnalysis.this,android.R.layout.simple_list_item_1,produce) ;
                search_prod.setAdapter(adapter);

                search_prod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(SearchActivityForMarketAnalysis.this,"You selected:"
                                +produce.get(position),Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),MarketAnalysis.class);

                            intent.putExtra("produceSelected", produce.get(position));
                            startActivity(intent);

                        }
                });
                }

                //search button
    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        //item coming from search prod
        MenuItem item = menu.findItem(R.id.search_prod);
        //import android.widget one:
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }
}
