package com.example.safekeep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText,PwdEditText,ConfirmPwdEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText=findViewById(R.id.email_edit_text);
        PwdEditText=findViewById(R.id.password_edit_text);
        ConfirmPwdEditText=findViewById(R.id.confirm_password_edit_text);
        createAccountBtn=findViewById(R.id.create_account_btn);
        progressBar=findViewById(R.id.progress_bar);
        loginBtnTextView=findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v->finish());
    }

    private void createAccount() {
        String email=emailEditText.getText().toString();
        String password=PwdEditText.getText().toString();
        String confirmPassword=ConfirmPwdEditText.getText().toString();

        boolean isValidated=validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }
        createAccountInFirebase(email,password);

        }
        void createAccountInFirebase(String email,String password){
           changeInProgress(true);
            FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    changeInProgress(false);
                    if(task.isSuccessful()){
                        Utility.showToast(CreateAccountActivity.this, "CREATED ACCOUNT SUCCESSFULLY.CHECK EMAIL FOR VERIFICATION");

                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        firebaseAuth.signOut();
                        finish();
                    }else{
                        Utility.showToast(CreateAccountActivity.this, " ACCOUNT FAILED.TRY AGAIN");


                    }
                }
            });

        }

        void changeInProgress(boolean inProgress){
            if(inProgress){
                progressBar.setVisibility(View.VISIBLE);
                createAccountBtn.setVisibility(View.GONE);
            }
            else{
                progressBar.setVisibility(View.GONE);
                createAccountBtn.setVisibility(View.VISIBLE);
            }
        }



    boolean validateData(String email,String password,String confirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("INVALID EMAIL");
            return false;
        }
        if(password.length()<6){
            PwdEditText.setError(" PASSWORD TOO SHORT");
            return false;
        }
        if(!password.equals(confirmPassword)){
            ConfirmPwdEditText.setError("PASSWORDS DONT MATCH");
            return false;
        }
        return true;
    }

}