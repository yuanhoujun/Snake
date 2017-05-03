# Snake
---
Android轻松实现类似iOS滑动关闭功能

![Alt text](https://github.com/yuanhoujun/Android_Slide_To_Close/blob/master/image/demo.gif)

# 重大升级
# 集成方法变更为使用JitPack集成
1）增加以下脚本到你的工程根目录的build.gradle文件中
<pre>
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
</pre>
	
在你的app工程中，增加如下依赖:
<pre>
dependencies {
    compile 'com.github.yuanhoujun:Android_Slide_To_Close:0.0.5'
}
</pre>

# 版本更新日志：
# Version 0.0.5
这个版本主要修复了以下问题：

1）me.snake.app.Fragment类在第二个Fragment无法使用侧滑正常回退至第一个Fragment

2）如果不使用继承基类的方式使用Snake框架，将导致Snake运行异常

# Version 0.0.6
修复了 issue #3 ： Activity关闭出现内存泄露问题，感谢 [yuki-ryoko](https://github.com/yuki-ryoko)

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
        Snake.init(this);
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

## 自定义设置
相对于0.0.2版本，0.0.3版本使用注解完成一些自定义的参数设置，并且可以通过注解随时控制滑动关闭的开启和关闭。
<pre>
@SlideToClose(enable = false , shadowStartColor = 0xff0000 , shadowEndColor = 0x00ff00 , minVelocity = 3000)
public class MainActivity extends BaseActivity implements View.OnClickListener {
   ...
}
</pre>

Note：要使注解生效，请确保在Activity的onCreate方法中调用了<code>Snake.init(this)</code>方法或者在父类中调用了该方法.

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
    // 使用方式同Activity完全一样
    Snake.init(this);
}
</pre>

通过上面三个步骤，Fragment中的集成工作也完成了，是不是很简单？

Note: Fragment和Activity一样，也使用注解完成自定义设置

## 注意事项：
* 由于Snake需要依赖于FragmentManager获取前一个Fragment，而这依赖于你对Fragment回退栈的处理。所以，为了保证Snake在Fragment中的正常使用，请将前一个Fragment放到回退栈中，并且设置合适的Tag名称！
* 为了保证侧滑功能可以正常使用，我们在style文件中设置了窗体透明。这个时候，如果你的根布局没有设置背景色，将导致
  页面透明，直接可以看到前一级页面。因此，为了保证Snake功能运行正常，请将每一个页面的根布局设置一个背景色。可以统一
  在基类里面处理
  
**如果你在使用过程中还出现了其它问题，请给我推送issue，或者去我的简书给我留言，我一定会在第一时间进行修复！**  
