package me.foji.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.youngfeng.annotations.EnableDragToClose;

import me.foji.snake.annotations.SlideToClose;

/**
 * @author Scott Smith  @Date 2016年11月2016/11/7日 14:11
 */
@EnableDragToClose
public class FragmentAuthor extends BaseFragment {
    private Button mJianShuButton;

    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author, container, false);
        mJianShuButton = (Button) view.findViewById(R.id.button_jianshu);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mJianShuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse(getString(R.string.url_jianshu));
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
}
