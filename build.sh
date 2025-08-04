#!/bin/bash

echo "🚀 开始构建大富翁游戏APK..."

# 检查必要的环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到Java环境"
    exit 1
fi

# 检查Android SDK
if [ ! -f "local.properties" ]; then
    echo "⚠️  警告: 未找到local.properties文件"
    if [ -d "android-sdk" ]; then
        echo "🔧 自动创建local.properties..."
        echo "sdk.dir=$PWD/android-sdk" > local.properties
        echo "✅ 已创建local.properties"
    else
        echo "❌ 错误: 未找到Android SDK，请先运行SDK安装步骤"
        echo ""
        echo "💡 解决方案："
        echo "1. 运行以下命令安装Android SDK："
        echo "   wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O android-tools.zip"
        echo "   unzip -q android-tools.zip && mkdir -p android-sdk/cmdline-tools"
        echo "   mv cmdline-tools android-sdk/cmdline-tools/latest && rm android-tools.zip"
        echo ""
        echo "2. 设置环境变量："
        echo "   export ANDROID_HOME=\$PWD/android-sdk"
        echo "   export PATH=\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin"
        echo ""
        echo "3. 安装SDK组件："
        echo "   yes | sdkmanager --licenses"
        echo "   sdkmanager \"platforms;android-34\" \"build-tools;34.0.0\""
        exit 1
    fi
fi

# 设置权限
chmod +x gradlew

# 检查gradlew wrapper
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "🔧 下载Gradle Wrapper..."
    mkdir -p gradle/wrapper
    curl -L -o gradle/wrapper/gradle-wrapper.jar https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar
    echo "✅ Gradle Wrapper已准备就绪"
fi

echo "📦 开始Gradle构建..."

# 构建Debug APK - 使用正确的任务名称
./gradlew app:assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ 构建成功！"
    echo "📱 APK文件位置: app/build/outputs/apk/debug/app-debug.apk"
    
    # 显示APK信息
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        APK_SIZE=$(ls -lh app/build/outputs/apk/debug/app-debug.apk | awk '{print $5}')
        echo "📊 APK大小: $APK_SIZE"
        
        # 显示APK详细信息
        echo "📋 APK详细信息:"
        echo "   - 应用ID: com.monopoly.game"
        echo "   - 版本: 1.0 (Debug)"
        echo "   - 目标SDK: Android 34"
        echo "   - 最低SDK: Android 24"
    else
        echo "⚠️  警告: APK文件未找到，但构建报告成功"
    fi
    
    echo ""
    echo "🎮 安装说明:"
    echo "1. 将APK文件传输到Android设备"
    echo "2. 在设备上启用'未知来源应用安装'"
    echo "3. 点击APK文件进行安装"
    echo "4. 享受大富翁游戏吧！"
    
    echo ""
    echo "📁 APK文件位置: $(pwd)/app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ 构建失败，请检查错误信息"
    echo ""
    echo "🔍 常见解决方案："
    echo "1. 检查Java版本: java -version"
    echo "2. 检查Android SDK配置: cat local.properties"
    echo "3. 清理项目: ./gradlew clean"
    echo "4. 检查网络连接（下载依赖需要）"
    exit 1
fi