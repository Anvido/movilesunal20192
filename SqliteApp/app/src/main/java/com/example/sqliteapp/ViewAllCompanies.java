package com.example.sqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.sqliteapp.db.CompanyOperations;
import com.example.sqliteapp.model.Company;

import java.util.List;

public class ViewAllCompanies extends ListActivity {

    private CompanyOperations companyOps;
    private List<Company> companies;
    private ArrayAdapter<Company> adapter;
    private SearchView search;
    private String spinnerValue, queryText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_companies);

        companyOps = new CompanyOperations(this);
        companyOps.open();
        companies = companyOps.getAllCompanies();
        companyOps.close();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, companies);
        setListAdapter(adapter);

        search = findViewById(R.id.simpleSearchView);

            Spinner spinner = (Spinner) findViewById(R.id.class_spinner);
            ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,
                    R.array.class_array, android.R.layout.simple_spinner_item);
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinner_adapter);

        spinnerValue = "";
        queryText = "";

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                companyOps.open();
                companies = companyOps.getCompaniesLike(s, spinnerValue);
                companyOps.close();
                queryText = s;
                adapter = new ArrayAdapter<Company>(ViewAllCompanies.this, android.R.layout.simple_list_item_1, companies);
                setListAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                companyOps.open();
                companies = companyOps.getCompaniesLike(s, spinnerValue);
                companyOps.close();
                queryText = s;
                adapter = new ArrayAdapter<Company>(ViewAllCompanies.this, android.R.layout.simple_list_item_1, companies);
                setListAdapter(adapter);

                return false;
            }
        });

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinnerValue = parent.getItemAtPosition(pos).toString();
                companyOps.open();
                companies = companyOps.getCompaniesLike(queryText, spinnerValue);
                companyOps.close();

                adapter = new ArrayAdapter<Company>(ViewAllCompanies.this, android.R.layout.simple_list_item_1, companies);
                setListAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
