package com.example.sqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sqliteapp.db.CompanyOperations;
import com.example.sqliteapp.model.Company;

public class AddUpdateCompany extends AppCompatActivity {

    private static final String EXTRA_EMP_ID = "com.sqliteapp.compId";
    private static final String EXTRA_ADD_UPDATE = "com.sqliteapp.add_update";
    private EditText name;
    private EditText web;
    private EditText number;
    private EditText email;
    private EditText products;
    private EditText services;
    private Button addUpdateButton;
    private Company newCompany;
    private Company oldCompany;
    private String mode, spinnerValue;
    private long empId;
    private CompanyOperations companyData;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinner_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_company);

        newCompany = new Company();
        oldCompany = new Company();
        name = (EditText) findViewById(R.id.edit_name);
        web = (EditText) findViewById(R.id.edit_web);
        number = (EditText) findViewById(R.id.edit_number);
        email = (EditText) findViewById(R.id.edit_email);
        products = (EditText) findViewById(R.id.edit_products);
        services = (EditText) findViewById(R.id.edit_services);

        addUpdateButton = (Button) findViewById(R.id.button_add_update_company);
        companyData = new CompanyOperations(this);
        companyData.open();

        spinner = (Spinner) findViewById(R.id.class_spinner);
        spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.class_array1, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinnerValue = (String) spinner_adapter.getItem(0);

        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if (mode.equals("Update")) {
            addUpdateButton.setText("Actualizar empresa");
            empId = getIntent().getLongExtra(EXTRA_EMP_ID, 0);

            initializeCompany(empId);
        }



        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mode.equals("Add")) {
                    newCompany.setCompName(name.getText().toString());
                    newCompany.setCompWebPage(web.getText().toString());
                    newCompany.setCompNumber(Integer.parseInt(number.getText().toString()));
                    newCompany.setCompEmail(email.getText().toString());
                    newCompany.setCompProducts(products.getText().toString());
                    newCompany.setCompServices(services.getText().toString());
                    newCompany.setCompClassification(spinnerValue);
                    companyData.addCompany(newCompany);
                    Toast t = Toast.makeText(AddUpdateCompany.this, "Empresa " + newCompany.getCompName() + "ha sido agregada con exito!", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateCompany.this, MainActivity.class);
                    startActivity(i);
                } else {
                    oldCompany.setCompName(name.getText().toString());
                    oldCompany.setCompWebPage(web.getText().toString());
                    oldCompany.setCompNumber(Integer.parseInt(number.getText().toString()));
                    oldCompany.setCompEmail(email.getText().toString());
                    oldCompany.setCompProducts(products.getText().toString());
                    oldCompany.setCompServices(services.getText().toString());
                    oldCompany.setCompClassification(spinnerValue);
                    companyData.updateCompany(oldCompany);
                    Toast t = Toast.makeText(AddUpdateCompany.this, "Empresa " + oldCompany.getCompName() + " ha sido actualizada con exito!", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateCompany.this, MainActivity.class);
                    startActivity(i);

                }
            }
        });

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinnerValue = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initializeCompany(long compId) {
        oldCompany = companyData.getCompany(compId);
        name.setText(oldCompany.getCompName());
        web.setText(oldCompany.getCompWebPage());
        number.setText(String.valueOf(oldCompany.getCompNumber()));
        email.setText(oldCompany.getCompEmail());
        products.setText(oldCompany.getCompProducts());
        services.setText(oldCompany.getCompServices());
        spinner.setSelection(spinner_adapter.getPosition(oldCompany.getCompClassification()));
    }
}
