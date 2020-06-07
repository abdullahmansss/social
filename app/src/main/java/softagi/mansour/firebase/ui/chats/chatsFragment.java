package softagi.mansour.firebase.ui.chats;

import android.content.res.ColorStateList;
import android.media.Image;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import softagi.mansour.firebase.models.chatModel;
import softagi.mansour.firebase.models.myChatsModel;
import softagi.mansour.firebase.models.userModel;
import softagi.mansour.firebase.ui.users.usersFragment;
import softagi.mansour.firebase.utils.constants;

public class chatsFragment extends Fragment
{
    private View mainView;
    private RecyclerView recyclerView;

    private EditText chatField;

    private List<chatModel> chatModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_chats, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getChat();
    }

    private void getChat()
    {
        constants.getDatabaseReference().child("Chats").child(constants.getUid(requireActivity())).child(constants.myChats.getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                chatModels.clear();

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    chatModel model = d.getValue(chatModel.class);

                    chatModels.add(model);
                }

                recyclerView.setAdapter(new chatAdapter(chatModels));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initViews()
    {
        recyclerView = mainView.findViewById(R.id.chat_recycler);
        chatField = mainView.findViewById(R.id.message_body_field);
        Toolbar toolbar = mainView.findViewById(R.id.chat_toolbar);
        CircleImageView circleImageView = mainView.findViewById(R.id.chat_image);
        TextView textView = mainView.findViewById(R.id.chat_title);
        FloatingActionButton sendFab = mainView.findViewById(R.id.send_message_fab);

        chatModels = new ArrayList<>();

        sendFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String message = chatField.getText().toString();

                if (message.isEmpty())
                {
                    constants.showToast(requireContext(), "type a message");
                    return;
                }

                sendMessage(message);
            }
        });

        Picasso.get()
                .load(constants.myChats.getImageUrl())
                .into(circleImageView);

        textView.setText(constants.myChats.getName());

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_white_ios_24); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

/*        requireActivity().setActionBar(toolbar);

        requireActivity().getActionBar().setTitle("");
        requireActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        requireActivity().getActionBar().setDisplayShowHomeEnabled(true);
        requireActivity().getActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_white_ios_24));
        requireActivity().getActionBar().*/
    }

    private void sendMessage(String message)
    {
        String senderId = constants.getUid(requireActivity());
        String receiverId = constants.myChats.getUid();

        String key = constants.getDatabaseReference().child("Chats").child(senderId).child(receiverId).push().getKey();

        chatModel chatModel = new chatModel(
                senderId,
                message,
                constants.getTime(),
                1,
                key
        );

        myChatsModel myChatsModel = new myChatsModel(
                constants.myChats.getName(),
                constants.myChats.getImageUrl()
        );

        if (key != null)
        {
            // ridge send a message to mansour
            constants.getDatabaseReference().child("Chats").child(senderId).child(receiverId).child(key).setValue(chatModel);

            // mansour receive message from ridge
            constants.getDatabaseReference().child("Chats").child(receiverId).child(senderId).child(key).setValue(chatModel);

            constants.getDatabaseReference().child("Chats").child(senderId).child(key).setValue(chatModel);
            constants.getDatabaseReference().child("Chats").child(receiverId).child(key).setValue(chatModel);

            constants.getDatabaseReference().child("MyChats").child(senderId).child(receiverId).setValue(myChatsModel);
            constants.getDatabaseReference().child("MyChats").child(receiverId).child(senderId).setValue(myChatsModel);

            chatField.setText("");
        }
    }

    public class chatAdapter extends RecyclerView.Adapter<chatAdapter.VH>
    {
        List<chatModel> chatModelList;

        chatAdapter(List<chatModel> chatModelList)
        {
            this.chatModelList = chatModelList;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_chat_message, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final VH holder, int position)
        {
            chatModel model = chatModelList.get(position);

            String message = model.getMessage();
            long time = model.getTime();
            String id = model.getSenderId();

            holder.message.setText(message);

            long now = System.currentTimeMillis();

            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

            holder.time.setText(ago);

            if (id.equals(constants.getUid(requireActivity())))
            {
                holder.card.setCardBackgroundColor(getResources().getColor(R.color.chat));
                holder.message.setTextColor(getResources().getColor(R.color.white));
                holder.time.setTextColor(getResources().getColor(R.color.white));
                holder.linearLayout.setGravity(Gravity.END);
            }

            setSeen(model.getId());
            isSeen(model.getId(), holder.seen);
        }

        @Override
        public int getItemCount()
        {
            return chatModelList.size();
        }

        public void setSeen(String id)
        {
            constants.getDatabaseReference().child("Seen").child(constants.getUid(requireActivity())).child(constants.myChats.getUid()).child(id).setValue(true);
        }

        public void isSeen(final String id, final ImageView imageView)
        {
            constants.getDatabaseReference().child("Seen").child(constants.myChats.getUid()).child(constants.getUid(requireActivity())).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.hasChild(id))
                    {
                        imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.like)));
                    } else
                        {
                            imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.disLike)));
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        class VH extends RecyclerView.ViewHolder
        {
            TextView message;
            TextView time;
            ImageView seen;
            CardView card;
            LinearLayout linearLayout;

            VH(@NonNull View itemView)
            {
                super(itemView);

                message = itemView.findViewById(R.id.chat_message_text);
                time = itemView.findViewById(R.id.chat_time_text);
                seen = itemView.findViewById(R.id.chat_seen);
                card = itemView.findViewById(R.id.chat_card);
                linearLayout = itemView.findViewById(R.id.chat_linear);
            }
        }
    }

/*    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                requireActivity().onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }*/
}