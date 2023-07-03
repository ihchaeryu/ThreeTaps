package com.example.threetaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class Tab1 extends Fragment {

    // to use the context here
    private MainActivity mainActivity;
    // creating variables for our array list, recycler view progress bar and adapter.
    private ArrayList<ContactsModal> contactsModalArrayList;
    private RecyclerView contactRV;
    private ContactRVAdapter contactRVAdapter;
    private ProgressBar loadingPB;
    // activity result launcher to solve deprecation
    private ActivityResultLauncher<Intent> launcher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // assign context here for later use
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // activity result launcher to solve deprecation
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle the result here
                }
            });

        return inflater.inflate(R.layout.fragment_tab1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // on below line we are initializing our variables.
        contactsModalArrayList = new ArrayList<>();
        contactRV = view.findViewById(R.id.RVContacts);
        loadingPB = view.findViewById(R.id.PBLoading);

        // calling method to prepare our recycler view.
        prepareContactRV();

        // calling a method to request permissions.
        requestPermissions();

    }

    private void prepareContactRV() {
        // in this method we are preparing our recycler view with adapter.
        contactRVAdapter = new ContactRVAdapter(mainActivity, contactsModalArrayList);
        // on below line we are setting layout manager.
        contactRV.setLayoutManager(new LinearLayoutManager(mainActivity));
        // on below line we are setting adapter to our recycler view.
        contactRV.setAdapter(contactRVAdapter);
    }

    private void requestPermissions() {
        // permissions to ask based on sdk version
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(android.Manifest.permission.READ_CONTACTS);
        permissions.add(android.Manifest.permission.CALL_PHONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        // below line is use to request
        // permission in the current activity.
        Dexter.withContext(mainActivity.getApplicationContext())
                // below line is use to request the number of
                // permissions which are required in our app.
                .withPermissions(permissions)
//                .withPermissions(android.Manifest.permission.READ_CONTACTS,
//                        android.Manifest.permission.CALL_PHONE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_MEDIA_AUDIO)
                // after adding permissions we are
                // calling and with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do your work now
                            getContacts();
                            Toast.makeText(mainActivity, "All the permissions are granted.", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently,
                            // we will show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some
                        // permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
                    // this method is use to handle error
                    // in runtime permissions
                    @Override
                    public void onError(DexterError error) {
                        // we are displaying a toast message for error message.
                        Toast.makeText(mainActivity, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                // below line is use to run the permissions
                // on same thread and to check the permissions
                .onSameThread().check();
    }

    @SuppressLint("Range")
    private void getContacts() {
        // this method is use to read contact from users device.
        // on below line we are creating a string variables for
        // our contact id and display name.
        String contactId = "";
        String displayName = "";
        // on below line we are calling our content resolver for getting contacts
        Cursor cursor = mainActivity.getContentResolver()
                .query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        // on below line we are checking the count for our cursor.
        if (cursor.getCount() > 0) {
            // if the count is greater than 0 then we are running a loop to move our cursor to next.
            while (cursor.moveToNext()) {
                // on below line we are getting the phone number.
                // An indicator of whether this contact has at least one phone number.
                // "1" if there is at least one phone number, "0" otherwise.
                int hasPhoneNumber = Integer.parseInt(cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    // we are checking if the has phone number is > 0
                    // on below line we are getting our contact id and user name for that contact
                    contactId = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // on below line we are calling a content resolver and making a query
                    Cursor phoneCursor = mainActivity.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);
                    // on below line we are moving our cursor to next position.
                    if (phoneCursor.moveToNext()) {
                        // on below line we are getting the phone number for our users
                        // and then adding the name along with phone number in array list.
                        String phoneNumber = phoneCursor.getString(phoneCursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactsModalArrayList.add(new ContactsModal(displayName, phoneNumber));
                    }
                    // on below line we are closing our phone cursor.
                    phoneCursor.close();
                }
            }
        }
        // on below line we are closing our cursor.
        cursor.close();
        // on below line we are hiding our progress bar and notifying our adapter class.
        loadingPB.setVisibility(View.GONE);
        contactRVAdapter.notifyDataSetChanged();
    }

    private void showSettingsDialog() {
        // dialog message for when a permission is permanently denied

        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called on click on positive
                // button and on clicking shit button we
                // are redirecting our user from our app to the
                // settings page of our app.
                dialog.cancel();
                // below is the intent from which we
                // are redirecting our user.
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mainActivity.getPackageName(), null);
                intent.setData(uri);
//                startActivityForResult(intent, 101);
                launcher.launch(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called when
                // user click on negative button.
                dialog.cancel();
            }
        });
        // below line is used
        // to display our dialog
        builder.show();
    }

}