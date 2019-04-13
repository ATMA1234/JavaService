package com.example.javaservice.Values;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class FunctionCall {
    public void showtoast(Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    public void showprogressdialog(String Message, ProgressDialog dialog, String Title) {
        dialog.setTitle(Title);
        dialog.setMessage(Message);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();
    }
}
