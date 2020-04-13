# JsBridge
web和原生app交互简单封装
# 使用方法
## 交互数据约束
```json
{
    "action" : “XXXX”,
    "data" : {
	    "xxx" : "xxxx"
        ...
     }
}
```
## 添加依赖

```java
implementation project(':jsbridge-core')
annotationProcessor project(':jsbridge-compiler')
```

## 编写实现类
```java
@Bridge("android") //window.andriod
@JsConfig(jsMethod = "call", actionName = "function", paramsName = "data") //window.android.call()
public class BridgeTest {

    private static final String TAG = "BridgeTest";

    @JsAction("test")
    public void test(){
        Log.i(TAG, "test: ");
    }


    @JsAction("testData")
    public void testData(TestBean test){
        Log.i(TAG, "testData: " + test.name +" " +  test.data);
    }

    @UnHandle
    public void UnHandle(String request){
        Log.w(TAG, "UnHandle: " + request);
    }

    @JsError
    public void error(String request, Exception e){
        Log.e(TAG, "error: " +request, e);
    }
    
    @JsFunc("func")
    public String func(String request){
        return "func data";
    }
    
}

```

## 绑定
```java
JsBridge.bind(webView, new BridgeTest());
```

## web调用方式
```javascript
//无返回值
function call(){
    var json = "{\"function\":\"test\", \"data\": {\"name\" : \"pxq\"}}"
    window.android.call(json);
}
//同步调用
function callSync(){
    var json = "{\"function\":\"func\", \"data\": {\"name\" : \"pxq\"}}"
    var result = window.android.callSync(json);
}
```
## 混淆规则
```java
#保持注解不被混淆
-keepattributes *Annotation*
-keep class * extends annotation.Annotation{*;}
#保持@Bridge注解的类不被混淆
-keepnames @com.pxq.jsbridge.annotation.Bridge class *
#保持生成的类不被混淆
-keep class **$$Bridge {
    #保留构造方法
    <init>(...);
    #保留交互方法
    @android.webkit.Javascriptinterface <method>;
}
```

