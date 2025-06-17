<div align="center">
    <h1>电视直播<sup>TV</sup></h1>
<div align="center">

<p align="right">
  <a href="README.md">🇨🇳 中文</a> | <a href="README_EN.md">🇺🇸 English</a>
</p>

![GitHub Repo stars](https://img.shields.io/github/stars/mytv-android/mytv-android)
![GitHub all releases](https://img.shields.io/github/downloads/mytv-android/mytv-android/total)
[![Android Sdk Require](https://img.shields.io/badge/Android-5.0%2B-informational?logo=android)](https://apilevels.com/#:~:text=Jetpack%20Compose%20requires%20a%20minSdk%20of%2021%20or%20higher)
[![GitHub](https://img.shields.io/github/license/mytv-android/mytv-android)](https://github.com/mytv-android/mytv-android)

</div>
    <p>基于天光云影3.3.9，使用Android原生开发的电视直播软件</p>

<!-- <img src="./screenshots/Screenshot_dashboard.png" width="96%"/> -->
<br/>
<!-- <img src="./screenshots/Screenshot_channels.png" width="48%"/>
<img src="./screenshots/Screenshot_search.png" width="48%"/> -->
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

- 访问以下网址：`http://<设备IP>:10591`

## 下载

可以通过右侧release进行下载或拉取代码到本地进行编译

## 说明

- 仅支持Android5及以上
- 只在自家电视上测过，其他电视稳定性未知

## 功能计划

1.混合源添加除央视网和央视频外的28个地区电视台官网，内置源失效仍然可以观看；

2.补全设置中—网络菜单的未开发功能，支持自定义重试时间和重放次数；

3.新增左右手势和遥控器左右按键切换订阅源开关，防止老年人误触；

4.新增电视频道的收藏列表的隐藏和显示开关；

5.m3u订阅源支持Referer请求头参数http-referer=""；

6.m3u文件支持混合使用webview://http://xxxx；

7.优化WebView植入JS脚本逻辑，提高获取效率，修复部分网址不全屏；

8.新增频道列表跨组切换，当前分组到底后跳转下一个分组；



## 信息获取

交流（此群一般不作为反馈bug，提供建议之处，相关内容请发布至issue）和测试版信息发布，都请关注此频道以获取最新改进，以及更新投票计划。

<div align="center">
    <img src="./img/QRCode.png" width="48%"/>
</div>

## 星标历史

<a href="https://www.star-history.com/#mytv-android/mytv-android&Date">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=mytv-android/mytv-android&type=Date&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=mytv-android/mytv-android&type=Date" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=mytv-android/mytv-android&type=Date" />
 </picture>
</a>

[![Stargazers over time](https://starchart.cc/mytv-android/mytv-android.svg?variant=adaptive)](https://starchart.cc/mytv-android/mytv-android)


## 著作权、许可证声明和致谢

- 本软件基于天光云影（https://github.com/yaoxieyoulei/mytv-android/tree/feature/ui ）进行迭代，在此感谢作者 yaoxieyoulei 的无私奉献。天光云影使用的MIT许可证请参见[天光云影许可证](./LICENSE_ORIGIN)，当你复制软件代码时，请保留此许可证和原作者版权声明。

- 本软件使用的GNU许可证请参见[本项目许可证](./LICENSE)，你可以自由地分发和衍生本软件。但当你基于本软件代码进行分发和演绎时，你不能修改许可证；你需要公开修改后的源代码；你也需要保留本软件的相关声明。

- 本软件还使用了BV（https://github.com/aaa1115910/bv ）的部分代码，在此特感谢aaa1115910。[许可证](./LICENSE_PART1)。