package com.threerosaty.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.threerosaty.utils.DialogUtils;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.utils.SessionManager;

/**
 * Created by akshay on 02-01-2017.
 */
public class BaseFragment extends Fragment {

    protected ServerSyncManager mServerSyncManager = null;
    protected static SessionManager mSessionManager = null;
    protected ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = SessionManager.getInstance(getActivity().getApplicationContext());
        mServerSyncManager = new ServerSyncManager(getActivity().getApplicationContext(), mSessionManager);
        progressDialog = DialogUtils.getProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    protected void customAlterDialog(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("" + title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
