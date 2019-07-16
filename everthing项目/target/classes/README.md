仿照Everything桌面工具，基于Java语言开发的命令行搜索文件
###2.背景

有时候在Windows命令行下需要查询一些文件，由于‘for'命令并不如Linux下的’find‘好用。

###3.意义
- 解决windows命令行下文件的搜索问题
- 基于Java开发的工具可以在Windows和Linux平台上无差异使用
- 锻炼编码能力

###4.技术
- JavaSE
- Java多线程，线程池
- JDBC编程
- 嵌入式数据库H2
- Apache Commons IO库
- 接口模式，设计模式。
- 插件Lombok

###5.实现
- 从上到下分层结构  客户端 -> 统一调度器 -> 索引，检索，监控，拦截器->数据库访问->数据库
- 绘图展示，文字描述
###6.使用
- 程序的发布包：everything-g1.zip
    - lib(依赖的库)
    - everything_g1-v1.0.0.jar
    -everything_config.properties
    
- 解压程序发布包
- 参考如下说明，配置文件

最大检索返回的结果数

everything.max_return=30，

是否开启构建索引

everything_enable_build_index=false

检索时depth深度的排序规则

everything.order_by_desc=false

文件监控的间隔时间

everything.interval=60000

索引目录

索引包含目录

everything.handle_path.include_path=C:\\;D:\\;E:\\

索引排除目录

everything.handle_path.exclude_path=C://Windows   
 
 - 启动程序
 
 java -jar everything-g1-v1.0.0.jar [配置文件的路径]
 
 ### 7.总结
 - 学到什么
 - 有什么提升
 - 有什么困难，怎么思考，如何解决
 - 测试结构
 ### 8.扩展
 - 功能扩展：history
 - 根据拼音检索
 - JRE包含在程序的发布包（bat,sh脚本）
 