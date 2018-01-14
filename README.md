Snake
=====


<img src="https://raw.githubusercontent.com/yuanhoujun/Android_Slide_To_Close/master/image/snake_logo.png" width=400px, height=400px/>


如果你使用的**Snake**版本低于0.1.0, 请 [点这里查看老版本文档](https://github.com/yuanhoujun/Android_Slide_To_Close/blob/master/README_OLD.md)

## 最新版本
模块|snake|snake-compiler|snake-annotations
---|---|---|---
最新版本|[![Download](https://api.bintray.com/packages/ouyangfeng2016/android/snake/images/download.svg)](https://bintray.com/ouyangfeng2016/android/snake/_latestVersion)|[![Download](https://api.bintray.com/packages/ouyangfeng2016/android/snake-compiler/images/download.svg)](https://bintray.com/ouyangfeng2016/android/snake-compiler/_latestVersion)|[![Download](https://api.bintray.com/packages/ouyangfeng2016/android/snake-annotations/images/download.svg)](https://bintray.com/ouyangfeng2016/android/snake-annotations/_latestVersion)

## 特性介绍
* 同时支持**Activity**，**Fragment**，**WebView**
* 使用简单，无侵入性
* 配置灵活，可以满足各种业务需求
* 适配SDK Version >= 14以上所有机型，无副作用

## Demo下载体验
![扫描图中二维码下载](https://raw.githubusercontent.com/yuanhoujun/Android_Slide_To_Close/master/image/demo_snapshot.jpg)

## 使用方法
1）添加依赖
```
dependencies {
    // Gradle高版本这里可以使用implementation代替compile
    // x.x.x代表上方表格中对应模块最新版本
    compile 'com.youngfeng.android:snake:x.x.x'
    annotationProcessor 'com.youngfeng.android:snake-compiler:x.x.x'
}
```

2）在**Application**中对**Snake**进行初始化
```
public class SnakeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        
        // 对Snake进行初始化
        Snake.init(this);
    }
}
```

3）在**Activity**中使用
* 添加注解 **@EnableDragToClose**，开启滑动关闭功能
```
@EnableDragToClose()
public class FirstActivity extends Activity
```
* 在 **Activity.onCreate** 方法中使用 **Snake.host()** 接口对其进行托管
```
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Snake.host(this);
 }
```

4）在**Fragment**中使用
* 同Activity一样，先添加注解 **@EnableDragToClose**, 开启滑动关闭功能
```
@EnableDragToClose()
public class FirstFragment extends Fragment {
```
* 在跳转至当前 **Fragment** 时，如果你的 **Fragment** 类继承自 **android.app.Fragment** ，则使用 **Snake.newProxy(xx.class)** 创建  **Fragment** 实例。而如果你的 **Fragment** 类继承自 **android.support.v4.app.Fragment** ，则使用  **Snake.newProxySupport(xx.class)** 创建 **Fragment** 实例。

**注意：**Fragment**无需使用**host**接口对其进行托管，**Snake**将自动完成对其进行托管**

## 标记主构造方法
在**Fragment**中，可能不存在默认构造方法。或者使用了多个构造方法，这个时候你可以使用**PrimaryConstructor**指定
主构造方法。
```
@EnableDragToClose()
public class FirstFragment extends Fragment {
    
    @PrimaryConstructor
    public FirstFragment(int x, int y) {
        
    }

```

在使用了主构造器的情况下，使用**Snake.newProxy**接口创建实例的时候需要传入构造参数，以上述代码片段为例，可以这样使用：
```
    FirstFragment fragment = Snake.newProxy(FirstFragment.class, 1, 2);
```
**Snake.newProxySupport**接口同理

## 滑动参数配置
通常情况下，完成上面的步骤，你已经可以正常使用滑动关闭功能了。可是，有些同学可能希望对滑动样式进行定制化。别担心，
**Snake**提供了两种方式对滑动参数进行配置。

* 全局滑动参数配置
如果你希望对所有页面应用滑动参数配置，可以使用**snake.xml**文件对参数进行配置，在工程的根目录下面，我提供了配置模板
```
<?xml version="1.0" encoding="utf-8"?>
<snake>
    <config>
        <!-- 设置为true，根Activity也能够滑动关闭，这很奇怪！不建议修改这个变量的默认值 -->
        <enable_for_root_activity>false</enable_for_root_activity>
        <!-- 设置为true，将监听当前页面所有位置往右快速滑动手势 -->
        <only_listen_to_fast_swipe>false</only_listen_to_fast_swipe>
        <!-- 快速滑动最低检测速度，不建议修改。过高会影响灵敏度，过低会导致误判 -->
        <min_velocity>2000</min_velocity>
        <!-- 设置为true，滑动时左侧边缘阴影将被隐藏, 这个变量的默认值也不建议修改 -->
        <hide_shadow_of_edge>false</hide_shadow_of_edge>
        <!-- 阴影边缘渐变色起始颜色 -->
        <shadow_start_color>#00000000</shadow_start_color>
        <!-- 阴影边缘渐变色结束颜色 -->
        <shadow_end_color>#50000000</shadow_end_color>
    </config>
</snake>
```
修改模板参数，复制当前xml文件，放到主工程目录的**assets**下面即可，名称必须依然是**snake.xml**，不能修改！

* 单页面参数配置
如果你只希望对单个页面应用滑动参数配置，可以使用**@SetDragParameter**对其进行配置：
```
@EnableDragToClose()
@SetDragParameter(minVelocity = 2000, hideShadowOfEdge = false ...)
public class FirstActivity extends Activity
```

## 其它接口介绍
`Snake.enableDragToClose()`：如果你希望动态开启或关闭【滑动关闭】特性，可以使用该接口

`Snake.addDragListener()`：如果你希望在滑动过程中进行一些额外的处理，可以使用该接口监听整个滑动过程。

`Snake.setCustomTouchInterceptor`：如果你在使用过程中，出现了一些滑动冲突问题，你可以通过使用该接口自定义拦截器解决。
注意：大多数情况下你不需要理会该接口，如果确定是需要解决这种滑动冲突问题，可以使用该接口。

`Snake.dragToCloseEnabled()`：如果你需要知道滑动关闭功能在当前页面是否处于开启状态，可以使用该接口。

## 动画处理
至此，你已经成功集成了滑动关闭功能，并且也知道了如何配置滑动关闭参数。可是，你会发现，如果使用系统返回键，Activity的
动画表现和滑动关闭不一致，有些不协调。另外，**Fragment**似乎在滑动关闭后还会再播放一次动画，看起来很奇怪。

为此，**Snake**提供了几种不同的动画实现和滑动关闭动画配合使用，使其看起来是完全一致的。
* `R.anim.snake_slide_in_left.xml`: 从左进入
* `R.anim.snake_slide_in_right.xml`: 从右进入
* `R.anim.snake_slide_out_left.xml`：从左退出
* `R.anim.snake_slide_out_right.xml`：从右退出
* `R.animator.snake_slide_in_left.xml`: 从左进入
* `R.animator.snake_slide_in_right.xml`: 从右进入
* `R.animator.snake_slide_out_left.xml`：从左退出
* `R.animator.snake_slide_out_right.xml`：从右退出

具体使用方法，可以参照Demo配置。

在**Activity**启动和关闭的时候，使用这几种动画配置基本就解决了滑动关闭的动画样式不一致问题。可是，**Fragment**的动画
重复播放问题依然存在。为了使**Fragment**表现一致，你还需要额外做一个工作：

* 重写`onCreateAnimation`或`onCreateAnimator`接口
* 实现`SnakeAnimationController`接口
推荐在**Fragment**基础父类中做这项工作，具体实现后的效果如下：
```
public class BaseFragment extends Fragment implements SnakeAnimationController {
    private boolean mDisableAnimation;

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return Snake.wrap(super.onCreateAnimator(transit, enter, nextAnim), this);
    }

    @Override
    public void disableAnimation(boolean disable) {
        mDisableAnimation = disable;
    }

    @Override
    public boolean animationDisabled() {
        return mDisableAnimation;
    }
}
```

**注意：在重写的`onCreateAnimator`或`onCreateAnimation`接口中，请使用`Snake.wrap`接口对父类实现进行包裹。否则，将导致设置无效，具体实现可以参照Demo实现**

## Best Practice
1）Activity的启动是一个耗时的过程，为了体验效果更佳，推荐使用全**Fragment**设计，或者说**Activity+多Fragment**设计。
另外，由于Android系统的兼容性问题，在SDK版本低于21的机型中，Activity的关闭将使用快速右滑手势，只有在高于21的机型中，
才能使用联动拖拽的方式进行滑动关闭。

2）建议在Activity和Fragment基类中使用`@EnableDragToClose`注解，这可以避免在子类中频繁使用注解设置。Activity的托管也建议在父类中完成。

3）不推荐对滑动样式进行自定义设置，默认样式在UI体现上已经比较漂亮，繁琐的设计反而会干扰你的理解。

4）遇到问题请先查看[Wiki](https://github.com/yuanhoujun/Android_Slide_To_Close/wiki)，看是否有你想要的答案。如果没有，请使用**Gitter**联系我，给我发送消息。如果发现问题，请给我推送issue，非常欢迎你帮我发现问题。

## 联系我
如果你在使用过程中，有任何不能解决的问题，请来Gitter IM讨论
[![Join the chat at https://gitter.im/Android_Slide_To_Close/Lobby](https://badges.gitter.im/Android_Slide_To_Close/Lobby.svg)](https://gitter.im/Android_Slide_To_Close/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**关注欧阳锋工作室，学更多编程知识**

![欧阳锋工作室](https://raw.githubusercontent.com/yuanhoujun/Android_Slide_To_Close/develop/image/%E6%AC%A7%E9%98%B3%E9%94%8B%E5%B7%A5%E4%BD%9C%E5%AE%A4.jpg)

**相关文章** 

* [将滑动关闭进行到底](https://www.jianshu.com/p/7cf6864c9bde)

PS: 如果你在产品中使用了**Snake**, 请来信告诉我！邮件地址：**626306805@qq.com**