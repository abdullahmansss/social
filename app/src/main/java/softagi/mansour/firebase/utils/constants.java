package softagi.mansour.firebase.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import softagi.mansour.firebase.R;
import softagi.mansour.firebase.models.userModel;

public class constants
{
    public static userModel myChats;

    // progress dialog
    private static ProgressDialog progressDialog;

    // firebase auth
    private static FirebaseAuth auth;

    // firebase database
    private static FirebaseDatabase firebaseDatabase;

    // database reference
    private static DatabaseReference databaseReference;

    // firebase storage
    private static FirebaseStorage firebaseStorage;

    // storage reference
    private static StorageReference storageReference;

    // shared preferences
    private static SharedPreferences sharedPreferences;

    public static void replaceFragment(Fragment from, Fragment to, boolean save)
    {
        if (save)
        {
            from
                    .requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, to)
                    .addToBackStack(null)
                    .commit();
        } else
            {
                from
                        .requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack();

                from
                        .requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, to)
                        .disallowAddToBackStack()
                        .commit();
            }
    }

    public static void showToast(Context context, String body)
    {
        Toast.makeText(context, body, Toast.LENGTH_SHORT).show();
    }

    public static void initProgress(Context context, String body)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(body);
        progressDialog.setCancelable(false);
    }

    public static void showProgress()
    {
        progressDialog.show();
    }

    public static void dismissProgress()
    {
        progressDialog.dismiss();
    }

    public static void initFirebase()
    {
        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public static FirebaseAuth getAuth()
    {
        return auth;
    }

    public static DatabaseReference getDatabaseReference()
    {
        return databaseReference;
    }

    public static StorageReference getStorageReference()
    {
        return storageReference;
    }

    public static void initPref(Activity activity)
    {
        sharedPreferences = activity.getSharedPreferences("SOCIAL", Context.MODE_PRIVATE);
    }

    public static void saveUid(Activity activity,String id)
    {
        sharedPreferences = activity.getSharedPreferences("SOCIAL", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Uid", id);
        editor.apply();
    }

    public static String getUid(Activity activity)
    {
        sharedPreferences = activity.getSharedPreferences("SOCIAL", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Uid", "empty");
    }

    public static long getTime()
    {
        return System.currentTimeMillis();
    }
}