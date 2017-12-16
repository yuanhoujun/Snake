package me.foji.demo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.youngfeng.Snake;
import com.youngfeng.annotations.EnableDragToClose;

import java.util.ArrayList;

import me.foji.widget.QuickAutoScrollViewPager;
import me.foji.widget.QuickScrollPagerAdapter;

/**
 * Created by scott on 2016/11/7.
 */
@EnableDragToClose()
public class FragmentConversation extends BaseFragment {
    private Button mAuthorButton;
    private FragmentAuthor mFragmentAuthor;
    private QuickScrollPagerAdapter<Integer> mAdapter;
    private QuickAutoScrollViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        mAuthorButton = (Button) view.findViewById(R.id.button_author);
        mViewPager = (QuickAutoScrollViewPager) view.findViewById(R.id.viewPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuthorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == mFragmentAuthor) mFragmentAuthor = Snake.newProxy(FragmentAuthor.class);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
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

        setViewPager();
    }

    private void setViewPager() {
        final ArrayList<Integer> datas = new ArrayList<>();
        datas.add(R.mipmap.cat1);
        datas.add(R.mipmap.cat2);
        datas.add(R.mipmap.cat3);

        if(null == mAdapter) {
            mAdapter = new QuickScrollPagerAdapter<Integer>(datas) {
                @Override
                public void convert(ImageView imageView, int i, Integer integer) {
                    imageView.setImageResource(integer);
                }
            };
        }
        mViewPager.setAdapter(mAdapter);
    }
}
