<div align="center">
    <h1>电视直播<sup>TV</sup></h1>
<div align="center">

<p align="right">
  <a href="README.md">🇨🇳 中文</a> | <a href="README_EN.md">🇺🇸 English</a>
</p>

![GitHub Repo stars](https://img.shields.io/github/stars/mytv-android/mytv-android)
![GitHub all releases](https://img.shields.io/github/downloads/mytv-android/mytv-android/total)
[![Android Sdk Require](https://img.shields.io/badge/Android-6.0%2B-informational?logo=android)](https://apilevels.com/#:~:text=Jetpack%20Compose%20requires%20a%20minSdk%20of%2023%20or%20higher)
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

### 常见问题解答
#### 我的源在本播放器上播放不了/起播太慢，但我确认源是没有问题的

- 播放不了尝试打开``设置/播放器/更好的视频探测``，起播太慢尝试关闭``设置/播放器/更好的视频探测``
- 对于使用``rtp2httpd``转发IPTV的用户，对于支持fcc的源，优先使用``Media3``和``VLC``播放器以获得快速的切台体验
- 对于使用``rtsp``源的用户，请勿使用Media3播放器，你可以通过在远程面板添加``rtsp://.*``规则并指定`IJK`或``VLC``播放器播放
#### 远程面板扫码二维码识别较差
目前发现苹果自带二维码工具可能出现此问题，建议使用微信扫码
#### 我在播放时遇到播放器提示错误，但我希望进一步了解有关信息
请至``网页面板/调试 导出Logcat``以查看。建议错误发生后立即导出，否则此日志容易被后面的日志输出所覆盖
## 下载

可以通过右侧release进行下载

## 姊妹项目
- [北京卫视订阅源] (https://github.com/mytv-android/BRTV-Live-M3U8)
- [部分地方台订阅源] (https://github.com/mytv-android/China-TV-Live-M3U8)
- [节目单项目] (https://github.com/mytv-android/myEPG)
- [频道标志项目] (https://github.com/mytv-android/myTVlogo)
- [JS订阅源] (https://github.com/mytv-android/mytvJS)
- [migu订阅源项目] (https://github.com/mytv-android/myMIGU)

## 相关项目
- [rtphttpd - IPTV转发工具] (https://github.com/stackia/rtp2httpd)

## 参与开发

由于项目开源引来大量的分发（Star:Fork 比例为 5:1），且这些分发者不能自行解决其渠道用户的反馈，项目目前将不再公开源代码。

不过，如果你有兴趣，仍然可以申请参与开发，我们在组织内部维护了一个分支，所有拥有访问权限的开发者都可以查看、编辑和提交。

你可以通过issue来联系我们。

## 信息获取

交流（此群一般不作为反馈bug，提供建议之处，相关内容请发布至issue），都请关注此频道以获取最新改进，以及更新投票计划。

<div align="center">
    <img src="./img/QRCode.png" width="48%"/>
</div>

## 星标历史

<a href="https://star-history.dera.page/#mytv-android/mytv-android&Date">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://star-history.dera.page/svg?repos=mytv-android/mytv-android&type=Date&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://star-history.dera.page/svg?repos=mytv-android/mytv-android&type=Date" />
   <img alt="Star History Chart" src="https://star-history.dera.page/svg?repos=mytv-android/mytv-android&type=Date" />
 </picture>
</a>

[![Stargazers over time](https://starchart.cc/mytv-android/mytv-android.svg?variant=adaptive)](https://starchart.cc/mytv-android/mytv-android)


## 著作权、许可证声明和致谢

- 本软件基于天光云影（https://github.com/yaoxieyoulei/mytv-android/tree/feature/ui ）进行迭代，在此感谢作者 yaoxieyoulei 的无私奉献。天光云影使用的MIT许可证请参见[天光云影许可证](./LICENSE_ORIGIN)。

- 本软件还使用了BV（https://github.com/aaa1115910/bv ）的部分代码，在此特感谢aaa1115910。[许可证](./LICENSE_PART1)。
