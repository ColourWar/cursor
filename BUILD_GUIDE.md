# 🛠️ 大富翁游戏构建指南

## 🚀 快速开始

### 方法一：一键构建（推荐）
如果您已经有Android SDK：
```bash
./build.sh
```

### 方法二：从零开始
如果您是第一次构建Android项目：
```bash
# 1. 安装Android SDK
./setup-sdk.sh

# 2. 构建APK
./build.sh
```

## 📋 系统要求

- **操作系统：** Linux/macOS/Windows WSL
- **Java：** JDK 8 或更高版本
- **网络：** 需要下载Android SDK和依赖包
- **存储空间：** 约1GB（包含SDK）

## 🔧 详细步骤

### 步骤1：检查Java环境
```bash
java -version
```
如果没有Java，请先安装：
```bash
# Ubuntu/Debian
sudo apt update && sudo apt install openjdk-11-jdk

# CentOS/RHEL
sudo yum install java-11-openjdk-devel
```

### 步骤2：安装Android SDK
```bash
./setup-sdk.sh
```

### 步骤3：构建APK
```bash
./build.sh
```

## 🐛 常见问题解决

### 问题1：Java环境未找到
**错误信息：** `❌ 错误: 未找到Java环境`

**解决方案：**
```bash
# 检查Java是否安装
which java

# 如未安装，请安装Java
sudo apt install openjdk-11-jdk  # Ubuntu
```

### 问题2：网络连接问题
**错误信息：** `下载失败，请检查网络连接`

**解决方案：**
```bash
# 检查网络连接
ping google.com

# 使用代理（如需要）
export http_proxy=http://your-proxy:port
export https_proxy=http://your-proxy:port
```

### 问题3：Gradle构建失败
**错误信息：** `BUILD FAILED`

**解决方案：**
```bash
# 清理项目
./gradlew clean

# 重新构建
./gradlew app:assembleDebug

# 查看详细错误
./gradlew app:assembleDebug --stacktrace
```

### 问题4：权限问题
**错误信息：** `Permission denied`

**解决方案：**
```bash
# 添加执行权限
chmod +x build.sh setup-sdk.sh gradlew
```

### 问题5：Android SDK未找到
**错误信息：** `SDK location not found`

**解决方案：**
```bash
# 检查local.properties文件
cat local.properties

# 重新创建配置文件
echo "sdk.dir=$PWD/android-sdk" > local.properties

# 或重新安装SDK
./setup-sdk.sh
```

## 📁 输出文件

成功构建后，APK文件位于：
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🔍 验证构建

```bash
# 检查APK文件是否存在
ls -la app/build/outputs/apk/debug/app-debug.apk

# 查看APK信息
file app/build/outputs/apk/debug/app-debug.apk
```

## 📱 安装到设备

### Android设备安装：
1. 将APK文件传输到设备
2. 在设备设置中启用"未知来源应用安装"
3. 点击APK文件安装

### 模拟器安装：
```bash
# 如果有Android模拟器
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 🏗️ 高级选项

### 构建Release版本
```bash
./gradlew app:assembleRelease
```

### 查看所有可用任务
```bash
./gradlew tasks
```

### 清理构建缓存
```bash
./gradlew clean
```

## 📞 获取帮助

如果遇到其他问题：

1. **查看构建日志：** 运行构建命令时会显示详细错误信息
2. **检查系统要求：** 确保满足所有系统要求
3. **重新安装SDK：** 删除`android-sdk`目录和`local.properties`文件，重新运行`./setup-sdk.sh`
4. **清理项目：** 运行`./gradlew clean`后重新构建

## 🎯 一键解决脚本

如果所有方法都失败，尝试重置环境：
```bash
# 完全清理和重新开始
rm -rf android-sdk local.properties app/build .gradle
./setup-sdk.sh
./build.sh
```