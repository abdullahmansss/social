package softagi.mansour.firebase.ui.timeline;

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
import softagi.mansour.firebase.ui.timeline.posts.newPostFragment;
import softagi.mansour.firebase.utils.constants;

public class timelineFragment extends Fragment
{
    private View mainView;
    private RecyclerView recyclerView;
    private List<postModel> postModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_timeline, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        constants.initProgress(requireContext(), "please wait ..");
        initViews();
        getPosts();
    }

    private void getPosts()
    {
        constants.showProgress();

        constants.getDatabaseReference().child("Posts").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                postModels.clear();

                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    postModel model = d.getValue(postModel.class);

                    postModels.add(model);
                }

                recyclerView.setAdapter(new postsAdapter(postModels));

                if (postModels.size() != 0)
                {
                    recyclerView.smoothScrollToPosition(postModels.size() - 1);
                }
                constants.dismissProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void initViews()
    {
        recyclerView = mainView.findViewById(R.id.posts_recycler);
        FloatingActionButton addPost = mainView.findViewById(R.id.add_post_fab);

        postModels = new ArrayList<>();

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constants.replaceFragment(timelineFragment.this, new newPostFragment(), true);
            }
        });
    }

    public class postsAdapter extends RecyclerView.Adapter<postsAdapter.VH>
    {
        List<postModel> postModelList;

        postsAdapter(List<postModel> postModelList)
        {
            this.postModelList = postModelList;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_post, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final VH holder, int position)
        {
            final postModel model = postModelList.get(position);

            int type = model.getType();

            if (type == 0)
            {
                holder.postImage.setVisibility(View.GONE);

                String name = model.getUserName();
                long time = model.getPostTime();
                String userImage = model.getUserImage();
                String text = model.getPostText();

                holder.postUserName.setText(name);

                long now = System.currentTimeMillis();

                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

                holder.postTime.setText(String.valueOf(ago));
                holder.postText.setText(text);

                Picasso
                        .get()
                        .load(userImage)
                        .into(holder.postUserImage);

                setLikesCount(model.getPostId(), holder.postLikesCount);
                isLike(model.getPostId(), holder.postLike);
            } else if (type == 1)
                {
                    String name = model.getUserName();
                    long time = model.getPostTime();
                    String userImage = model.getUserImage();
                    String text = model.getPostText();
                    String image = model.getPostImage();

                    holder.postUserName.setText(name);

                    long now = System.currentTimeMillis();

                    CharSequence ago =
                            DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

                    holder.postTime.setText(String.valueOf(ago));
                    holder.postText.setText(text);

                    Picasso
                            .get()
                            .load(userImage)
                            .into(holder.postUserImage);

                    Picasso
                            .get()
                            .load(image)
                            .into(holder.postImage);

                    setLikesCount(model.getPostId(), holder.postLikesCount);
                    isLike(model.getPostId(), holder.postLike);
                }
        }

        @Override
        public int getItemCount()
        {
            return postModelList.size();
        }

        void setLikesCount(String postId, final TextView textView)
        {
            constants.getDatabaseReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    long count = dataSnapshot.getChildrenCount();

                    Log.d("likes count", String.valueOf(count));

                    if (count > 0)
                    {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(count + " Likes");
                    } else
                        {
                            textView.setVisibility(View.GONE);
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }

        void isLike(final String postId, final TextView textView)
        {
            constants.getDatabaseReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.hasChild(constants.getUid(requireActivity())))
                    {
                        textView.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.like));
                        textView.setText("Dislike");

                        textView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                constants.getDatabaseReference().child("Likes").child(postId).child(constants.getUid(requireActivity())).removeValue();
                            }
                        });
                    } else
                        {
                            textView.setCompoundDrawableTintList(ContextCompat.getColorStateList(requireActivity(), R.color.disLike));
                            textView.setText("Like");

                            textView.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    constants.getDatabaseReference().child("Likes").child(postId).child(constants.getUid(requireActivity())).setValue(true);
                                }
                            });
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
            CircleImageView postUserImage;
            ImageView postImage;
            TextView postUserName;
            TextView postTime;
            TextView postText;
            TextView postLikesCount;
            TextView postLike;
            TextView postComment;
            TextView postShare;

            VH(@NonNull View itemView)
            {
                super(itemView);

                postUserImage = itemView.findViewById(R.id.post_user_image);
                postImage = itemView.findViewById(R.id.post_image);
                postUserName = itemView.findViewById(R.id.post_user_name);
                postTime = itemView.findViewById(R.id.post_time);
                postText = itemView.findViewById(R.id.post_text);
                postLikesCount = itemView.findViewById(R.id.post_likes_count);
                postLike = itemView.findViewById(R.id.post_like);
                postComment = itemView.findViewById(R.id.post_comment);
                postShare = itemView.findViewById(R.id.post_share);
            }
        }
    }
}