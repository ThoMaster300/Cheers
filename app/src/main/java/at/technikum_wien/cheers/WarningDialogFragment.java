package at.technikum_wien.cheers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * Created by Alexander on 03.01.2018.
 */

public class WarningDialogFragment extends DialogFragment{




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.warningDialogMessage)
                .setTitle(R.string.warningDialogTitle)
                .setPositiveButton(R.string.warningDialogPositiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Wenn man OK klickt, muss man keine Aktion starten
                    }
                })
                .setNegativeButton(R.string.warningDialogNegativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Die app schließt sich
                        getActivity().finish();

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    //Wenn die Partypeople außerhalb des Dialogs drücken, wird die App auch geschlossen
    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        getActivity().finish();
    }
}
