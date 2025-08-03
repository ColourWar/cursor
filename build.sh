#!/bin/bash

echo "🚀 开始构建大富翁游戏APK..."

# 检查必要的环境
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到Java环境"
    exit 1
fi

# 设置权限
chmod +x gradlew

echo "📦 开始Gradle构建..."

# 构建Debug APK
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ 构建成功！"
    echo "📱 APK文件位置: app/build/outputs/apk/debug/app-debug.apk"
    
    # 显示APK信息
    if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
        APK_SIZE=$(ls -lh app/build/outputs/apk/debug/app-debug.apk | awk '{print $5}')
        echo "📊 APK大小: $APK_SIZE"
    fi
    
    echo ""
    echo "🎮 安装说明:"
    echo "1. 将APK文件传输到Android设备"
    echo "2. 在设备上启用'未知来源应用安装'"
    echo "3. 点击APK文件进行安装"
    echo "4. 享受大富翁游戏吧！"
else
    echo "❌ 构建失败，请检查错误信息"
    exit 1
fi