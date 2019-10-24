# cordova-plugin-leyu

> cordova框架下的关于乐愚底层的app在乐愚平板上关于手势的锁定跟解锁服务功能

### 使用方法

- 安装插件
```shell
  cordova plugin add cordova-plugin-leyu
```

- 开始使用
需要先开启leyu服务
```
  cordova.plugins.LeyuPlugin.openService()
```

- 获取服务状态
```
  // 返回状态： true 关闭手写 / false 开启手写
  cordova.plugins.LeyuPlugin.getTpEnable().then(res => {
    console.log(res)
  })
```

- 设置是否手写
```
  // 关闭手写
  cordova.plugins.LeyuPlugin.setTpEnable(true)

  // 开启手写
  cordova.plugins.LeyuPlugin.setTpEnable(true)
```

- 关闭服务
```
  cordova.plugins.LeyuPlugin.closeService()
```

`tip： 记得关闭服务`
