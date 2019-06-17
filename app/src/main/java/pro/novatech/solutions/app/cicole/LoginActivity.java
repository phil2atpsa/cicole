package pro.novatech.solutions.app.cicole;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pro.novatech.solutions.app.cicole.helper.MyPreferenceManager;
import pro.novatech.solutions.cicole.app.R;
import pro.novatech.solutions.kelasiapi.v1.KelasiApiException;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccess;
import pro.novatech.solutions.kelasiapi.v1.Login.UserAccessResponse;
import pro.novatech.solutions.kelasiapi.v1.OnServiceResponseListener;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {



    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private Button btnSignUp;
    private EditText inputEmail;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;
    private EditText inputPassword;

    private Toolbar toolbar;
    private MyPreferenceManager myPreferenceManager;




    @Override
    protected  void onResume(){
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPreferenceManager = new MyPreferenceManager(this);

        if(myPreferenceManager.isUserSession()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);


        // Set up the login form.
        toolbar = ((Toolbar)findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputLayoutEmail = ((TextInputLayout)findViewById(R.id.input_layout_email));
        inputLayoutPassword = ((TextInputLayout)findViewById(R.id.input_layout_password));
        inputEmail = ((EditText)findViewById(R.id.input_email));
        inputPassword = ((EditText)findViewById(R.id.input_password));
        btnSignUp = ((Button)findViewById(R.id.btn_signin));
        inputEmail.addTextChangedListener(new MyTextWatcher(this.inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(this.inputPassword));







        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validateEmail();
                if(inputEmail.getError() == null) {
                    validatePassword();
                    if(inputPassword.getError() == null){
                        attemptLogin();
                    }
                }
            }
        });


    }


    private class  MyTextWatcher implements TextWatcher {

        private TextView textView;

        private MyTextWatcher(TextView textView) {
            this.textView = textView;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch(textView.getId()){
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void validateEmail(){
        String email = inputEmail.getText().toString().trim();
        if(email.isEmpty()){
            inputEmail.setError(getString(R.string.err_msg_email));
            inputEmail.requestFocus();
        }

    }

    private void validatePassword(){
        String password = inputPassword.getText().toString().trim();
        if(password.isEmpty() || !LoginActivity.isPasswordValid(password)){
            inputPassword.setError(getString(R.string.err_msg_password));
            inputPassword.requestFocus();
        }
    }




    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        new UserAccess(this, new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                UserAccessResponse response  = (UserAccessResponse) object;
                authorise(response.getAccess_token());
                //Testing  purpose

            }

            @Override
            public void onFailure(KelasiApiException e) {
                //for  testing purpose Force auth.


                if(e.getMessage().contains("username") || e.getMessage().contains("Username")) {
                    inputEmail.setError(e.getMessage());
                    inputEmail.requestFocus();
                } else if(e.getMessage().contains("password") || e.getMessage().contains("Password")) {
                    inputPassword.setError(e.getMessage());
                    inputPassword.requestFocus();
                } else{
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, true).Login(inputEmail.getText().toString(), inputPassword.getText().toString());


    }
    private void authorise(String access_token){
        new UserAccess(this, new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                UserAccessResponse response  = (UserAccessResponse) object;
                //authorise(response.getAuth_key());
                if( "Guardian".equals(response.getAssignment())  ||  "Teacher".equals(response.getAssignment()) ||  "Student".equals(response.getAssignment())) {
                    myPreferenceManager.setUserSession(response.ToJSONObjectString());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.err_msg_access), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(KelasiApiException e) {
                inputEmail.setError(e.getMessage());
                inputEmail.requestFocus();
            }
        }, true).Authorize(access_token);

    }

    private static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (!TextUtils.isEmpty(email)) && (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



}

