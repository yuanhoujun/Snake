Snake
=====
**温馨提示**：Snake已经完成AndroidX适配，请放心使用！

<img src="https://raw.githubusercontent.com/yuanhoujun/Android_Slide_To_Close/master/image/snake_logo.png" width=300px, height=300px/>

# [旧版本文档](https://github.com/yuanhoujun/Snake/blob/develop/README_SUPPORT.md)

# 最新版本
模块|snake-androidx|snake-compiler-androidx
---|---|---
最新版本|[![Download](https://api.bintray.com/packages/ouyangfeng2016/android/snake-androidx/images/download.svg)](https://bintray.com/ouyangfeng2016/android/snake-androidx/_latestVersion)|[![Download](https://api.bintray.com/packages/ouyangfeng2016/android/snake-compiler-androidx/images/download.svg)](https://bintray.com/ouyangfeng2016/android/snake-compiler-androidx/_latestVersion)

# 特性介绍
* 同时支持Activity，Fragment
* 使用简单，一行代码接入，无侵入性
* 配置灵活，可以适配各种复杂场景
* 适配SDK Version >= 14以上所有机型，无副作用

# Demo下载体验
![扫描图中二维码下载](https://raw.githubusercontent.com/yuanhoujun/Snake/develop/image/demo_qrcode.png)

扫描上方二维码 或 [直接点这里下载](https://yunpang2019.oss-cn-shenzhen.aliyuncs.com/snake/demo-release.apk)

# 使用方法
1）添加依赖

```
dependencies {
    // Gradle高版本这里可以使用implementation代替compile
    // x.x.x代表上方表格中对应模块最新版本
    compile 'com.youngfeng.android:snake-androidx:x.x.x'
    annotationProcessor 'com.youngfeng.android:snake-compiler-androidx:x.x.x'
}
```

**注：如果使用Kotlin，请将annotationProcessor修改为kapt**


2）在Application中对初始化Snake

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

# Activity集成步骤
在需要开启滑动返回的Activity类上方添加注解`@EnableDragToClose`即可。

```
@EnableDragToClose()
public class FirstActivity extends Activity
```


# Fragment集成步骤
在Fragment中使用Snake实现滑动关闭比Activity更加灵活，推荐大家使用Fragment进行页面布局。目前，Snake提供了两种方式在Fragment中开启滑动关闭。

### 方法一：动态配置
第一步：在需要开启滑动返回的Fragment类上方添加注解`@EnableDragToClose`。
 
```
@EnableDragToClose(
public class FirstFragment extends Fragment {
```

第二步：使用Snake提供的方法动态创建Fragment实例。

```
// 如果Fragment继承自androidx.fragment.app.Fragment, 则使用方法newProxySupport创建实例
FirstFragment fragment = Snake.newProxySupport(FirstFragment.class);

// 如果Fragment继承自android.app.Fragment, 则使用个方法newProxy创建实例
FirstFragment fragment = Snake.newProxy(FirstFragment.class);
```

注意：在不存在默认构造函数的情况下，你需要使用注解`@PrimaryConstructor`指定将要用于创建Fragment实例的构造方法。

```
@EnableDragToClose()
public class FirstFragment extends Fragment {
    
    @PrimaryConstructor
    public FirstFragment(int x, int y) {
        
    }
    
    ...
}
```

在使用了注解标记构造函数的情况下，使用Snake方法创建实例的时候需要传入构造参数，如下所示：

```
FirstFragment fragment = Snake.newProxySupport(FirstFragment.class, 1, 2);
```

### 方法二：使用继承方式集成
按照下面的对应关系，改变你的Fragment父类即可完成滑动关闭集成:
* `android.app.Fragment` => `com.youngfeng.snake.app.Fragment`
* `androidx.fragment.app.Fragment` => `com.youngfeng.snake.androidx.app`

### 注意
1）从**0.4.0**版本开始，support库将不再提供支持，如需继续使用support库版本Fragment，请使用0.4.0以下版本，Activity不受影响。

2）由于Android 9.0已经舍弃了`android.app.Fragment`类，Snake也将不再提供对该类支持，推荐大家使用`androidx.app.Fragment`代替。

3）Snake与[Navigation](https://developer.android.com/guide/navigation/)暂时不兼容，如需使用Snake实现滑动关闭，需要自己控制Fragment页面导航。

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
        <!-- 类似iPhone X, 从底部边缘快速上滑回到桌面 (实验性功能，默认关闭） -->
        <enable_swipe_up_to_home>false</enable_swipe_up_to_home>
        <!-- 是否允许页面联动，默认为true -->
        <allow_page_linkage>true</allow_page_linkage>
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

`Snake.enableSwipeToHome()`: 如果希望在某个页面开启上滑退出到桌面功能，可以使用该接口

`Snake.swipeUpToHomeEnabled()`: 获取当前页面上滑退出到桌面功能开启状态

## WebView滑动控制
如果希望`WebView`也开启滑动控制功能，修改你的`WebView`类为`SnakeWebView`即可。目前，暂时只支持快速向左轻扫前进，快速向右轻扫回退。

## 最佳实践
1）为了避免出现大量重复代码，推荐大家使用**继承**的方式使用Snake。

2）Activity的启动是一个非常耗时的过程，为了体验效果更佳，推荐使用全**Fragment**设计，或者**单Activity+多Fragment**设计。

3）遇到问题请先查看[Wiki](https://github.com/yuanhoujun/Snake/wiki)，如果没有你想要的答案，请添加QQ交流群**288177681**及时反馈。

4）为了在Activity中获得最佳使用体验，建议大家在style文件中添加如下配置：

```
<item name="android:windowIsTranslucent">true</item>
```

5）`android.app.Fragment`与`android.support.v4.app.Fragment`都已经被Android官方舍弃，推荐大家始终使用`androidx.fragment.app.Fragment`作为Fragment类唯一选择。

## 混淆配置

```
# 如果已经应用该规则，无需重复配置
-keepattributes *Annotation*
-keep class **.*_SnakeProxy
-keep @com.youngfeng.snake.annotations.EnableDragToClose public class *
```

## 微信公众号
![欧阳锋工作室](https://raw.githubusercontent.com/yuanhoujun/Android_Slide_To_Close/develop/image/%E6%AC%A7%E9%98%B3%E9%94%8B%E5%B7%A5%E4%BD%9C%E5%AE%A4.jpg)

**微信公众号也是一个高效的问题反馈平台，如需帮助请在微信公众号中给我留言，我会第一时间查看！***

## QQ交流群
**QQ群**：288177681

如果你在使用过程中遇到了任何问题，欢迎加群交流。如果你想给作者支持，请点击上方star支持。

PS: 如果你在产品中使用了`Snake`, 请来信告诉我！邮件地址：**626306805@qq.com**，非常感谢！

License
==
   Copyright 2018 Ouyangfeng Office

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
