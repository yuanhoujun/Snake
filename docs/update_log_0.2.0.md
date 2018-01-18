本次更新内容有：
==
* 增加`SnakeWebView`, 提供对 **WebView** 的滑动手势控制。目前，暂时只支持轻扫手势。
* 增加类似iPhone X快速上滑回退到桌面功能（实验性功能）
* `Snake.onDragListener`接口细化
* 快速滑动页面闪动问题修复

**注意：如果你在`0.1.0`版本中使用了`Snake.addOnDragListener`接口并且重写了`onDrag`方法，为了兼容，需要手动添加一个参数`boolean settling`.**

## 更新建议
> #### **推荐更新**

