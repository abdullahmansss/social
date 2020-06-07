package softagi.mansour.firebase.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import softagi.mansour.firebase.R;
import softagi.mansour.firebase.ui.home.homeFragment;
import softagi.mansour.firebase.ui.test.testFragment;
import softagi.mansour.firebase.ui.welcome.welcomeFragment;
import softagi.mansour.firebase.utils.constants;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constants.initFirebase();

        if (!constants.getUid(this).equals("empty"))
        {
            startFragment(new testFragment());
        } else
            {
                startFragment(new welcomeFragment());
            }
    }

    private void startFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .disallowAddToBackStack()
                .commit();
    }
}