package com.example.sqliteapp.db;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sqliteapp.db.CompanyDBHandler;
import com.example.sqliteapp.model.Company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CompanyOperations {

    public static final String LOGTAG = "COMP_MNGMNT_SYS";

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    private static final String[] allColumns = {
            CompanyDBHandler.COLUMN_ID,
            CompanyDBHandler.COLUMN_NAME,
            CompanyDBHandler.COLUMN_WEB,
            CompanyDBHandler.COLUMN_NUMBER,
            CompanyDBHandler.COLUMN_EMAIL,
            CompanyDBHandler.COLUMN_PRODUCTS,
            CompanyDBHandler.COLUMN_SERVICES,
            CompanyDBHandler.COLUMN_CLASS
    };

    public CompanyOperations(Context context) {
        dbHandler = new CompanyDBHandler(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        db = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Close");
        dbHandler.close();
    }

    public Company addCompany(Company company) {
        ContentValues values = new ContentValues();
        values.put(CompanyDBHandler.COLUMN_NAME, company.getCompName());
        values.put(CompanyDBHandler.COLUMN_WEB, company.getCompWebPage());
        values.put(CompanyDBHandler.COLUMN_NUMBER, company.getCompNumber());
        values.put(CompanyDBHandler.COLUMN_EMAIL, company.getCompEmail());
        values.put(CompanyDBHandler.COLUMN_PRODUCTS, company.getCompProducts());
        values.put(CompanyDBHandler.COLUMN_SERVICES, company.getCompServices());
        values.put(CompanyDBHandler.COLUMN_CLASS, company.getCompClassification());

        long insertId = db.insert(CompanyDBHandler.TABLE_COMPANIES, null, values);
        company.setCompId(insertId);
        return company;
    }

    // Get single company
    public Company getCompany(long id) {

        Cursor cursor = db.query(CompanyDBHandler.TABLE_COMPANIES, allColumns, CompanyDBHandler.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Company company = new Company(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
        return company;
    }

    public List<Company> getAllCompanies() {

        Cursor cursor = db.query(CompanyDBHandler.TABLE_COMPANIES, allColumns, null, null, null, null, null);

        List<Company> companies = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Company company = new Company();
                company.setCompId(cursor.getLong(cursor.getColumnIndex(CompanyDBHandler.COLUMN_ID)));
                company.setCompName(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_NAME)));
                company.setCompWebPage(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_WEB)));
                company.setCompNumber(cursor.getInt(cursor.getColumnIndex(CompanyDBHandler.COLUMN_NUMBER)));
                company.setCompEmail(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_EMAIL)));
                company.setCompProducts(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_PRODUCTS)));
                company.setCompServices(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_SERVICES)));
                company.setCompClassification(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_CLASS)));
                companies.add(company);
            }
        }
        return companies;
    }

    // Updating company
    public int updateCompany(Company company) {

        ContentValues values = new ContentValues();
        values.put(CompanyDBHandler.COLUMN_NAME, company.getCompName());
        values.put(CompanyDBHandler.COLUMN_WEB, company.getCompWebPage());
        values.put(CompanyDBHandler.COLUMN_NUMBER, company.getCompNumber());
        values.put(CompanyDBHandler.COLUMN_EMAIL, company.getCompEmail());
        values.put(CompanyDBHandler.COLUMN_PRODUCTS, company.getCompProducts());
        values.put(CompanyDBHandler.COLUMN_SERVICES, company.getCompServices());
        values.put(CompanyDBHandler.COLUMN_CLASS, company.getCompClassification());

        // updating row
        return db.update(CompanyDBHandler.TABLE_COMPANIES, values, CompanyDBHandler.COLUMN_ID + "=?", new String[]{String.valueOf(company.getCompId())});
    }

    // deleting company
    public void removeCompany(Company company) {
        db.delete(CompanyDBHandler.TABLE_COMPANIES,CompanyDBHandler.COLUMN_ID + "=" + company.getCompId(), null);
    }

    public List<Company> getCompaniesLike(String s, String spinnerValue) {

        if (s.equalsIgnoreCase("") && (spinnerValue.equalsIgnoreCase("Todos") || spinnerValue.equalsIgnoreCase("")))
            return this.getAllCompanies();
        Cursor cursor;

        if (s.equalsIgnoreCase(""))
            cursor = db.query(CompanyDBHandler.TABLE_COMPANIES, allColumns, CompanyDBHandler.COLUMN_CLASS + "=?",  new String[]{spinnerValue}, null, null, null);
        else if (spinnerValue.equalsIgnoreCase("Todos") || spinnerValue.equalsIgnoreCase(""))
            cursor = db.query(CompanyDBHandler.TABLE_COMPANIES, allColumns, CompanyDBHandler.COLUMN_NAME + " LIKE ?",  new String[]{"%" + s + "%"}, null, null, null);
        else
            cursor = db.query(CompanyDBHandler.TABLE_COMPANIES, allColumns, CompanyDBHandler.COLUMN_NAME + " LIKE ? AND " + CompanyDBHandler.COLUMN_CLASS + "=?",  new String[]{"%" + s + "%", spinnerValue}, null, null, null);

        List<Company> companies = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Company company = new Company();
                company.setCompId(cursor.getLong(cursor.getColumnIndex(CompanyDBHandler.COLUMN_ID)));
                company.setCompName(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_NAME)));
                company.setCompWebPage(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_WEB)));
                company.setCompNumber(cursor.getInt(cursor.getColumnIndex(CompanyDBHandler.COLUMN_NUMBER)));
                company.setCompEmail(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_EMAIL)));
                company.setCompProducts(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_PRODUCTS)));
                company.setCompServices(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_SERVICES)));
                company.setCompClassification(cursor.getString(cursor.getColumnIndex(CompanyDBHandler.COLUMN_CLASS)));
                companies.add(company);
            }
        }
        return companies;

    }
}
