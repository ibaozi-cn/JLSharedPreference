# JLSharedPreference
对腾讯MMKV的包使用，方便后期无缝切换

#JLSharedPreference

## MMKV 源起
在微信客户端的日常运营中，时不时就会爆发特殊文字引起系统的 crash，[参考文章](https://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=2649286826&idx=1&sn=35601cb1156617aa235b7fd4b085bfc4)，文章里面设计的技术方案是在关键代码前后进行计数器的加减，通过检查计数器的异常，来发现引起闪退的异常文字。在会话列表、会话界面等有大量 cell 的地方，希望新加的计时器不会影响滑动性能；另外这些计数器还要永久存储下来——因为闪退随时可能发生。这就需要一个性能非常高的通用 key-value 存储组件，我们考察了 SharedPreferences、NSUserDefaults、SQLite 等常见组件，发现都没能满足如此苛刻的性能要求。考虑到这个防 crash 方案最主要的诉求还是实时写入，而 mmap 内存映射文件刚好满足这种需求，我们尝试通过它来实现一套 key-value 组件。

## MMKV 原理
* **内存准备**  
通过 mmap 内存映射文件，提供一段可供随时写入的内存块，App 只管往里面写数据，由操作系统负责将内存回写到文件，不必担心 crash 导致数据丢失。
* **数据组织**  
数据序列化方面我们选用 protobuf 协议，pb 在性能和空间占用上都有不错的表现。
* **写入优化**  
考虑到主要使用场景是频繁地进行写入更新，我们需要有增量更新的能力。我们考虑将增量 kv 对象序列化后，append 到内存末尾。
* **空间增长**  
使用 append 实现增量更新带来了一个新的问题，就是不断 append 的话，文件大小会增长得不可控。我们需要在性能和空间上做个折中。

更详细的设计原理参考 [MMKV 原理](https://github.com/Tencent/MMKV/wiki/design)。

MMKV 支持**多进程访问**，更详细的用法参考 [Android Tutorial](https://github.com/Tencent/MMKV/wiki/android_tutorial_cn)。

### 性能对比
循环写入随机的`int` 1k 次，我们有如下性能对比：  
![](https://github.com/Tencent/MMKV/wiki/assets/profile_android_mini.jpg)  
更详细的性能对比参考 [Android Benchmark](https://github.com/Tencent/MMKV/wiki/android_benchmark_cn)。

## 例子
        ```
        
        //step 1  初始化
        JLSharedPreference.initWithContext(this)
        Log.e("JLSharedPreference", "JLSharedPreference  init")
        
        //step 2  迁移
        val sharedPreferences = getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE)
        val isSuccessSize = JLSharedPreference.defaultSp().importFromSharedPreferences(sharedPreferences)
        
        //step 3 打印迁移日志
        var str = ""
        JLSharedPreference.defaultSp().allKeys().forEach {
            str += it
            str += " "
            str +=  JLSharedPreference.defaultSp().getString(it, " ")
            str += "\n"
        }
        Log.e("JLSharedPreference", "JLSharedPreference  content:$str")
        if (isSuccessSize > 0) {
            Log.e("JLSharedPreference", "sharedPreferences  size:${sharedPreferences.all.size}")
            Log.e("JLSharedPreference", "JLSharedPreference  isSuccess size:$isSuccessSize")
        }
        ```