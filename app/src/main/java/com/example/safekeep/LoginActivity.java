package com.example.safekeep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,PwdEditText;
    Button LoginBtn;
    ProgressBar progressBar;
    TextView CreateAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText=findViewById(R.id.email_edit_text);
        PwdEditText=findViewById(R.id.password_edit_text);
        LoginBtn=findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.progress_bar);
        CreateAccountBtnTextView=findViewById(R.id.create_account_text_view_btn);

        LoginBtn.setOnClickListener(v-> loginUser());
        CreateAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
    }
    void loginUser(){
        String email=emailEditText.getText().toString();
        String password=PwdEditText.getText().toString();

        boolean isValidated=validateData(email,password);
        if(!isValidated){
            return;
        }
        loginAccountInFirebase(email,password);
    }
    void loginAccountInFirebase(String email,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else{
                        Utility.showToast(LoginActivity.this, "CREATED ACCOUNT SUCCESSFULLY.CHECK EMAIL FOR VERIFICATION");
                    }
                }else {
                    Utility.showToast(LoginActivity.this, "SOMETHING WENT WRONG...TRY AGAIN");
                }
            }
        });
    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            LoginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            LoginBtn.setVisibility(View.VISIBLE);
        }
    }



    boolean validateData(String email,String password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("INVALID EMAIL");
            return false;
        }
        if(password.length()<6){
            PwdEditText.setError(" PASSWORD TOO SHORT");
            return false;
        }
        return true;
    }


    }
