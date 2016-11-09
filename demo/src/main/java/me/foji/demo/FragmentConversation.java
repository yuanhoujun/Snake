package me.foji.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by scott on 2016/11/7.
 */

public class FragmentConversation extends BaseFragment {
    private Button mAuthorButton;
    private FragmentAuthor mFragmentAuthor;

    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        mAuthorButton = (Button) view.findViewById(R.id.button_author);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuthorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == mFragmentAuthor) mFragmentAuthor = new FragmentAuthor();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if(mFragmentAuthor.isAdded()) {
                    transaction.show(mFragmentAuthor);
                } else {
                    transaction.add(R.id.container, mFragmentAuthor, FragmentAuthor.class.getName());
                }
                transaction.addToBackStack(FragmentConversation.class.getName());
                transaction.commitAllowingStateLoss();
            }
        });
    }
}
