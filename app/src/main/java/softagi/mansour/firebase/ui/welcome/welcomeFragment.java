package softagi.mansour.firebase.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import softagi.mansour.firebase.R;
import softagi.mansour.firebase.ui.login.loginFragment;
import softagi.mansour.firebase.ui.register.registerFragment;
import softagi.mansour.firebase.utils.constants;

public class welcomeFragment extends Fragment
{
    private View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_welcome, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
    }

    private void initViews()
    {
        Button loginBtn = mainView.findViewById(R.id.login_btn);
        Button registerBtn = mainView.findViewById(R.id.register_btn);

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                constants.replaceFragment(welcomeFragment.this, new loginFragment(), true);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                constants.replaceFragment(welcomeFragment.this, new registerFragment(), true);
            }
        });
    }
}