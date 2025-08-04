# 🪟 大富翁游戏Windows构建指南

## 🚀 快速开始

### 方法一：一键构建（推荐）
如果您已经有Android SDK：
```cmd
build.bat
```

### 方法二：从零开始
如果您是第一次在Windows上构建Android项目：
```cmd
REM 1. 安装Android SDK
setup-sdk.bat

REM 2. 构建APK
build.bat
```

## 📋 Windows系统要求

- **操作系统：** Windows 10/11 或 Windows Server 2016+
- **Java：** JDK 11 或更高版本
- **PowerShell：** Windows 10+ 内置
- **网络：** 需要下载Android SDK和依赖包
- **存储空间：** 约1GB（包含SDK）
- **权限：** 管理员权限（可选，某些操作需要）

## 🔧 详细安装步骤

### 步骤1：安装Java环境

#### 选项A：使用Eclipse Temurin（推荐）
1. 访问 https://adoptium.net/
2. 下载Java 11或更高版本
3. 运行安装程序，使用默认设置

#### 选项B：使用Oracle JDK
1. 访问 https://www.oracle.com/java/technologies/downloads/
2. 下载并安装JDK

#### 选项C：使用包管理器
```cmd
REM 使用Chocolatey
choco install openjdk11

REM 使用Scoop
scoop install openjdk11
```

#### 验证Java安装
```cmd
java -version
```

### 步骤2：下载项目
```cmd
REM 克隆项目
git clone https://github.com/your-repo/monopoly-game.git
cd monopoly-game

REM 切换到Windows分支
git checkout windows-support
```

### 步骤3：安装Android SDK
```cmd
setup-sdk.bat
```

### 步骤4：构建APK
```cmd
build.bat
```

## 🔧 Windows特有功能

### 1. 自动下载依赖
Windows脚本会自动：
- 下载Android SDK命令行工具
- 设置正确的目录结构
- 下载Gradle Wrapper
- 配置环境变量

### 2. 智能错误处理
- 检测Java环境
- 验证Android SDK安装
- 提供详细错误信息
- 建议解决方案

### 3. APK分享服务器
```cmd
REM 启动本地服务器分享APK
start-server.bat
```

## 🐛 Windows常见问题解决

### 问题1：PowerShell执行策略限制
**错误信息：** `PowerShell执行策略不允许运行脚本`

**解决方案：**
```cmd
REM 以管理员身份运行命令提示符
powershell -Command "Set-ExecutionPolicy RemoteSigned -Scope CurrentUser"
```

### 问题2：长路径名问题
**错误信息：** `路径过长` 或 `文件名过长`

**解决方案：**
```cmd
REM 启用长路径支持（需要管理员权限）
reg add "HKLM\SYSTEM\CurrentControlSet\Control\FileSystem" /v LongPathsEnabled /t REG_DWORD /d 1

REM 或者将项目移动到更短的路径，如 C:\monopoly
```

### 问题3：防火墙阻止下载
**错误信息：** `网络连接失败` 或 `下载超时`

**解决方案：**
```cmd
REM 暂时禁用防火墙或添加例外
REM 或者使用代理设置
set HTTP_PROXY=http://your-proxy:port
set HTTPS_PROXY=http://your-proxy:port
```

### 问题4：中文字符编码问题
**错误信息：** `字符显示乱码`

**解决方案：**
```cmd
REM 设置命令提示符为UTF-8编码
chcp 65001

REM 或者使用PowerShell替代命令提示符
```

### 问题5：gradlew.bat权限问题
**错误信息：** `gradlew.bat不是内部或外部命令`

**解决方案：**
```cmd
REM 检查文件是否存在
dir gradlew.bat

REM 如果不存在，重新下载
curl -o gradlew.bat https://github.com/gradle/gradle/raw/v8.2.0/gradlew.bat
```

## 📁 Windows文件路径

构建完成后，APK文件位于：
```
app\build\outputs\apk\debug\app-debug.apk
```

## 🔍 验证构建

```cmd
REM 检查APK文件是否存在
dir app\build\outputs\apk\debug\app-debug.apk

REM 查看文件大小
for %A in ("app\build\outputs\apk\debug\app-debug.apk") do echo 文件大小: %~zA 字节
```

## 📱 安装到Android设备

### 方法1：USB连接安装
```cmd
REM 确保启用了Android调试模式
adb devices

REM 安装APK
adb install app\build\outputs\apk\debug\app-debug.apk
```

### 方法2：通过网络分享
```cmd
REM 启动本地服务器
start-server.bat

REM 在手机浏览器访问显示的地址下载APK
```

### 方法3：云存储分享
1. 上传APK到OneDrive、Google Drive等
2. 在手机上下载APK文件
3. 安装APK

## 🏗️ 高级Windows选项

### 使用Windows Subsystem for Linux (WSL)
```cmd
REM 安装WSL2
wsl --install

REM 在WSL中构建（可以使用Linux脚本）
wsl
./build.sh
```

### 使用Docker Desktop
```cmd
REM 构建Docker镜像
docker build -t monopoly-builder .

REM 运行构建容器
docker run -v %cd%:/workspace monopoly-builder
```

### 使用Visual Studio Code
1. 安装VS Code和Android扩展
2. 打开项目文件夹
3. 使用集成终端运行构建脚本

## 🚀 自动化选项

### 使用任务计划程序
创建定时任务自动构建APK：
```cmd
schtasks /create /tn "MonopolyBuild" /tr "C:\path\to\build.bat" /sc daily /st 02:00
```

### 使用Windows批处理
创建一键式桌面快捷方式：
```cmd
REM 创建快捷方式指向build.bat
echo 将build.bat创建为桌面快捷方式
```

## 💡 性能优化建议

1. **使用SSD存储** - 提高编译速度
2. **增加JVM内存** - 在gradle.properties中设置
3. **启用并行构建** - 使用多核CPU
4. **使用本地仓库** - 减少网络下载

## 📞 Windows技术支持

如果遇到Windows特有的问题：

1. **检查系统兼容性** - 确保Windows 10+
2. **更新PowerShell** - 使用最新版本
3. **检查权限设置** - 某些操作需要管理员权限
4. **查看事件日志** - Windows事件查看器中的错误信息
5. **使用Windows调试工具** - Process Monitor, Dependency Walker等

## 🎯 一键解决脚本

如果所有方法都失败，尝试重置环境：
```cmd
REM 完全清理和重新开始
rmdir /s /q android-sdk
del local.properties
rmdir /s /q app\build
rmdir /s /q .gradle

REM 重新安装
setup-sdk.bat
build.bat
```