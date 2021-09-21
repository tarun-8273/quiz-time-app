package goyal.world.quiztime;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, password, confirmPassword;
    private Button signUpBtn;
    private ImageView backBtn;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, confirmPassStr, nameStr;
    private Dialog progressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        signUpBtn = findViewById(R.id.signUp_btn);
        backBtn = findViewById(R.id.back_btn);

        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false); // so that user can not cancel this dialog
        //set the layout for progress window
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Regitering User...");


        mAuth = FirebaseAuth.getInstance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish(); // backBtn: back to the previous activity(this activity is finish...)
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    signupNewUser();
                }
            }
        });

    }

    private boolean validate()
    {
        nameStr = name.getText().toString().trim(); //trim use for delete the space from string(i.e. Start and End Space Only)
        passStr = password.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmPassword.getText().toString().trim();

        if (nameStr.isEmpty())
        {
            name.setError("Enter Your Name");
            return false;
        }

        if (emailStr.isEmpty())
        {
            email.setError("Enter Your E-Mail ID");
            return false;
        }

        if (passStr.isEmpty())
        {
            password.setError("Enter Password");
            return false;
        }

        if (confirmPassStr.isEmpty())
        {
            confirmPassword.setError("Enter Confirm Password");
            return false;
        }

        if (passStr.compareTo(confirmPassStr) != 0) // 0 means for same
        {
            Toast.makeText(SignUpActivity.this, "Password and Confirm Password should be same !", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }


    private void signupNewUser()
    {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SignUpActivity.this, "Sign Up Successfully...", Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(emailStr, nameStr, new MyCompleteListener(){

                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this, "Something went wrong ! please Try Again Later !",
                                                    Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {

                                    Toast.makeText(SignUpActivity.this, "Something went wrong ! please Try Again Later !",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
