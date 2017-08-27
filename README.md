# web-springboot
web-springboot
想去构建一个简单或者复杂的web项目。

描述：当前项目中使用yml格式的文件配置格式，支持jsp和html两种页面格式。
多数据库支持（现只有两个）

# thymeleaf
1. 自动部署加载
spring.thymeleaf.cache=false
如果使用的开发工具是idea,修改html后使用Ctrl+Shift+F9

2. mybatis的逆向工程
有个插件叫做MyBatisCodeHelper（需要自己先去定义po）
现在我使用的是配置文件的形式
ps: war打包时请不能使用插件形式（打包时需要把逆向工程的插件注释掉）
使用Lifecycle去打包，maven的插件不能编译（具体未研究）
3. tomcat需要8.0以上版本

4. 打包过滤器里面不能使用外部的变量（ps：这个项目，加入配置url的例外，一旦引用进来tomcat就启动不起来）

5. linux centOS7 上面的tomcat的项目配置使用的是相对路径（绝对路径不生效）


# 待办事项
待操作：
2. 分页插件 公共异常
9. 页面编写
5. CentOS 7 64位的服务器配置 java tomcat ftp 数据库
7. 购买域名搭建微信平台

1. 数据库单例一个
3. 另一种文件读取形式
4. 数据库动态切换
8. 微信小程序

# 项目问题
1.  调用方法返回页面时，过滤器接到的css、js等的路径不对