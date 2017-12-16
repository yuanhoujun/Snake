package me.foji.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.foji.snake.annotations.SlideToClose;

/**
 * Created by scott on 2016/11/6.
 */
@SlideToClose(enable = false)
public class FragmentMesssage extends BaseFragment {
    private LinearLayout messageItemLayout;
    private Button mSlideToCloseBtn;
    @BindView(R.id.btn_snake) Button mSnakeBtn;

    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        messageItemLayout = (LinearLayout) view.findViewById(R.id.item_message);
        mSlideToCloseBtn = (Button) view.findViewById(R.id.btn_test_fragment);
        ButterKnife.bind(this, view);

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

        mSlideToCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TestFragmentActivity.class);
                startActivity(intent);
            }
        });

        mSnakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void impl() throws IOException {

    }

    @Override
    protected boolean openSlideToClose() {
        return false;
    }
}
