package softagi.mansour.firebase.ui.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import softagi.mansour.firebase.R;

public class testFragment extends Fragment
{
    private View mainView;
    private Spinner firstSpinner;
    private Spinner secondSpinner;
    private TextView textView;

    private List<String> firstData = new ArrayList<>();
    private List<String> secondData = new ArrayList<>();

    private int first,second;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.fragment_test, null);
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
        firstSpinner = mainView.findViewById(R.id.first_spinner);
        secondSpinner = mainView.findViewById(R.id.second_spinner);
        textView = mainView.findViewById(R.id.result_text);

        setFirstSpinner();
        setSecondSpinner();
    }

    private void setFirstSpinner()
    {
        firstData.add("first");
        firstData.add("1");
        firstData.add("2");
        firstData.add("3");
        firstData.add("4");
        firstData.add("5");
        firstData.add("6");

        ArrayAdapter aa = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, firstData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstSpinner.setAdapter(aa);

        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position != 0)
                {
                    first = Integer.valueOf(firstData.get(position));

                    if (first == 3 && second == 5)
                    {
                        textView.setText("36%");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSecondSpinner()
    {
        secondData.add("second");
        secondData.add("1");
        secondData.add("2");
        secondData.add("3");
        secondData.add("4");
        secondData.add("5");
        secondData.add("6");

        ArrayAdapter aa = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, secondData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondSpinner.setAdapter(aa);

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position != 0)
                {
                    second = Integer.valueOf(secondData.get(position));

                    if (first == 3 && second == 5)
                    {
                        textView.setText("36%");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sum(int i, int x)
    {
        textView.setText(String.valueOf(i + x));
    }
}