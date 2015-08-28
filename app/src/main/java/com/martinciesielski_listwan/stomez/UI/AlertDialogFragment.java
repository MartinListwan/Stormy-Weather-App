package com.martinciesielski_listwan.stomez.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.martinciesielski_listwan.stomez.R;

/**
 * Created by marti_000 on 2015-08-15.
 */
public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        // SetTitle is the title of the alert
                // writing the  quotess and then alt enter will make a string
                //for you
                .setTitle(context.getString(R.string.Error_title))
                .setMessage(context.getString(R.string.Error_message))
                // Creates a button that dosent do anything
                .setPositiveButton(context.getString(R.string.Error_button_message), null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
