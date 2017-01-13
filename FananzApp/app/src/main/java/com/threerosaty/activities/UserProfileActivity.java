package com.threerosaty.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.threerosaty.R;

public class UserProfileActivity extends BaseActivity {

    private TextView txtName, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle(getString(R.string.my_profile));
        txtName = (TextView) findViewById(R.id.edt_name);
        txtEmail = (TextView) findViewById(R.id.edt_email);

        txtName.setText(mSessionManager.getUserFName() + " " + mSessionManager.getUserLName());
        txtEmail.setText(mSessionManager.getUserEmail());
    }
}
