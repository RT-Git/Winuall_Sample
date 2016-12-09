package com.wordpress.obliviouscode.winualltest;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    public ProgressDialog mProgressDialog;
    FancyButton f;
    FragmentManager fm ;
    int p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        f = (FancyButton) (findViewById(R.id.sup));
        f.setOnClickListener(this);
        addSignInFrag();
    }

    public void addSignInFrag(){
        p=1;
        fm=getSupportFragmentManager();
        f.setText("New user? Sign Up");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerFragment, new SignInFragment());
        ft.commit();
    }

    public void addSignUpFrag(){
        p=-1;
        fm=getSupportFragmentManager();
        f.setText("Existing user Sign In");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerFragment, new SignUpFragment());
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        if(p==1)
            addSignUpFrag();
        else if(p==-1)
            addSignInFrag();
    }




    public void showProgressDialog(String s) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(s);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.setMessage(s);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
