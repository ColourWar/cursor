@echo off
setlocal

echo 🌐 启动APK分享服务器 (Windows版本)
echo =======================================

REM 检查APK文件是否存在
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ❌ 错误: 未找到APK文件
    echo 请先运行 build.bat 构建APK
    pause
    exit /b 1
)

echo ✅ 找到APK文件: app\build\outputs\apk\debug\app-debug.apk

REM 获取本机IP地址
for /f "tokens=2 delims=:" %%A in ('ipconfig ^| findstr /C:"IPv4"') do (
    for /f "tokens=1" %%B in ("%%A") do (
        set "LOCAL_IP=%%B"
        goto :ip_found
    )
)
:ip_found

echo 📂 切换到APK目录...
cd /d "app\build\outputs\apk\debug"

echo 🚀 启动HTTP服务器在端口8000...
echo.
echo 📱 APK下载地址:
echo    本地访问: http://localhost:8000/app-debug.apk
if defined LOCAL_IP (
    echo    网络访问: http://%LOCAL_IP%:8000/app-debug.apk
)
echo.
echo 📋 使用说明:
echo 1. 在手机浏览器中访问上述地址
echo 2. 下载app-debug.apk文件
echo 3. 在Android设备上安装APK
echo.
echo 💡 提示: 按Ctrl+C停止服务器
echo =======================================
echo.

REM 检查Python是否可用
python --version >nul 2>&1
if %errorlevel% equ 0 (
    echo 使用Python启动服务器...
    python -m http.server 8000
) else (
    REM 如果没有Python，使用PowerShell
    echo 使用PowerShell启动服务器...
    powershell -Command "& { Add-Type -AssemblyName System.Net.HttpListener; $listener = New-Object System.Net.HttpListener; $listener.Prefixes.Add('http://localhost:8000/'); $listener.Start(); Write-Host 'HTTP服务器已启动在 http://localhost:8000/'; Write-Host '按任意键停止服务器...'; while ($listener.IsListening) { $context = $listener.GetContext(); $request = $context.Request; $response = $context.Response; if ($request.Url.LocalPath -eq '/app-debug.apk') { $bytes = [System.IO.File]::ReadAllBytes('app-debug.apk'); $response.ContentType = 'application/vnd.android.package-archive'; $response.ContentLength64 = $bytes.Length; $response.OutputStream.Write($bytes, 0, $bytes.Length); } else { $html = '<html><body><h1>大富翁游戏APK下载</h1><a href=\"/app-debug.apk\">点击下载APK</a></body></html>'; $bytes = [System.Text.Encoding]::UTF8.GetBytes($html); $response.ContentType = 'text/html'; $response.ContentLength64 = $bytes.Length; $response.OutputStream.Write($bytes, 0, $bytes.Length); } $response.OutputStream.Close(); if ([Console]::KeyAvailable) { break; } } $listener.Stop(); }"
)

echo.
echo 服务器已停止
pause