# 🎮 大富翁游戏 APK 文件位置

## 📱 APK 文件位置

经过成功构建后，大富翁游戏的APK文件位于：

```
app/build/outputs/apk/debug/app-debug.apk
```

## 🛠️ 如何构建APK

1. **确保环境准备就绪：**
   ```bash
   # 设置Android SDK
   export ANDROID_HOME=/path/to/android-sdk
   
   # 创建local.properties文件
   echo "sdk.dir=$ANDROID_HOME" > local.properties
   ```

2. **执行构建：**
   ```bash
   # 使用构建脚本
   ./build.sh
   
   # 或直接使用Gradle
   ./gradlew app:assembleDebug
   ```

3. **APK文件详情：**
   - **文件名：** `app-debug.apk`
   - **大小：** 约 5.7MB
   - **版本：** Debug版本
   - **应用ID：** `com.monopoly.game`
   - **目标SDK：** Android 34 (API 34)
   - **最低SDK：** Android 24 (API 24)

## 📦 安装说明

1. 将APK文件传输到Android设备
2. 在设备上启用"未知来源应用安装"
3. 点击APK文件进行安装
4. 享受大富翁游戏！

## 🏗️ 构建要求

- Java 8 或更高版本
- Android SDK Platform 34
- Android Build Tools 34.0.0
- Gradle 8.2

## 📝 注意事项

- APK文件不包含在Git仓库中，因为它们是构建产物
- 每次修改源代码后需要重新构建APK
- Debug版本APK仅用于开发和测试，生产环境需要构建Release版本

## 🚀 快速开始

```bash
# 克隆项目
git clone https://github.com/your-repo/monopoly-game.git
cd monopoly-game

# 构建APK
./build.sh

# APK文件将在以下位置生成：
# app/build/outputs/apk/debug/app-debug.apk
```