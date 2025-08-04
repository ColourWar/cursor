#!/bin/bash

echo "🛠️  Android SDK 安装脚本"
echo "================================"

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到Java环境"
    echo "请先安装Java 8或更高版本"
    exit 1
fi

echo "✅ Java环境检查通过: $(java -version 2>&1 | head -n 1)"

# 检查是否已经安装了SDK
if [ -d "android-sdk" ] && [ -f "local.properties" ]; then
    echo "✅ Android SDK已经安装"
    echo "如需重新安装，请删除android-sdk目录和local.properties文件"
    exit 0
fi

echo ""
echo "📥 步骤1: 下载Android SDK命令行工具..."
if [ ! -f "android-tools.zip" ]; then
    wget -q --show-progress https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O android-tools.zip
    if [ $? -ne 0 ]; then
        echo "❌ 下载失败，请检查网络连接"
        exit 1
    fi
else
    echo "✅ 工具包已存在，跳过下载"
fi

echo ""
echo "📦 步骤2: 解压和设置SDK目录结构..."
unzip -q android-tools.zip
mkdir -p android-sdk/cmdline-tools
mv cmdline-tools android-sdk/cmdline-tools/latest
rm android-tools.zip
echo "✅ SDK目录结构创建完成"

echo ""
echo "🔧 步骤3: 设置环境变量..."
export ANDROID_HOME=$PWD/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
echo "✅ 环境变量已设置"

echo ""
echo "📝 步骤4: 创建local.properties配置文件..."
echo "sdk.dir=$PWD/android-sdk" > local.properties
echo "✅ 配置文件已创建"

echo ""
echo "🔑 步骤5: 接受Android SDK许可证..."
echo "正在接受许可证，请稍候..."
yes | sdkmanager --licenses > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ 许可证已接受"
else
    echo "⚠️  许可证接受可能失败，但继续安装"
fi

echo ""
echo "📦 步骤6: 安装必要的SDK组件..."
echo "正在安装Android平台和构建工具..."
sdkmanager "platforms;android-34" "build-tools;34.0.0" > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ SDK组件安装完成"
else
    echo "⚠️  SDK组件安装可能失败，请手动检查"
fi

echo ""
echo "🔧 步骤7: 准备Gradle Wrapper..."
mkdir -p gradle/wrapper
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    curl -L -o gradle/wrapper/gradle-wrapper.jar https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar > /dev/null 2>&1
    echo "✅ Gradle Wrapper已准备就绪"
else
    echo "✅ Gradle Wrapper已存在"
fi

echo ""
echo "🎉 Android SDK安装完成！"
echo "================================"
echo ""
echo "📋 安装信息："
echo "- SDK位置: $PWD/android-sdk"
echo "- 配置文件: local.properties"
echo "- 平台版本: Android 34"
echo "- 构建工具: 34.0.0"
echo ""
echo "🚀 下一步："
echo "现在可以运行 ./build.sh 来构建APK文件"
echo ""
echo "💡 如果遇到问题："
echo "1. 确保网络连接正常"
echo "2. 检查Java版本: java -version"
echo "3. 重新运行此脚本: ./setup-sdk.sh"