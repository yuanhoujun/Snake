本次更新内容有：
==
* Fragment增加继承方式集成

## 使用方法
按照下面的对应关系，改变你的Fragment父类就可以完成滑动关闭集成:
* `android.app.Fragment` => `com.youngfeng.snake.app.Fragment`
* `android.support.v4.app.Fragment` => `com.youngfeng.snake.support.v4.app.Fragment`

## 两种集成方案的区别
集成方案|newProxy/newProxySupport|使用继承
:---:|:---:|:---:
侵入性|无|改变了顶级父类
难易程度|稍难一点|简单
动画处理|需要自行处理|不需要处理
实例创建|必须使用newProxy/newProxySupport创建|可以自行处理

**注意：使用继承方式集成的情况下，原来的API完全可以通用。你可以选择使用Snake的API进行滑动控制，也可以使用父类中的方法进行滑动控制，这取决于你自己。甚至实例创建你依然可以交给newProxy/newProxySupport接口。**

*一点建议：如果你的工程有一致的编程规范，代码工整，我推荐你使用继承的方式集成。如果你的工程相对较乱，整体表现不一致，我推荐你使用newProxy/newProxySupport方式集成，灵活性更高。*

## 更新建议
> #### **推荐更新**

