package com.wordpress.obliviouscode.winualltest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener{

    EditText username, email, password;
    Button btnRegister;
    MainActivity loginActivity;
    Context context;

    private FirebaseAuth mAuth;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity=(MainActivity) getActivity();
        context=getContext();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        username=(EditText)view.findViewById(R.id.etUsername);
        email=(EditText)view.findViewById(R.id.etEmail);
        password=(EditText)view.findViewById(R.id.etPassword);
        btnRegister=(Button)view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                if(validateForm())
                    createUser();
                break;
        }
    }

    private boolean validateForm() {
        final String emailStr = email.getText().toString().trim();
        String usernameStr=username.getText().toString().trim();
        String passwd=password.getText().toString().trim();
        boolean b=true;

        if (TextUtils.isEmpty(emailStr)) {
            email.setError("Enter Email address");
            b=false;
        }
        else if(!emailStr.contains("@")){
            email.setError("Enter valid Email address");
            b=false;
        }
        if(passwd.length()<8){
            password.setError("Weak Password");
        }
        if (TextUtils.isEmpty(usernameStr)) {
            username.setError("Enter Username");
            b=false;
        }
        return b;
    }


    private void createUser() {
        String emailStr = email.getText().toString().trim();
        String passwd=password.getText().toString().trim();
        final String usernameStr=username.getText().toString().trim();
        loginActivity.showProgressDialog("Registering User");

        //UTILISES EMAIL AND PASSEWORD TO CREATE A USER
        mAuth.createUserWithEmailAndPassword(emailStr, passwd)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //mAuth.signOut();
                        if (!task.isSuccessful()) {
                            Toast.makeText(context,task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            loginActivity.hideProgressDialog();
                        }
                        else if(task.isSuccessful()){

                            final FirebaseUser user = task.getResult().getUser();

                            //ADDS THE USER'S NAME
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usernameStr)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                sendVerificationMail(user);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void sendVerificationMail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    loginActivity.hideProgressDialog();
                    Toast.makeText(context,task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                else if(task.isSuccessful()){
                    loginActivity.hideProgressDialog();
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Confirm Email");
                    alertDialog.setMessage("Please check your mailbox for confirmation email");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    alertDialog.show();
                    loginActivity.addSignInFrag();
                }
            }
        });
    }

}
