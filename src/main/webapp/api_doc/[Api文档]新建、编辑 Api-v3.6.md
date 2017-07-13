# 【Api文档】新建、编辑 Api - v3.6
> 新建或编辑 Api 文档

### 1. url：/api/doc/item.do

### 2. 请求方式：POST

### 3. 请求参数
|参数 Key|参数名称|参数类型|长度上限|是否必填|说明|
|:-----------|:-----------|:---------|:---------|:---------|:-----------|
|id|主键|Integer|0|false|为空时，是新建操作|
|||String|0|true||

### 4. 请求实例
```JSON
{
&nbsp;&nbsp;&nbsp;&nbsp;
    

}
```

### 5. 主要返回内容
|参数 Key|参数名称|参数类型|说明|
|:-----------|:-----------|:---------|:---------|:---------|:-----------|
|code|数据库操作结果|Integer|1：成功|
|id|Api 文档主键|Integer|新建成功后的主键|


### 6. 返回实例
```JSON
{
&nbsp;&nbsp;&nbsp;&nbsp;
        "msg":"操作成功",
&nbsp;&nbsp;&nbsp;&nbsp;
        "ret":
    {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                "code":1,
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                "id":12    
    
&nbsp;&nbsp;&nbsp;&nbsp;},
&nbsp;&nbsp;&nbsp;&nbsp;
        "code":0

}
```

