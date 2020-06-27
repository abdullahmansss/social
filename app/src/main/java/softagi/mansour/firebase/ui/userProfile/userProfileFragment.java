package softagi.mansour.firebase.ui.userProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import softagi.mansour.firebase.R;
import softagi.mansour.firebase.models.userModel;
import softagi.mansour.firebase.ui.chats.chatsFragment;
import softagi.mansour.firebase.ui.users.usersFragment;
import softagi.mansour.firebase.utils.constants;

public class userProfileFragment extends Fragment
{
    private View mainView;
    private TextView textView;
    private String uId;
    private userModel userModel;

    public userProfileFragment(String getuId)
    {
        this.uId = getuId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_user_profile, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getUserData(uId);
    }

    private void getUserData(String uId)
    {
        constants.getDatabaseReference().child("Users").child(uId).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                userModel = dataSnapshot.getValue(userModel.class);

                textView.setText(userModel.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initViews()
    {
        textView = mainView.findViewById(R.id.user_profile_name);

        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                constants.replaceFragment(userProfileFragment.this, new chatsFragment(), true);
                constants.myChats = userModel;
            }
        });
    }
}