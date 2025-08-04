@echo off
setlocal enabledelayedexpansion

echo 🚀 开始构建大富翁游戏APK (Windows版本)...
echo ================================================

REM 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到Java环境
    echo 请从 https://adoptium.net/ 下载并安装Java 11或更高版本
    pause
    exit /b 1
)

echo ✅ Java环境检查通过

REM 检查Android SDK
if not exist "local.properties" (
    echo ⚠️  警告: 未找到local.properties文件
    if exist "android-sdk" (
        echo 🔧 自动创建local.properties...
        echo sdk.dir=%cd%\android-sdk > local.properties
        echo ✅ 已创建local.properties
    ) else (
        echo ❌ 错误: 未找到Android SDK，请先运行SDK安装步骤
        echo.
        echo 💡 解决方案：
        echo 1. 运行 setup-sdk.bat 安装Android SDK
        echo 2. 或者手动下载Android Studio并设置SDK路径
        echo.
        pause
        exit /b 1
    )
)

REM 检查gradlew wrapper
if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo 🔧 下载Gradle Wrapper...
    if not exist "gradle\wrapper" mkdir gradle\wrapper
    
    echo 正在下载gradle-wrapper.jar...
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle\wrapper\gradle-wrapper.jar'"
    
    if %errorlevel% equ 0 (
        echo ✅ Gradle Wrapper下载完成
    ) else (
        echo ⚠️  Gradle Wrapper下载失败，尝试继续构建
    )
)

echo 📦 开始Gradle构建...
echo.

REM 构建Debug APK
gradlew.bat app:assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo ✅ 构建成功！
    echo 📱 APK文件位置: app\build\outputs\apk\debug\app-debug.apk
    
    REM 显示APK信息
    if exist "app\build\outputs\apk\debug\app-debug.apk" (
        for %%A in ("app\build\outputs\apk\debug\app-debug.apk") do (
            set size=%%~zA
            set /a sizeMB=!size!/1024/1024
            echo 📊 APK大小: !sizeMB!MB
        )
        
        echo 📋 APK详细信息:
        echo    - 应用ID: com.monopoly.game
        echo    - 版本: 1.0 (Debug^)
        echo    - 目标SDK: Android 34
        echo    - 最低SDK: Android 24
    ) else (
        echo ⚠️  警告: APK文件未找到，但构建报告成功
    )
    
    echo.
    echo 🎮 安装说明:
    echo 1. 将APK文件传输到Android设备
    echo 2. 在设备上启用'未知来源应用安装'
    echo 3. 点击APK文件进行安装
    echo 4. 享受大富翁游戏吧！
    
    echo.
    echo 📁 APK文件位置: %cd%\app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 🌐 或者运行 start-server.bat 启动本地服务器分享APK
    
) else (
    echo.
    echo ❌ 构建失败，请检查错误信息
    echo.
    echo 🔍 常见解决方案：
    echo 1. 检查Java版本: java -version
    echo 2. 检查Android SDK配置: type local.properties
    echo 3. 清理项目: gradlew.bat clean
    echo 4. 检查网络连接（下载依赖需要）
    echo 5. 运行 setup-sdk.bat 重新安装SDK
    pause
    exit /b 1
)

echo.
echo 按任意键退出...
pause >nul