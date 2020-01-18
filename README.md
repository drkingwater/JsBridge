# JsBridge
web和原生app交互简单封装
# 使用方法
## 交互数据约束
```
{
    "action" : “XXXX”,
    "data" : {
	    "xxx" : "xxxx"
        ...
     }
}
```
## 添加依赖

```
implementation project(':jsbridge-core')
annotationProcessor project(':jsbridge-compiler')
```

## 编写实现类
```
@Bridge(name = "android", jsMethod = "request")
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

}
```

## 绑定
```
JsBridge.bind(webView, new BridgeTest());
```
