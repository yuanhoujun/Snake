package me.foji.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by scott on 2016/11/6.
 */
public class FragmentMesssage extends BaseFragment {
    private LinearLayout messageItemLayout;

    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        messageItemLayout = (LinearLayout) view.findViewById(R.id.item_message);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        messageItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityMessage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected boolean openSlideToClose() {
        return false;
    }
}
