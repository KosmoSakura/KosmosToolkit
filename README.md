# KosmosUtils
> 这个是我的工具和笔记库,这里有不少伪代码(笔记)，以及一下日常踩坑记录。<br/>
> 项目是不能够直接跑起来的，有时间我会把`Utils`部分部署成jitpack依赖<br/>
> [详情见我的博客](https://blog.csdn.net/zull_kos_mos)

---

#### 1.[Glide](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/glide)→相关工具

| 分类         | 功能              |
| ------------ | ----------------- |
| UGlideSimple | Glide普通使用     |
| UGlideDisk   | Glide内存磁盘方面 |
| UGlideEffect | Glide特效方面     |



#### 2.[USnackbar](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/snackbar)→可以从上面弹出的SB

> 基于：[MySnackBar](https://github.com/guoyoujin/MySnackBar)

| 方法             | 描述                 | 封装               |
| ---------------- | -------------------- | ------------------ |
| showNormal       | 显示顶部普通通知     | 传入Contex代替View |
| showWarning      | 显示顶部警告通知     |                    |
| showError        | 显示顶部错误通知     |                    |
| showProgress     | 显示顶部带进度条通知 |                    |
| showNormalDown   | 显示底部普通通知     |                    |
| showWarningDown  | 显示底部警告通知     |                    |
| showErrorDown    | 显示底部错误通知     |                    |
| showProgressDown | 显示底部带进度条通知 |                    |
| showNoAction     | 显示不带action的通知 |                    |
| showNoColor      | 显示默认颜色的通知   |                    |


#### 3.[UToast](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/toasts)

> 基于鸿洋牌的[ToastUtils](https://github.com/getActivity/ToastUtils)封装

#### 4.[UColor](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UColor.java)→颜色工具类

| 方法                            | 描述                        |
| ------------------------------- | --------------------------- |
| ColorFilter                     | 颜色反转                    |
| RgbToInt(int , int , int )      | 将RGB颜色转化为int          |
| RgbToInt(int , int , int, int ) | 将RGB颜色转化为int 带透明度 |
| IntToRgb                        | int颜色转化为RGB            |
| HexFromColor                    | Color对象转换成字符串       |



#### 5.[UDate](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UDate.java)

> 时间日期处理,以及平时在处理日期时的踩坑修复


| 方法（重载）        | 描述                                                         |
| ------------------- | ------------------------------------------------------------ |
| 踩坑记录            | 比如：[关于Calendar获取星期的天数踩坑记录](https://blog.csdn.net/zull_kos_mos/article/details/83934581) |
| Safety              | 其中带`Safety`字样的是处理过异常判断的安全返回               |
| strToStrSafety      | `strToStrSafety`:一种格式的时间 转换另一个 格式的时间        |
| longToReadEasy      | `longToReadEasy`:目标时间和现在时间的长度(返回：14年2月1天11小时9分钟) |
| dateToNow           | `dateToNow`:时间格式化(返回：几分钟前、几小时前、几天前、几月前、几年前) |
| dateToSimpleStr     | `dateToSimpleStr`:某个时间段和现在时间的简要写法(eg:2018-10-05 15:30 至 15:40) |
| getAgeSafety        | `getAgeSafety`:通过生日（String、Date）返回年龄              |
| getMondayOfDate     | `getMondayOfDate`:返回指定日期所在的周一的日期               |
| getSundayOfDate     | `getSundayOfDate`:返回指定日期所在的周七的日期               |
| getDayOfWeek        | ` getDayOfWeek`:指定日期 所在周的星期                        |
| getAnotherMonthDate | `getAnotherMonthDate(boolean future, int x)`、`getAnotherYearDate(boolean future, int x) `...:返回指定日期 前后x个单位的日期 |



#### 6.[UKeyboard](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UKeyboard.java)→键盘输入工具类

| 方法           | 描述                         |
| -------------- | ---------------------------- |
| isKeyboardShow | 是否显示软件盘               |
| openOrhide     | 已经显示，则隐藏，反之则显示 |
| showForce      | 强制显示键盘                 |
| hideForce      | 强制隐藏键盘                 |


#### 7.[ULog](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/ULog.java)

> 基于[Blankj](https://github.com/Blankj/AndroidUtilCode)的工具重构

| 方法       | 描述                |
| ---------- | ------------------- |
| commonD    | 普通打印Debug       |
| commonV    | 普通打印Verbose     |
| 。。。。。 | 略                  |
| d          | 打印带边框的Debug   |
| v          | 打印带边框的Verbose |
| 。。。。。 | 略                  |



#### 8.[URegular](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/URegular.java)→正则表达式相关工具

| 方法                   | 描述                                  |
| ---------------------- | ------------------------------------- |
| emojiFilters           | 禁止输入表情                          |
| checkPhoneNum          | 使用正则表达式检查手机号码            |
| checkSign              | 使用正则表达式检查标点                |
| checkChineseCharacters | 校验纯汉字                            |
| checkBankCard          | 校验银行卡卡号                        |
| checkPassword_3        | 密码：必须包含 数字,字母,符号 3项组合 |
| checkPassword          | 密码：只能包含字母（大小写）和数字    |
| checkNumber            | 6位纯数字                             |
| checkIdCard            | 验证身份证号码                        |
| checkEmail             | 验证邮箱                              |
| formPhoneNo            | 隐藏手机号中间4位                     |
| formIDCardNo           | 隐藏身份证号中间N位                   |
| formBankCard           | 银行卡号每隔四位增加一个空格          |

#### 9.[USP](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/URegular.java)→SharedPreferences工具

| 方法                  | 描述                       |
| --------------------- | -------------------------- |
| put(String , Object ) | 存入Object，自动判断类型   |
| get(String , Object ) | 取出Object，，自动判断类型 |
| remove(String )       | 删除某一key对应的值        |
| clear                 | 清空SharedPreferences      |
| contain               | 查询某个key是否存在        |
| getAll                | 返回所有的键值对           |
| getString             | 获取String类型             |
| putString             | 取出String类型             |
| 类似的略              | 类似的略                   |



#### 10.[UText](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UText.java)→文字、字符相关工具

| 方法                                 | 描述                           | 重载传入                                                     |
| ------------------------------------ | ------------------------------ | ------------------------------------------------------------ |
| isNull（Object）                     | 为空返回安全类型               | TextView、String、Integer、Editable、CharSequence、Long、Float、Boolean |
| isNull（Object ，defValue）          | 为空返回指定类型               | TextView、String、Integer、Editable、CharSequence、Long、Float、Boolean |
| isEmpty（Object）                    | 判断是否为空                   | List、Z[]、String、TextView（判断是否有文字）                |
| isSpace（String）                    | 判断字符串是否为null或全为空格 |                                                              |
| clearNullOfList                      | 递归：移除list中为null的元素   | -                                                            |
| lowerFirstLetter                     | 返回：首字母小写字符串         |                                                              |
| equals（CharSequence，CharSequence） | 判断两字符是否相等             | {@code true}: 相等<br>{@code false}: 不相等                  |
| getTextUnderLine                     | 给字符添加下划线               | TextView、String                                             |
| getTextBold                          | 文本加粗                       |                                                              |
| getTextItalic                        | 文本斜体                       |                                                              |
| getSpannableString                   | 动态设置字符串的颜色和大小     | (TextView t, int color, int startLocation, int endLocation, float large) |
| getFileName                          | 获取文件名                     |                                                              |
| getFileSuffix                        | 获取后缀名                     |                                                              |



#### 11.[UUnit](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UUnit.java)→单位换算工具类

> 单位换算：像素<->dp、摄氏度<->华氏度、字符串版本号换算、字节换算.

| 方法                             | 描述                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| sizeFormatbit                    | 格式化字节单位（返回：XGB）                                  |
| compareVersion(String , String ) | 比较版本号的大小, 前者大则返回一个正数, 后者大返回一个负数, 相等则返回0 |
| px2dp                            | 像素转dp                                                     |
| dp2px                            | 反过来                                                       |
| cToF                             | 摄氏度转华氏度                                               |
| fToC                             | 反过来                                                       |

#### 12.[UGPS](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/tool/UGPS.java)→GPS工具

|方法名   | 描述   |
| ----------- | ----------- |
| isGPSOpne | GPS是否打开 |
| openGPSSettings | 跳转GPS设置界面 |

#### 13.[UWiFi](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/tool/UWiFi.java)→wifi工具类

|方法名   | 描述   |
| ----------- | ----------- |
| getSSIDWithManager | 通过WifiManager获取ssid（需要位置权限） |
| getSSID | 通过ConnectivityManager获取ssid（不需要位置权限） |
|toLinkedWifi | 打开wifi并连接，常规情况 |
|toLinkedWifi | 打开wifi并连接，区分3种加密方式|
| jumpToWifiSetting | 跳转到wifi设置页面|

#### 14.[UAp](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/tool/UAp.java)→wifi热点工具类

|    方法名  | 描述  |
| ------------- | -------------------- |
| getApSSID | 获取wifi热点的ssid （通过反射）  |
|getApPassword|wifi热点的password（通过反射）|
|getApState|获取热点状态（通过反射）|
|getApOpen|热点是否已经打开（通过反射）|
|closeAp|关闭热点（通过反射）|
|openAp|打开热点(解决Android 8.0反射失效的情况）|
|isSystemO|当前系统是否高于8.0|
#### 15.[USensor ](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/USensor.java)→指南针辅助类：磁场放、方向等传感器工具

> 指南针功能辅助类

| 方法                                     | 描述                             |
| ---------------------------------------- | -------------------------------- |
| getSensors                               | 获取设备支持的传感器列表         |
| setDegreeListener                        | 设置传感监听                     |
| clear                                    | 资源清理，注销监听   |
| private static final int sensitivity= 6; | 变化灵敏度，数值越小，变化月灵敏 |

#### 16.[UPermissions](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UPermissions.java)→权限工具

> 基于RxPermissions

| 方法                               | 说明                      |
| ---------------------------------- | ------------------------- |
| clear                              | 资源清理                  |
| check(String ,Listener,String... ) | 权限检测,操作逻辑内部处理 |
| toGoSystem                         | 跳转系统授权页面          |

### 16.[UReflex](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UReflex.java)→反射辅助工具

| 方法               | 描述                                 |
| ------------------ | ------------------------------------ |
| getPrivateFirldStr | 获取公共属性的值(比如是String类型 ） |
| getPicMethod       | 获取公共方法的值(比如是String类型 ） |
| getPrtFirld        | 获取私有属性的值(比如是String类型 ） |
| getPicFirld        | 获取公共属性的值(比如是String类型 ） |
| getAllField        | 获取某类的全部属性                   |
| getAllMethods      | 获取某类的全部方法                   |
| getSuperClass      | 获取某类的父类                       |
| getClassBase       | 通过包名+类名获取到类对象            |

### 17.[UFlashLight](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UFlashLight.java)→闪光灯工具

| 方法                       | 说明                              |
| -------------------------- | --------------------------------- |
| screenLight（float alpha） | 修改屏幕亮度                      |
| light（boolean）           | true→开启闪光灯，false→关闭闪光灯 |
| openFlash                  | 开启闪光灯                        |
| closeFlash                 | 关闭闪光灯                        |

### 18.[USound](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/USound.java)→音频池工具

| 方法                       | 说明                                 |
| -------------------------- | ------------------------------------ |
| instance                   | 异步                                 |
| load(Context , int... res) | 数据加载方法，res为要被加载的raw资源 |
| play（index）              | 播放                                 |

### 18.[UApps](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/utils/UApps.java)→[应用程序工具包](https://blog.csdn.net/zull_kos_mos/article/details/84958669)

| 对外提供的方法           | 说明                                               |
| ------------------------ | -------------------------------------------------- |
| getAppList()             | 获取系统所有安装应用（应用区分系统非系统：sysApp） |
| loadUsersApp()           | 获取手机内非系统应用                               |
| getAppByPkgName()        | 通过包名获取该应用的相关信息                       |
| getShareApps()           | 查询手机内所有支持分享的应用                       |
| startThridApp()          | 通过包名启动第三方app                              |
| getSystemLanguageList()  | 获取当前系统上的语言列表(Locale列表)               |
| getAppSize()             | 获取应用数据大小                                   |
| installExternalStorage() | 应用是否安装在外置储存空间                         |
| isSystemApp()            | 是否是系统应用                                     |
| getCurTopAppPkg()        | 轻量： 获取栈顶应用包名(非系统程序在在栈顶时有效)  |
| getTopAppPkg()           | 获取栈顶应用包名（所有程序有效）                   |
| getLauncherPkgName()     | 获取当前正在运行的桌面launcher包名                 |



---

## 其他

#### 0.androidx

> androidx的踩坑记录

#### 1.[UImage](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/tool/UImage.java):图片工具

> 1.基于Retrofit2图片下载
> 2.2种方式设置壁纸
> 3.glide4.8获取bmp

#### 2.[pager_tab_frag](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/ui/pager_tab_frag)包

> TableLayout+ViewPager+Fragment使用示例

#### 3.[MainActivity](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/ui/MainActivity.java)

> RecyclerView瀑布流示例

#### 4.[mvp](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/mvp)包

> 基于Retrofit2，rxJava的mvp示例

#### 5.[dao](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/src/main/java/cos/mos/utils/)包

> greendao3.2数据库升级示例

#### 6.[混淆踩坑配置](https://github.com/KosmoSakura/KosmosUtils/blob/master/app/proguard-rules.pro)

#### 7.[retrofit](https://github.com/KosmoSakura/KosmosUtils/blob/master/kosmoslibrary/src/main/java/cos/mos/library/retrofit)包

> Retrofit2网络请求，文件下载示例
>
> 及相关的踩坑记录，比如：[在Retrofit2网络请求时打印参数的踩坑记录](https://blog.csdn.net/zull_kos_mos/article/details/83934732)

#### 8.[封装EventBus]()

---

## License

```
   Copyright 2017 Kosmos

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
