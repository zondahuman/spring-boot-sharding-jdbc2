# springboot-shardingjdbc
使用spring boot+sharding-jdbc+mybatis 实现分库分表


数据分片
http://shardingsphere.io/document/current/cn/manual/sharding-jdbc/usage/sharding/


spring-boot-starter-actuator这个库让我们可以访问应用的很多信息，包括：/env、/info、/metrics、/health等。
现在运行程序，然后在浏览器中访问：http://localhost:8080/health






订单表逻辑语句：
CREATE TABLE IF NOT EXISTS t_order (order_id INT NOT NULL, user_id INT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id))

订单项逻辑语句：
CREATE TABLE IF NOT EXISTS t_order_item (item_id INT NOT NULL, order_id INT NOT NULL, user_id INT NOT NULL, PRIMARY KEY (item_id))









分库分表，读写分离配置官方
http://shardingsphere.io/document/current/en/manual/sharding-jdbc/configuration/config-java/

















