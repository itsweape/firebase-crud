package com.example.pertemuan14;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText mNameEditText, mAddressEditText;
    EditText mUpdateNameEditText, mUpdateAddressEditText;

    //memasukkan data ke firebase
    DatabaseReference mDatabaseRererenceStudent, mDatabaseRererenceTeacher;
    Student mStudent;
    Teacher mTeacher;
    String key;

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mengambil data di class student
        mDatabaseRererenceStudent = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());
        mDatabaseRererenceTeacher = FirebaseDatabase.getInstance().getReference(Teacher.class.getSimpleName());

        mNameEditText = findViewById(R.id.name_edit);
        mAddressEditText = findViewById(R.id.address_edt);
        mUpdateNameEditText = findViewById(R.id.update_name_edit);
        mUpdateAddressEditText = findViewById(R.id.update_address_edt);

        spinner = findViewById(R.id.label_spinner);

        adapter = ArrayAdapter.createFromResource(this, R.array.labels_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });

        findViewById(R.id.read_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });
    }

    private void insertData(){
        String name = mNameEditText.getText().toString();
        String address = mAddressEditText.getText().toString();
        if(spinner.getSelectedItem().toString().equals("Student")){
            //create data student
            Student newStudent = new Student();
            if(name != "" && address != ""){
                newStudent.setName(name);
                newStudent.setAddress(address);

                mDatabaseRererenceStudent.push().setValue(newStudent);
                Toast.makeText(this, "Succesfully insert student data!", Toast.LENGTH_SHORT).show();
            }
        }else if(spinner.getSelectedItem().toString().equals("Teacher")){
            //create data teacher
            Teacher newTeacher = new Teacher();
            if (name != "" && address != ""){
                newTeacher.setName(name);
                newTeacher.setAddress(address);

                mDatabaseRererenceTeacher.push().setValue(newTeacher);
                Toast.makeText(this, "Succesfully insert teacher data!", Toast.LENGTH_SHORT).show();
            }
        }
        mNameEditText.setText("");
        mAddressEditText.setText("");

    }

    private void readData(){
        if (spinner.getSelectedItem().toString().equals("Student")) {
            //read data student
            mStudent = new Student();
            mDatabaseRererenceStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()){
                        for (DataSnapshot currentData : snapshot.getChildren()){
                            key = currentData.getKey();
                            mStudent.setName(currentData.child("name").getValue().toString());
                            mStudent.setAddress(currentData.child("address").getValue().toString());
                        }
                    }
                    mUpdateNameEditText.setText(mStudent.getName());
                    mUpdateAddressEditText.setText(mStudent.getAddress());
                    Toast.makeText(MainActivity.this, "Student data has been show!", Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (spinner.getSelectedItem().toString().equals("Teacher")){
            //read data teacher
            mTeacher = new Teacher();
            mDatabaseRererenceTeacher.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()){
                        for (DataSnapshot currentData : snapshot.getChildren()){
                            key = currentData.getKey();
                            mTeacher.setName(currentData.child("name").getValue().toString());
                            mTeacher.setAddress(currentData.child("address").getValue().toString());
                        }
                    }
                    mUpdateNameEditText.setText(mTeacher.getName());
                    mUpdateAddressEditText.setText(mTeacher.getAddress());
                    Toast.makeText(MainActivity.this, "Teacher data has been show!", Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void updateData(){
        if (spinner.getSelectedItem().toString().equals("Student")){
            Student updateDataStudent = new Student();
            updateDataStudent.setName(mUpdateNameEditText.getText().toString());
            updateDataStudent.setAddress(mUpdateAddressEditText.getText().toString());

            //update data student
            mDatabaseRererenceStudent.child(key).setValue(updateDataStudent);
            Toast.makeText(MainActivity.this, "Succesfully update student data!", Toast.LENGTH_SHORT).show();
        }else if (spinner.getSelectedItem().toString().equals("Teacher")){
            Teacher updateDataTeacher = new Teacher();
            updateDataTeacher.setName(mUpdateNameEditText.getText().toString());
            updateDataTeacher.setAddress(mUpdateAddressEditText.getText().toString());

            //update data teacher
            mDatabaseRererenceTeacher.child(key).setValue(updateDataTeacher);
            Toast.makeText(MainActivity.this, "Succesfully update teacher data!", Toast.LENGTH_SHORT).show();
        }
        mUpdateNameEditText.setText("");
        mUpdateAddressEditText.setText("");

    }

    private void deleteData(){
        if (spinner.getSelectedItem().equals("Student")){
            Student deleteDataStudent = new Student();
            deleteDataStudent.setName(mUpdateNameEditText.getText().toString());
            deleteDataStudent.setAddress(mUpdateAddressEditText.getText().toString());

            //delete data student
            mDatabaseRererenceStudent.child(key).removeValue();
            Toast.makeText(MainActivity.this, "Succesfully delete student data!", Toast.LENGTH_SHORT).show();
        }else if (spinner.getSelectedItem().equals("Teacher")){
            Teacher deleteDataTeacher = new Teacher();
            deleteDataTeacher.setName(mUpdateNameEditText.getText().toString());
            deleteDataTeacher.setAddress(mUpdateAddressEditText.getText().toString());

            //delete data teacher
            mDatabaseRererenceTeacher.child(key).removeValue();
            Toast.makeText(MainActivity.this, "Succesfully delete teacher data!", Toast.LENGTH_SHORT).show();
        }
        mUpdateNameEditText.setText("");
        mUpdateAddressEditText.setText("");
    }
}