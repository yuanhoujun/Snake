package me.foji.demo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.foji.snake.Snake;
import me.foji.snake.annotations.SlideToClose;
import me.foji.snake.app.Fragment;

/**
 * Fragment one
 * @author Scott Smith 2017-05-01 15:35
 */
@SlideToClose(enable = false)
public class FragmentOne extends Fragment {
    private Button mFragmentTwoBtn;
    private FragmentTwo mFragmentTwo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snake.init(this);
    }

    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        mFragmentTwoBtn = (Button) view.findViewById(R.id.btn_fragment_two);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFragmentTwoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == mFragmentTwo) mFragmentTwo = new FragmentTwo();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(mFragmentTwo.isAdded()) {
                    transaction.show(mFragmentTwo);
                } else {
                    transaction.add(R.id.container, mFragmentTwo, FragmentTwo.class.getName());
                }
                transaction.addToBackStack(FragmentOne.class.getName());
                transaction.commit();
            }
        });
    }
}
