package goyal.world.quiztime;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileActivity extends AppCompatActivity {

    private EditText name, email, phone;
    private LinearLayout editB;
    private Button cancelB, saveB;
    private TextView profileText;
    private LinearLayout button_layout;
    private String nameString, phoneString;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile"); // for selected category
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for back btn

        name = findViewById(R.id.mp_name);
        phone = findViewById(R.id.mp_phone);
        email = findViewById(R.id.mp_email);
        profileText = findViewById(R.id.profile_text);
        editB = findViewById(R.id.editB);
        cancelB = findViewById(R.id.cancelB);
        saveB = findViewById(R.id.saveB);
        button_layout = findViewById(R.id.button_layout);


        progressDialog = new Dialog(MyProfileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false); // so that user can not cancel this dialog
        //set the layout for progress window
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Updating Data...");


        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enableEditing();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disableEditing();
            }
        });

        saveB.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    saveData();
                }
            }
        });

    }

    private void disableEditing()
    {
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);

        button_layout.setVisibility(View.GONE);

        name.setText(DbQuery.myProfile.getName());
        email.setText(DbQuery.myProfile.getEmail());

        if(DbQuery.myProfile.getPhone() != null)
            phone.setText(DbQuery.myProfile.getPhone());

        String profileName = DbQuery.myProfile.getName();

        profileText.setText(profileName.toUpperCase().substring(0,1));
    }

    private void enableEditing()
    {
        name.setEnabled(true);
       // email.setEnabled(true);
        phone.setEnabled(true);

        button_layout.setVisibility(View.VISIBLE);
    }

    private boolean validate()
    {
        nameString = name.getText().toString();
        phoneString = phone.getText().toString();

        if(nameString.isEmpty())
        {
            name.setError("Name can not be empty!");
            return false;
        }

        if(! phoneString.isEmpty())
        {
            if(! ( (phoneString.length() == 10) && (TextUtils.isDigitsOnly(phoneString))) )
            {
                phone.setError("Enter Valid Phone Number.");
                return false;
            }
        }

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveData()
    {
        progressDialog.show();

        if(phoneString.isEmpty())
            phoneString = null;

        DbQuery.saveProfileData(nameString, phoneString, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this,"Profile Updated Successfully.", Toast.LENGTH_SHORT).show();

                disableEditing();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {

                Toast.makeText(MyProfileActivity.this,"Something Went Wrong! Please try again later.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) // back button
        {
            MyProfileActivity.this.finish();// kill this activity
        }
        return super.onOptionsItemSelected(item);
    }
}