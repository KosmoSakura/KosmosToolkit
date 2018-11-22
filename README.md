# KosmosUtils
> Kosmos的工具库,因为我比较懒，所以折腾了这个东西，
> 把我平时在用的工具们抽取了出来，方便以后直接include进来

#### 
---

#### 0.androidx
> androidx的踩坑记录

#### 1.UImage

> 1.基于Retrofit2图片下载
> 2.2种方式设置壁纸
> 3.glide4.8获取bmp

#### 2.cos.mos.utils.ui.pager_tab_frag包

> TableLayout+ViewPager+Fragment使用示例

#### 3.cos.mos.utils.uiMainActivity

> RecyclerView瀑布流示例

#### 4.cos.mos.utils.mvp包

> 基于Retrofit2，rxJava的mvp示例

#### 5.cos.mos.utils.dao

> greendao3.2数据库升级示例

#### 6.混淆踩坑配置

#### 7.cos.mos.library.retrofit包

> Retrofit2网络请求，文件下载示例
>
> 及相关的踩坑记录，比如：[在Retrofit2网络请求时打印参数的踩坑记录](https://blog.csdn.net/zull_kos_mos/article/details/83934732)

#### 8.Glide相关工具

> - 1.UGlideDisk:Glide内存磁盘方面
> - 2.UGlideEffect:Glide特效方面
> - 3.UGlideSimple:Glide普通使用

#### 9.USnackbar

> 可以从上面弹出的SB
>
> 基于：[MySnackBar](https://github.com/guoyoujin/MySnackBar)

#### 10.UToast

> 基于鸿洋牌的[ToastUtils](https://github.com/getActivity/ToastUtils)封装

#### 11.UColor

> 颜色工具类

#### 12.UDate

> 时间日期处理,以及平时在处理日期时的踩坑修复
>
> - 1.比如：[关于Calendar获取星期的天数踩坑记录](https://blog.csdn.net/zull_kos_mos/article/details/83934581)
> - 2.其中带`Safety`字样的是处理过异常判断的安全返回
> - 3.`strToStrSafety`:一种格式的时间 转换另一个 格式的时间
> - 4.`longToReadEasy`:目标时间和现在时间的长度(返回：14年2月1天11小时9分钟)
> - 5.`dateToNow`:时间格式化(返回：几分钟前、几小时前、几天前、几月前、几年前)
> - 6.`dateToSimpleStr`:某个时间段和现在时间的简要写法(eg:2018-10-05 15:30 至 15:40)
> - 7.`getAgeSafety`:通过生日（String、Date）返回年龄
> - 8.`getMondayOfDate`:返回指定日期所在的周一的日期
> - 9.`getSundayOfDate`:返回指定日期所在的周七的日期
> - 10.`getDayOfWeek`:指定日期 所在周的星期
> - 11.`getAnotherMonthDate(boolean future, int x)`、`getAnotherYearDate(boolean future, int x) `...:返回指定日期 前后x个单位的日期

#### 13.UKeyboard

> 键盘输入工具类

#### 14.ULog

> 基于[Blankj](https://github.com/Blankj/AndroidUtilCode)的工具封装

#### 15.URegular

> 正则表达式相关工具

#### 16.USP

> SharedPreferences工具

#### 17.UText

> 文字相关工具

#### 18.UUnit

> 单位换算：像素<->dp、摄氏度<->华氏度、字符串版本号换算、字节换算.

#### 19.封装EventBus

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
