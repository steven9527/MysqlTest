mysql读写分离

主要参考地址：
https://www.jianshu.com/p/2222257f96d3(第四种模式)


原理很简单：
1 ApplicationContext.xml 中定义两个数据源，一个读，一个写
2 新增一个DynamicDataSource,定义在ApplicationContext.xml里，它会动态去选择上面的两个数据源
3 新增一个mybatis的拦截器 F:\example\java\MysqlTest\app\src\main\java\com\steven\mysql\interceptor\DynamicPlugin.java
 该拦截器根据mybatis底层的CommandType决定采用读的数据源还是写的数据源，如果commandType不清晰的，根据sql语句去判断。
4 如何确认读写分离生效了？
参考本文：https://blog.csdn.net/qidaif/article/details/80931703

a 登录到两台服务器上，具体登录密码都在db.properties里
b 开启服务器上的sql语句监听
c 同时本地开启客户端，连接到两个数据库
d 执行 localhost:8080/add,将会执行写入操作。可看到 主数据库里有相应记录，从数据库里暂时没有（因为设置了同步间隔为30s）。
同时，在主linux服务器上，可监听到插入语句，从linux服务器上，没有插入语句；30s后，从数据库里也可以查询到相应记录了，说明同步成功
e 执行 localhost:8080/list,将会执行读操作。
此时，在主linux服务器上，监听不到查询语句；在从linux服务器上，可以监听到查询语句，证明，读的操作是在从服务器上执行的



