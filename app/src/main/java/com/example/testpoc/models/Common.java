package com.example.testpoc.models;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class Common {
    private static Common instance;

    private Common() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * Singleton for Common
     *
     * @return Instance of {@link Common}
     */
    public static Common getInstance() {
        if (instance == null) {
            instance = new Common();
        }
        return instance;
    }

    /**
     * Method to display toast message to the user
     *
     * @param applicationContext {@link android.app.Application} Used to pass to {@link Toast}
     * @param message            {@link String} Desired message which is to be displayed
     */
    public void showMessage(Context applicationContext, String message) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to display a snackbar message to the user
     *
     * @param view       {@link View} Used to pass to {@link Snackbar}
     * @param message    {@link String} Desired message which is to be displayed
     * @param isFragment {@link Boolean} This represents if the message is to be displayed in a fragment or not (need to control margin values for a better UI)
     */
    public void showSnackMessage(View view, String message, Boolean isFragment) {
        try {
            Snackbar snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(Color.BLACK)
                    .setActionTextColor(Color.WHITE);

            View snackBarView = snackBar.getView();
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) snackBarView.getLayoutParams();
            if (isFragment) {
                layoutParams.bottomMargin = 210;
            } else {
                layoutParams.bottomMargin = 50;
            }
            snackBarView.setLayoutParams(layoutParams);

            snackBar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });

            snackBar.show();
        } catch (Exception e) {
            Logger.e("Agnesh | Common -> SnackBar issue: " + e.getMessage());
        }
    }

}
