<div align="center">
    <h1>电视直播<sup>TV</sup></h1>
<div align="center">


![GitHub Repo stars](https://img.shields.io/github/stars/mytv-android/mytv-android)
![GitHub all releases](https://img.shields.io/github/downloads/mytv-android/mytv-android/total)
[![Android Sdk Require](https://img.shields.io/badge/Android-5.0%2B-informational?logo=android)](https://apilevels.com/#:~:text=Jetpack%20Compose%20requires%20a%20minSdk%20of%2021%20or%20higher)
[![GitHub](https://img.shields.io/github/license/mytv-android/mytv-android)](https://github.com/mytv-android/mytv-android)

</div>
    <p>基于天光云影3.3.7，使用Android原生开发的电视直播软件</p>

<img src="./screenshots/Screenshot_dashboard.png" width="96%"/>
<br/>
<img src="./screenshots/Screenshot_channels.png" width="48%"/>
<img src="./screenshots/Screenshot_search.png" width="48%"/>
</div>

## 使用

### 操作方式

> 遥控器操作方式与主流电视直播软件类似；

- 频道切换：使用上下方向键，或者数字键切换频道；屏幕上下滑动；
- 频道选择：OK键；单击屏幕；
- 线路切换：使用左右方向键；屏幕左右滑动；
- 设置页面：按下菜单、帮助键，长按OK键；双击、长按屏幕；

### 触摸键位对应

- 方向键：屏幕上下左右滑动
- OK键：点击屏幕
- 长按OK键：长按屏幕
- 菜单、帮助键：双击屏幕

### 自定义设置

- 访问以下网址：`http://<设备IP>:10481`

## 下载

可以通过右侧release进行下载或拉取代码到本地进行编译

## 说明

- 仅支持Android5及以上
- 只在自家电视上测过，其他电视稳定性未知

## 功能计划

1.混合源添加除央视网和央视频外的28个地区电视台官网，内置源失效仍然可以观看；

2.补全设置中—网络菜单的未开发功能，支持自定义重试时间和重放次数；

3.新增左右手势和遥控器左右按键切换播放源开关，防止老年人误触；

4.新增电视频道的收藏列表的隐藏和显示开关；

5.m3u播放源支持Referer请求头参数http-referer=""；

6.m3u文件支持混合使用webview://http://xxxx；

7.优化WebView植入JS脚本逻辑，提高获取效率，修复部分网址不全屏；

8.新增频道列表跨组切换，当前分组到底后跳转下一个分组；

9.新增播放线路按延迟排序，自动播放最优线路。

10.新增统一播放音量均衡、响度归一化。

11.m3u直播源文件支持自定义执行js脚本参数，便于播放某些需要点击选择的页面
