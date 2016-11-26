# Snake
---
Android轻松实现类似iOS滑动关闭功能

![Alt text](https://github.com/yuanhoujun/Android_Slide_To_Close/blob/master/image/demo.gif)

# 使用方法
在你的app module的build.gradle脚本中添加如下依赖:
<pre>
    compile 'me.foji.snake:Snake:0.0.2'
</pre>

### 注意：Activity和Fragment的使用方法有一些不一样
## Activity集成方法
## 第一步：
在你的基类Activity中添加如下代码

<pre>
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 仅仅使用这一句代码，就可以开启滑动关闭功能
        Snake.init(this).enable(openSlideToClose()).start();
    }
	
	 // 推荐添加该方法，方便在子类中控制滑动关闭功能的开启或关闭
    protected boolean openSlideToClose() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 必须调用该方法，才能正常使用
        Snake.onDestroy(this);
    }
}
</pre>

## 第二步：
为了保证可以看到前一个Activity视图滑动的效果，必须设置Activity的Window透明，这里稍微有些麻烦，但没有想到更好的方法，如果你恰好有更好的方法，请Fork这个仓库，给我发送Pull request,不甚感激！
在你的app module的res/values/style.xml的主题中添加如下设置:
    
```xml
   <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
   		 <!-- 这两行设置主要是用于设置窗体透明 --> 	
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsTranslucent">true</item>
    </style>
```  

通过上面两个步骤，Activity的滑动关闭功能已经可以正常使用了，赶紧测试一下吧！

## Fragment集成方法
Fragment的集成方法相对Activity较为复杂，但依然可以使用少量的代码即可搞定！
## 第一步：
一个合格的程序员应该会给自己的Fragment设置一个基类，这里假设叫做BaseFragment，为了保证可以使用滑动关闭功能，需要对基类对一点小小的修改：

1）如果你的BaseFragment是继承自android.app.Fragment, 则将基类修改为me.foji.snake.app.Fragment, 其实它只是android.app.Fragment的一个子类而已，请放心使用。
2）如果你的基类的onCreateView方法中有对布局进行了一些共同的处理，请将这部分代码移至<code>    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)</code>方法中
3）和Activity处理方式一致，分别在Fragment的onCreate和onDestroy方法中添加如下代码：

<pre>
 	@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用方式同Activity完全一样, 记得也要在onDestory里面调用回收方法
        Snake.init(this).enable(openSlideToClose()).start();
    }

    protected boolean openSlideToClose() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Snake.onDestroy(this);
    }
</pre>

通过上面三个步骤，Fragment中的集成工作也完成了，是不是很简单？

## 注意事项：
由于Snake需要依赖于FragmentManager获取前一个Fragment，而这依赖于你对Fragment回退栈的处理。所以，为了保证Snake在Fragment中的正常使用，请将前一个Fragment放到回退栈中，并且设置合适的Tag名称！
