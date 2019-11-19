# 本项目是一个电商项目。<br>开发工具：linux，tomcat,mysql,idea<br>开发技术：dubbo，springboot，mybatis，zookeeper,elasticsearch，redis，activemq等
## 后台模块(manager)
三级分类查询<br>
平台属性增删改查<br>
上传spu（spu信息，图片，销售属性，销售属性值），图片存储在fastdfs，链接存储在数据库<br>
上传spu下的sku（sku信息，选择spu中的图片，关联spu中的属性，关联平台属性中的属性）<br>
## 详情模块(item)
查询sku信息传递给前端页面<br>
查询sku对应spu下的销售属性以及属性值传递给前端页面<br>
查询spu下的所有sku信息，生成每个skuid与销售属性组成的键值对，再将所有键值对转化成字符串传递给前端页面，前端页面将字符串转回map，当选择属性时生成销售属性组成的字符串查询出skuid，跳转到该skuid详情。<br>
## 搜索模块(search)
在kibana中新增搜索规则。<br>
将mysql中的数据同步到elasticsearch。<br>
通过三级分类id,搜索sku。<br>
通过关键字,利用ik分词器搜素sku。<br>
将搜索页面sku的全部平台属性值提取出来，根据平台属性值生成平台属性和属性值组成类型的集合A，将集合传递给前端页面生成属性框并生成一个面包屑集合B。<br>
当点击属性框时，集合B添加该属性值以及复制链接,并以集合B属性集合作为检索条件重新进行检索。<br>
当点击面包屑时，删除面包屑上对应的属性值，更改链接重新检索。<br>
## 购物车模块(cart)
未登入时将订单信息存入cookie，如果cookie中有该商品了就在数量上进行增加，如果cookie没有该商品就在cookie中新增<br>
登入时将cookie中的数据合并到数据库，同时写入redis。<br>
## 订单模块(order)
从数据库查询用户的订单信息。<br>
购物车结算时将选中商品展现，查询收货地址展现以及计算总金额。为了防止在多线程时订单重复提交的问题，如果redis没有交易码可以提交并生成交易码，如果有交易码就不能提交。<br>
提交订单生成一个订单信息存入数据库。<br>
## 用户模块(user)
获取用户信息和关联的收货地址信息<br>
## 登录模块(passport)
单点登入：从数据库获取用户信息用jwt算法进行加密生成token存入cookie。<br>
社交登入：本项目采用微博登入，点击登入跳转到微博登入授权页面，用户授权返回一个授权码code,用code和秘钥向微博换取access_token,再用access_token换取用户信息，将用户信息加密生成token存入cookie并将用户信息存入数据库。<br>
验证中心：由于一些模块需要用户登入才能访问，如购物车（登入不登入都能访问），结算，订单，支付。本项目采用一个注解式拦截器对这些模块进行拦截同时在注解上设置一个参数判断是否必须登入才能访问。当拦截到请求时获取请求路径returnUrl并从cookie中获取token进行解密检查用户信息是否存在，如果存在说明已经登入，放行。如果不存在说明没有登入，将用户打回登入界面并传递returnUrl当登入成功时跳转到returnurl（购物车即使没有登入也能访问，如果登入会获取用户信息）。<br>
## 支付模块(payment)
本项目调用支付宝支付接口。配置alipayClient信息：支付宝公钥，回调链接，私钥，appid等。<br>
提交订单时从数据库获取订单信息，通过alipayClient写入订单信息生成表单同时生成支付信息存入数据库。<br>
对于更新支付状态有两种方式。<br>
第一种等待支付宝回调，有时候可能无法及时回调。<br>
第二种是主动查询，在生成支付表单单时用activemq发送一个延时消息（每10秒发送一次，发送5次），在服务端接收到消息后检查支付状态，如果支付成功就会更新支付信息。同时可以实现分布式事务。<br>
## 秒杀模块(seckill)
做了一个使用redis和redission的一个压力测试<br>
