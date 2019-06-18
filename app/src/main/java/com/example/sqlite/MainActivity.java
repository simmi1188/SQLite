package com.example.sqlite;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private Menu menu;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    ArrayList<student> allStudents;
    private MyAdapter myAdapter;
    private DBHelper dbHelper;
    private boolean nameAsec, markAsec;
    private CardView headerView;
    private TextView NoRecords;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private boolean filter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headerView = (CardView) findViewById(R.id.header);
        NoRecords = (TextView) findViewById(R.id.lblNoRecords);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        initialize();

    }

    public void initialize() {
        filter = true;
        nameAsec = true;
        markAsec = false;

        dbHelper = DBHelper.getDBHelper(this);

    }

    private void setList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this, this);
        myAdapter.getStudentList().addAll(allStudents);
        recyclerView.setAdapter(myAdapter);
    }

    public void updateList() {
        myAdapter.getStudentList().clear();
        myAdapter.getStudentList().addAll(allStudents);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        allStudents = dbHelper.getAllStudent();
        System.out.println("allStudents : " + allStudents.size());
        for (student record : allStudents) {
            //System.out.println("student in onResume : " + record.toString());
        }
        if (myAdapter == null)
            setList();
        else
            updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);

        final android.support.v7.widget.SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.background_light));
        searchAutoComplete.setTextColor(getResources().getColor(android.R.color.white));
        searchAutoComplete.setHint("Search");
        searchView.setOnQueryTextListener(this);
        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.close);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                showHideMenuIcons(menu, R.id.action_search, false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                showHideMenuIcons(menu, R.id.action_search, false);
                recyclerView.setVisibility(View.VISIBLE);
                headerView.setVisibility(View.VISIBLE);
                NoRecords.setVisibility(View.GONE);
                return true;
            }
        });
        return true;
    }

    private void showHideMenuIcons(Menu menu, int visibleItemId, boolean isVisible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem != null && menuItem.getItemId() != visibleItemId) {
                menuItem.setVisible(isVisible);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.add) {

            Intent intent = new Intent(this, StudentDetailsActivity.class);
            intent.putExtra("Create", true);
            startActivity(intent);
        }

        switch (id) {
            case R.id.sortOnName:
                sortStudent(true, nameAsec);
                nameAsec = !nameAsec;
                item.setIcon(nameAsec ? R.drawable.nameasec : R.drawable.namedsec);
                updateList();
                break;


            case R.id.sortOnMarks:
                sortStudent(false, markAsec);
                updateList();
                markAsec = !markAsec;
                item.setIcon(markAsec ? R.drawable.markasec : R.drawable.markdsec);
                break;

            case R.id.filter:
                if (filter) {
                    alertDialogFilter();

                    filter = !filter;
                    item.setIcon(filter ? R.drawable.filter : R.drawable.cross);
                    showHideMenuIcons(menu, R.id.filter, false);
                } else if (!filter) {
                    updateList();
                    filter = !filter;
                    item.setIcon(filter ? R.drawable.filter : R.drawable.cross);
                    showHideMenuIcons(menu, R.id.filter, true);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortStudent(final boolean sortOnName, final boolean Ascending) {
        Collections.sort(allStudents, new Comparator<student>() {
            @Override
            public int compare(student o1, student o2) {
                int result = 0;
                if (sortOnName) {
                    if (Ascending)
                        result = o1.getName().compareToIgnoreCase(o2.getName());
                    else result = o2.getName().compareToIgnoreCase(o1.getName());
                } else {
                    int marks1 = Ascending ? Integer.parseInt(o1.getMarks()) : Integer.parseInt(o2.getMarks());
                    int marks2 = Ascending ? Integer.parseInt(o2.getMarks()) : Integer.parseInt(o1.getMarks());
                    if (marks1 > marks2)
                        result = 1;
                    else if (marks1 < marks2)
                        result = -1;
                }
                return result;
            }
        });

    }

    private void alertDialogFilter() {
        final View buttonEntryView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        radioButton1 = (RadioButton) buttonEntryView.findViewById(R.id.Button1);
        radioButton2 = (RadioButton) buttonEntryView.findViewById(R.id.Button2);
        radioButton3 = (RadioButton) buttonEntryView.findViewById(R.id.Button3);

        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(buttonEntryView)
                .setTitle("Students Result")
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                radioButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateList();
                        dialog.dismiss();
                    }
                });

                radioButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAdapter.getStudentList().clear();
                        for (int i = 0; i < allStudents.size(); i++) {
                            if (Integer.parseInt(allStudents.get(i).getMarks()) >= 40) {
                                myAdapter.getStudentList().add(allStudents.get(i));
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                radioButton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAdapter.getStudentList().clear();
                        for (int i = 0; i < allStudents.size(); i++) {
                            if (Integer.parseInt(allStudents.get(i).getMarks()) < 40) {
                                myAdapter.getStudentList().add(allStudents.get(i));
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        myAdapter.getStudentList().clear();


        for (student child : allStudents) {
            if (child.getName().toLowerCase().contains(userInput)) {

                myAdapter.getStudentList().add(child);
            }
        }
        if (myAdapter.getStudentList().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            headerView.setVisibility(View.GONE);
            NoRecords.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            headerView.setVisibility(View.VISIBLE);
            NoRecords.setVisibility(View.GONE);
        }
        myAdapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public void onClick(View v) {
        final student student = (com.example.sqlite.student) v.getTag();
        int id = v.getId();
        switch (id) {
            case R.id.edit:
                Intent intent = new Intent(this, StudentDetailsActivity.class);
                intent.putExtra("Create", false);
                intent.putExtra("ID", student.getID());
                startActivity(intent);
                break;
            case R.id.delete:
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                        .setMessage(R.string.delete)
                        .setPositiveButton(R.string.btn_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dbHelper.deleteUser(student);
                                allStudents.remove(student);
                                myAdapter.getStudentList().remove(student);
                                myAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, R.string.deleteToast, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.btn_Cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
                break;
        }
    }
}
