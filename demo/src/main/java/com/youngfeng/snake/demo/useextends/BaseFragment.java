package com.youngfeng.snake.demo.useextends;

import com.youngfeng.snake.androidx.app.Fragment;
import com.youngfeng.snake.annotations.EnableDragToClose;

/**
 * 使用继承的方式集成，子类不再需要添加注解@EnableDragToClose，Activity集成方式同理。
 *
 * @author Scott Smith 2019-11-26 09:36
 */
@EnableDragToClose()
public class BaseFragment extends Fragment {
}
