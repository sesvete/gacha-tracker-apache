package com.sesvete.gachatrackerapache.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.sesvete.gachatrackerapache.R;

public class DialogHelper {

    public static void buildAlertDialogWindow(AlertDialog dialog, Context context, Activity activity){
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            // prevents white background of the drawable

            window.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_transparent_background));
            // če rabim custom size
            /*
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            lp.width = (int) (displayMetrics.widthPixels * 0.8);
            window.setAttributes(lp);

             */
        }
    }


    public static void buildAlertDialogWindowWithKeyboard(AlertDialog dialog, Context context, EditText editText, Activity activity){
        // da se keyboard odpre šele ko se dialog popolnoma zgradi

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                openKeyboard(editText, context);
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            // prevents white background of the drawable
            window.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_transparent_background));
            /*
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            lp.width = (int) (displayMetrics.widthPixels * 0.8);
            window.setAttributes(lp);

             */
        }
    }

    // da se keyboard odpre šele ko se dialog popolnoma zgradi
    public static void openKeyboard(EditText editText, Context context){
        if (editText != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    editText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }, 70);
        }
    }

    // https://stackoverflow.com/questions/1109022/how-can-i-close-hide-the-android-soft-keyboard-programmatically
    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static void keyboardTouchListener(View view, Activity activity){
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(activity); // Call your hideKeyboard function
                    return false;
                }
            });
        }

    }

}
