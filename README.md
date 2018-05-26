# WaterRippleView
水波纹动画

# 使用方法
直接在xml中引入
```xml
<com.allenyu.view.WaterRippleView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    />
```

# 效果图


# 原理
1. 通过半径List来控制每个圆的半径
1. 圆的透明度与半径成线性关系，逐步递减，最后透明度为0
1. 通过Choreographer实现定时刷新
1. 每绘制一次都扩大圆的半径
1. 最后一个加入的圆每达到一定大小则增加一个新的圆
1. 超过圆的总数量则移除第一个圆

# 注意
里面依赖到第三方的只有DensityUtil.dp2px，别无依赖项
