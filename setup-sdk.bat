@echo off
setlocal enabledelayedexpansion

echo 🛠️  Android SDK 安装脚本 (Windows版本)
echo ===============================================

REM 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误: 未找到Java环境
    echo 请从以下网址下载并安装Java 11或更高版本:
    echo https://adoptium.net/
    echo 或者安装OpenJDK: https://openjdk.org/install/
    pause
    exit /b 1
)

echo ✅ Java环境检查通过
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr "version"') do (
    set "java_version=%%i"
    echo Java版本: !java_version!
)

REM 检查是否已经安装了SDK
if exist "android-sdk" if exist "local.properties" (
    echo ✅ Android SDK已经安装
    echo 如需重新安装，请删除android-sdk目录和local.properties文件
    pause
    exit /b 0
)

echo.
echo 📥 步骤1: 下载Android SDK命令行工具...
if not exist "android-tools.zip" (
    echo 正在下载Android SDK命令行工具，请稍候...
    powershell -Command "Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip' -OutFile 'android-tools.zip'"
    
    if %errorlevel% neq 0 (
        echo ❌ 下载失败，请检查网络连接
        echo 您也可以手动下载并放置在项目根目录:
        echo https://dl.google.com/android/repository/commandlinetools-win-9477386_latest.zip
        pause
        exit /b 1
    )
    echo ✅ 下载完成
) else (
    echo ✅ 工具包已存在，跳过下载
)

echo.
echo 📦 步骤2: 解压和设置SDK目录结构...

REM 使用PowerShell解压文件
powershell -Command "Expand-Archive -Path 'android-tools.zip' -DestinationPath '.' -Force"

if not exist "android-sdk" mkdir android-sdk
if not exist "android-sdk\cmdline-tools" mkdir android-sdk\cmdline-tools
move cmdline-tools android-sdk\cmdline-tools\latest >nul 2>&1
del android-tools.zip >nul 2>&1

echo ✅ SDK目录结构创建完成

echo.
echo 🔧 步骤3: 设置环境变量...
set "ANDROID_HOME=%cd%\android-sdk"
set "PATH=%PATH%;%ANDROID_HOME%\cmdline-tools\latest\bin"
echo ✅ 环境变量已设置

echo.
echo 📝 步骤4: 创建local.properties配置文件...
echo sdk.dir=%cd%\android-sdk > local.properties
echo ✅ 配置文件已创建

echo.
echo 🔑 步骤5: 接受Android SDK许可证...
echo 正在接受许可证，请稍候...

REM 使用echo yes来自动接受许可证
echo yes | "%ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager.bat" --licenses >nul 2>&1

if %errorlevel% equ 0 (
    echo ✅ 许可证已接受
) else (
    echo ⚠️  许可证接受可能失败，但继续安装
)

echo.
echo 📦 步骤6: 安装必要的SDK组件...
echo 正在安装Android平台和构建工具，请稍候...

"%ANDROID_HOME%\cmdline-tools\latest\bin\sdkmanager.bat" "platforms;android-34" "build-tools;34.0.0" >nul 2>&1

if %errorlevel% equ 0 (
    echo ✅ SDK组件安装完成
) else (
    echo ⚠️  SDK组件安装可能失败，请手动检查
)

echo.
echo 🔧 步骤7: 准备Gradle Wrapper...
if not exist "gradle\wrapper" mkdir gradle\wrapper

if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo 正在下载Gradle Wrapper...
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.2.0/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle\wrapper\gradle-wrapper.jar'"
    echo ✅ Gradle Wrapper已准备就绪
) else (
    echo ✅ Gradle Wrapper已存在
)

echo.
echo 🎉 Android SDK安装完成！
echo ===============================================
echo.
echo 📋 安装信息：
echo - SDK位置: %cd%\android-sdk
echo - 配置文件: local.properties
echo - 平台版本: Android 34
echo - 构建工具: 34.0.0
echo.
echo 🚀 下一步：
echo 现在可以运行 build.bat 来构建APK文件
echo.
echo 💡 如果遇到问题：
echo 1. 确保网络连接正常
echo 2. 检查Java版本: java -version
echo 3. 重新运行此脚本: setup-sdk.bat
echo 4. 查看Windows构建指南: BUILD_GUIDE_WINDOWS.md
echo.
echo 按任意键退出...
pause >nul