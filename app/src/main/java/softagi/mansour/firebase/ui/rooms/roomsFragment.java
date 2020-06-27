package softagi.mansour.firebase.ui.rooms;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
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
import softagi.mansour.firebase.models.roomModel;
import softagi.mansour.firebase.models.userModel;
import softagi.mansour.firebase.ui.rooms.newRoom.newRoomFragment;
import softagi.mansour.firebase.utils.constants;

public class roomsFragment extends Fragment
{
    private View mainView;
    private FloatingActionButton addRoom;
    private RecyclerView recyclerView;
    private List<roomModel> roomModels;
    private userModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_rooms, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initViews();
        getRooms();
        getUser();
    }

    private void getUser()
    {
        String uId = constants.getUid(requireActivity());

        constants.getDatabaseReference().child("Users").child(uId).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                userModel = dataSnapshot.getValue(userModel.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void getRooms()
    {
        constants.getDatabaseReference().child("Rooms").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                roomModels.clear();

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    roomModel model = d.getValue(roomModel.class);

                    if (!constants.getUid(requireActivity()).equals(model.getuId()))
                        roomModels.add(model);
                }

                recyclerView.setAdapter(new roomsAdapter(roomModels));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initViews()
    {
        addRoom = mainView.findViewById(R.id.add_room_fab);

        recyclerView = mainView.findViewById(R.id.rooms_recycler);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        roomModels = new ArrayList<>();

        addRoom.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                constants.replaceFragment(roomsFragment.this,new newRoomFragment(), true);
            }
        });
    }

    public class roomsAdapter extends RecyclerView.Adapter<roomsAdapter.VH>
    {
        List<roomModel> roomModelList;

        roomsAdapter(List<roomModel> roomModels)
        {
            this.roomModelList = roomModels;
        }

        @NonNull
        @Override
        public roomsAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_room, parent, false);
            return new roomsAdapter.VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final roomsAdapter.VH holder, int position)
        {
            final roomModel model = roomModelList.get(position);

            String title = model.getRoomTitle();
            String owner = model.getOwnerName();
            String image = model.getRoomImage();

            holder.roomTitle.setText(title);
            holder.roomOwnerName.setText(owner);

            Picasso.get()
                    .load(image)
                    .into(holder.roomOwnerImage);

            setRoomMembers(model.getRoomId(), holder.roomMembers);

            if (model.isPrivate())
            {
                holder.privateRoom.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("are you sure to exit this room?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    // FIRE ZE MISSILES!
                                    String uId = constants.getUid(requireActivity());
                                    constants.getDatabaseReference().child("Members").child(model.getRoomId()).child(uId).removeValue();
                                }
                            })
                            .setNegativeButton("no", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    // User cancelled the dialog
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String uId = constants.getUid(requireActivity());

                    if (!model.isPrivate())
                    {
                        constants.getDatabaseReference().child("Members").child(model.getRoomId()).child(uId).setValue(true);
                    } else
                        {
                            constants.getDatabaseReference().child("RoomsRequests").child(model.getuId()).child(model.getRoomId()).child(uId).setValue(userModel);
                        }
                }
            });
        }

        void setRoomMembers(String roomId, TextView textView)
        {
            constants.getDatabaseReference().child("Members").child(roomId).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    long count = dataSnapshot.getChildrenCount();
                    textView.setText(String.valueOf(count));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        @Override
        public int getItemCount()
        {
            return roomModelList.size();
        }

        class VH extends RecyclerView.ViewHolder
        {
            CircleImageView roomOwnerImage;
            ImageView privateRoom;
            TextView roomTitle;
            TextView roomMembers;
            TextView roomOwnerName;

            VH(@NonNull View itemView)
            {
                super(itemView);

                roomOwnerImage = itemView.findViewById(R.id.room_user_image);
                privateRoom = itemView.findViewById(R.id.private_room);
                roomTitle = itemView.findViewById(R.id.room_title);
                roomMembers = itemView.findViewById(R.id.room_members);
                roomOwnerName = itemView.findViewById(R.id.room_owner_name);
            }
        }
    }
}