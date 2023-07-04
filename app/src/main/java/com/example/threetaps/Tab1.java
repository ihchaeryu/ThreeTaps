package com.example.threetaps;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
        ArrayList<String> permissionsList = new ArrayList<>();
        permissionsList.add(android.Manifest.permission.READ_CONTACTS);
        permissionsList.add(android.Manifest.permission.CALL_PHONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // LOLLIPOP 이상 버전의 장치인 경우, 최신 API 사용
            permissionsList.add(READ_MEDIA_IMAGES);

        } else {
            // LOLLIPOP 미만 버전의 장치인 경우, 대체 API 사용
            permissionsList.add(READ_EXTERNAL_STORAGE);
        }

        // below line is use to request
        // permission in the current activity.
        Dexter.withContext(mainActivity)
                // below line is use to request the number of
                // permissions which are required in our app.
                .withPermissions(permissionsList
                )
                // after adding permissions we are
                // calling and with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do your work now
                            getContacts();
                            Toast.makeText(mainActivity, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
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

    private void showSettingsDialog() {

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

}