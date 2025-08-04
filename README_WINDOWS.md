# 🎮 大富翁游戏 - Windows版本

专为Windows用户优化的Android大富翁游戏构建分支。

## ⚡ 快速开始

### 🚀 一键构建APK
```cmd
REM 方法1：如果已有Android SDK
build.bat

REM 方法2：从零开始（推荐）
setup-sdk.bat
build.bat
```

## 🪟 Windows特色功能

### ✨ 全自动化脚本
- **setup-sdk.bat** - 自动下载并配置Android SDK
- **build.bat** - 智能构建APK，包含完整错误处理
- **start-server.bat** - 一键启动APK分享服务器

### 🔧 Windows优化
- 使用PowerShell进行高级功能
- 自动检测和配置环境变量
- 智能路径处理（支持中文路径）
- 完整的错误诊断和解决建议

### 📱 多种APK分享方式
- 本地HTTP服务器
- 自动获取局域网IP
- 支持Python和PowerShell双后端

## 📋 系统要求

| 组件 | 要求 | 下载链接 |
|-----|------|----------|
| **操作系统** | Windows 10/11 | - |
| **Java** | JDK 11+ | [Eclipse Temurin](https://adoptium.net/) |
| **PowerShell** | 5.1+ (内置) | - |
| **网络** | 宽带连接 | - |
| **存储** | 1GB+ 可用空间 | - |

## 🛠️ 详细安装指南

### 1️⃣ 安装Java（必需）
```cmd
REM 下载并安装Java 11或更高版本
REM 访问: https://adoptium.net/

REM 验证安装
java -version
```

### 2️⃣ 获取项目代码
```cmd
REM 克隆项目
git clone https://github.com/your-repo/monopoly-game.git
cd monopoly-game

REM 切换到Windows分支
git checkout windows-support
```

### 3️⃣ 安装Android SDK
```cmd
REM 运行自动安装脚本（推荐）
setup-sdk.bat

REM 或者手动配置（高级用户）
REM 1. 下载Android Studio
REM 2. 设置ANDROID_HOME环境变量
REM 3. 创建local.properties文件
```

### 4️⃣ 构建APK
```cmd
REM 运行构建脚本
build.bat

REM 构建成功后，APK位于：
REM app\build\outputs\apk\debug\app-debug.apk
```

### 5️⃣ 安装到设备
```cmd
REM 方法1：启动分享服务器
start-server.bat

REM 方法2：USB直接安装
adb install app\build\outputs\apk\debug\app-debug.apk
```

## 🎯 脚本功能详解

### 📦 setup-sdk.bat
自动化Android SDK安装脚本：
```cmd
✅ 检查Java环境
📥 下载Android SDK命令行工具
📂 创建正确的目录结构
🔑 自动接受许可证
📦 安装必要的SDK组件
🔧 配置Gradle Wrapper
```

### 🔨 build.bat
智能APK构建脚本：
```cmd
✅ 环境检查（Java、SDK）
🔧 自动修复配置文件
📦 Gradle构建执行
📊 显示详细构建信息
🎮 提供安装指导
```

### 🌐 start-server.bat
APK分享服务器：
```cmd
📱 自动检测APK文件
🌐 启动HTTP服务器
🔗 显示下载链接
📋 提供使用说明
```

## 🐛 常见问题快速解决

### ❌ "未找到Java环境"
```cmd
REM 下载并安装Java
https://adoptium.net/

REM 重启命令提示符后重试
```

### ❌ "PowerShell脚本执行被阻止"
```cmd
REM 以管理员身份运行
powershell -Command "Set-ExecutionPolicy RemoteSigned -Scope CurrentUser"
```

### ❌ "网络连接失败"
```cmd
REM 检查防火墙设置
REM 或使用代理
set HTTP_PROXY=http://your-proxy:port
```

### ❌ "路径过长"
```cmd
REM 移动项目到更短路径
move C:\very\long\path\monopoly-game C:\monopoly

REM 或启用长路径支持（管理员权限）
reg add "HKLM\SYSTEM\CurrentControlSet\Control\FileSystem" /v LongPathsEnabled /t REG_DWORD /d 1
```

## 📁 输出文件位置

构建完成后，您可以在以下位置找到APK文件：

```
📂 项目根目录
└── 📂 app
    └── 📂 build
        └── 📂 outputs
            └── 📂 apk
                └── 📂 debug
                    └── 📱 app-debug.apk  ← APK文件在这里
```

完整路径：`app\build\outputs\apk\debug\app-debug.apk`

## 🎮 游戏特色

- 🏠 **经典大富翁玩法** - 买地、建房、收租
- 🎯 **智能AI对手** - 多种难度选择
- 🎨 **精美UI界面** - 现代化Material Design
- 📱 **触屏优化** - 专为移动设备设计
- 🌐 **多人游戏** - 支持2-6人对战
- 💰 **丰富道具** - 机会卡、命运卡等

## 🔄 版本信息

- **当前版本：** 1.0 (Debug)
- **目标Android版本：** API 34 (Android 14)
- **最低支持版本：** API 24 (Android 7.0)
- **应用包名：** com.monopoly.game
- **APK大小：** 约5.7MB

## 🆘 获取帮助

### 📚 文档资源
- [Windows构建指南](BUILD_GUIDE_WINDOWS.md) - 详细的Windows构建说明
- [APK下载方案](DOWNLOAD_APK.md) - 多种APK获取方法
- [常见问题解答](BUILD_GUIDE.md) - 通用问题解决

### 💬 技术支持
如果遇到问题：
1. 查看错误信息并参考解决方案
2. 运行 `java -version` 检查Java环境
3. 删除 `android-sdk` 目录后重新运行 `setup-sdk.bat`
4. 查看Windows事件查看器中的错误日志

### 🔄 重置环境
如果所有方法都失败：
```cmd
REM 完全重置构建环境
rmdir /s /q android-sdk
del local.properties
rmdir /s /q app\build
rmdir /s /q .gradle

REM 重新开始
setup-sdk.bat
build.bat
```

## 📝 许可证

本项目采用MIT许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

---

🎮 **享受大富翁游戏的乐趣！**

如果这个项目对您有帮助，请给我们一个 ⭐ Star！