package softagi.mansour.firebase.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;
import softagi.mansour.firebase.R;
import softagi.mansour.firebase.ui.myChats.myChatsFragment;
import softagi.mansour.firebase.ui.profile.profileFragment;
import softagi.mansour.firebase.ui.rooms.myRooms.myRoomsFragment;
import softagi.mansour.firebase.ui.rooms.roomsFragment;
import softagi.mansour.firebase.ui.timeline.timelineFragment;
import softagi.mansour.firebase.ui.users.usersFragment;

public class homeFragment extends Fragment
{
    private View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_home, null);
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
        ViewPager2 viewPager2 = mainView.findViewById(R.id.viewpager);
        TabLayout tabLayout = mainView.findViewById(R.id.tabs);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new timelineFragment());
        fragments.add(new myRoomsFragment());
        fragments.add(new myChatsFragment());
        fragments.add(new roomsFragment());
        fragments.add(new profileFragment());

        final List<String> names = new ArrayList<>();

        names.add("Timeline");
        names.add("My Rooms");
        names.add("Chats");
        names.add("Rooms");
        names.add("Profile");

        tabsAdapter adapter = new tabsAdapter(homeFragment.this, fragments);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy()
        {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position)
            {
                tab.setText(names.get(position));
/*                if (position == 0)
                {
                    tab.setIcon(R.drawable.ic_camera_black_24dp);
                } else
                    {
                        tab.setText(names.get(position));
                    }*/
            }
        }).attach();

        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //tabLayout.getTabAt(1).select();
        //        //viewPager2.setCurrentItem(1);
    }

    public class tabsAdapter extends FragmentStateAdapter
    {
        private List<Fragment> fragmentList;

        tabsAdapter(Fragment fragment, List<Fragment> fragments)
        {
            super(fragment);
            this.fragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount()
        {
            return fragmentList.size();
        }
    }
}