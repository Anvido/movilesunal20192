package com.example.sqliteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqliteapp.db.CompanyOperations;

public class MainActivity extends AppCompatActivity {

    private Button addCompany;
    private Button editCompany;
    private Button deleteCompany;
    private Button viewAllcompanies;
    private CompanyOperations companyOps;
    private static final String EXTRA_EMP_ID = "com.sqliteapp.compId";
    private static final String EXTRA_ADD_UPDATE = "com.sqliteapp.add_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCompany = (Button) findViewById(R.id.add_company);
        editCompany = (Button) findViewById(R.id.edit_company);
        deleteCompany = (Button) findViewById(R.id.delete_company);
        viewAllcompanies = (Button)findViewById(R.id.view_companies);

        addCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddUpdateCompany.class);
                i.putExtra(EXTRA_ADD_UPDATE, "Add");
                startActivity(i);
            }
        });

        editCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmpIdAndUpdateEmp();
            }
        });
        deleteCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmpIdAndRemoveEmp();
            }
        });
        viewAllcompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewAllCompanies.class);
                startActivity(i);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.employee_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.menu_item_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void getEmpIdAndUpdateEmp(){

        LayoutInflater li = LayoutInflater.from(this);
        View getCompIdView = li.inflate(R.layout.dialog_get_comp_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getCompIdView);

        final EditText userInput = (EditText) getCompIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        Intent i = new Intent(MainActivity.this,AddUpdateCompany.class);
                        i.putExtra(EXTRA_ADD_UPDATE, "Update");
                        i.putExtra(EXTRA_EMP_ID, Long.parseLong(userInput.getText().toString()));
                        startActivity(i);
                    }
                }).create()
                .show();

    }


    public void getEmpIdAndRemoveEmp(){

        LayoutInflater li = LayoutInflater.from(this);
        View getCompIdView = li.inflate(R.layout.dialog_get_comp_id, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set dialog_get_emp_id.xml to alertdialog builder
        alertDialogBuilder.setView(getCompIdView);

        final EditText userInput = (EditText) getCompIdView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // get user input and set it to result
                        // edit text
                        companyOps = new CompanyOperations(MainActivity.this);
                        companyOps.open();
                        System.out.println("Remover ......... " +Long.parseLong(userInput.getText().toString()));
                        companyOps.removeCompany(companyOps.getCompany(Long.parseLong(userInput.getText().toString())));
                        Toast t = Toast.makeText(MainActivity.this,"Empresa eliminada con exito!",Toast.LENGTH_SHORT);
                        t.show();
                    }
                }).create()
                .show();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        companyOps.open();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        companyOps.close();
//
//    }


}
