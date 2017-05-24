## 【Api文档】Api列表 - v3.7
> 按页索引 Api 列表

#### 1. url：/api/doc/list.do

#### 2. 请求方式：POST

#### 3. 请求参数
|参数 Key|参数名称|参数类型|长度上限|是否必填|说明|
|:-----------|:-----------|:---------|:---------|:---------|:-----------|
|page|页数|Integer|0|true|当前页数|
|pageSize|每页数量|Integer|0|true|默认20|

#### 4. 请求实例
```JSON
{"page":1, "pageSize":20}
```

#### 5. 主要返回内容
|参数 Key|参数名称|参数类型|说明|
|:-----------|:-----------|:---------|:---------|:---------|:-----------|
|id|Api 文档主键|Integer||

#### 6. 错误Code
|Code|内容|
|:-----------|:-----------|
|||

#### 7. 返回实例
```JSON
{"msg":"操作成功","ret":[{"name":"是的","method":"12","id":2,"responseDemo":"12","wrongCode":"1234","updateTime":412,"params":"[]","response":"[]","module":"一级","memo":"41","url":"打算","username":"二级2i","version":"3.6","del":0,"paramsDemo":"12345","addTime":12},{"name":"xx","method":"231","id":6,"responseDemo":"1232","wrongCode":"123","updateTime":123,"params":"[]","response":"[]","module":"一级","memo":"213","url":"1231","username":"231","version":"3.6","del":0,"paramsDemo":"31","addTime":1231}],"code":0}
```

