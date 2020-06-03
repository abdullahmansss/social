package softagi.mansour.firebase.ui.users;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.mansour.firebase.R;
import softagi.mansour.firebase.models.postModel;
import softagi.mansour.firebase.models.userModel;
import softagi.mansour.firebase.ui.chats.chatsFragment;
import softagi.mansour.firebase.ui.login.loginFragment;
import softagi.mansour.firebase.ui.timeline.posts.newPostFragment;
import softagi.mansour.firebase.ui.timeline.timelineFragment;
import softagi.mansour.firebase.ui.welcome.welcomeFragment;
import softagi.mansour.firebase.utils.constants;

public class usersFragment extends Fragment
{
    private View mainView;
    private RecyclerView recyclerView;
    private List<userModel> userModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_users, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getUsers();
    }

    private void getUsers()
    {
        constants.getDatabaseReference().child("Users").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                userModels.clear();

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    userModel model = d.getValue(userModel.class);

                    if (!model.getUid().equals(constants.getUid(requireActivity())))
                    {
                        userModels.add(model);
                    }
                }

                recyclerView.setAdapter(new usersAdapter(userModels));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initViews()
    {
        recyclerView = mainView.findViewById(R.id.users_recycler);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        userModels = new ArrayList<>();
    }

    public class usersAdapter extends RecyclerView.Adapter<usersAdapter.VH>
    {
        List<userModel> userModelList;

        usersAdapter(List<userModel> userModelList)
        {
            this.userModelList = userModelList;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_user, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final VH holder, int position)
        {
            final userModel model = userModelList.get(position);

            String name = model.getName();
            String address = model.getAddress();
            String image = model.getImageUrl();

            holder.userName.setText(name);
            holder.address.setText(address);

            Picasso.get()
                    .load(image)
                    .into(holder.userImage);

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    constants.replaceFragment(usersFragment.this, new chatsFragment(), true);
                    constants.myChats = model;
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return userModelList.size();
        }

        class VH extends RecyclerView.ViewHolder
        {
            CircleImageView userImage;
            TextView userName;
            TextView address;

            VH(@NonNull View itemView)
            {
                super(itemView);

                userImage = itemView.findViewById(R.id.user_image);
                userName = itemView.findViewById(R.id.user_name);
                address = itemView.findViewById(R.id.user_address);
            }
        }
    }
}