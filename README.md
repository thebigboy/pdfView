# PDF电子发票解析器

一个使用Java开发的PDF电子发票解析工具，可以自动提取PDF格式电子发票中的关键信息。

## 🚀 快速开始

### 运行程序
```bash
# 直接运行JAR文件（推荐）
java -jar target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar

# 或使用Maven运行
mvn exec:java -Dexec.mainClass="com.invoice.InvoiceParserMain"
```

### 处理PDF发票
```bash
# 处理单个文件
java -jar target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar invoice.pdf

# 批量处理
java -jar target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar *.pdf
```

## ✨ 功能特性

- ✅ 解析PDF格式的电子发票文件
- ✅ 自动提取发票号码、发票代码、开票日期
- ✅ 提取销售方和购买方信息（名称、纳税人识别号）
- ✅ 解析金额信息（价税合计、税额、不含税金额）
- ✅ 支持发票明细项目解析
- ✅ 输出结构化的发票信息
- ✅ 可选择保存解析结果为JSON格式
- ✅ 支持批量处理多个文件
- ✅ 交互式命令行界面

## 🛠️ 技术栈

- **Java 8+**: 核心开发语言
- **Apache PDFBox**: PDF文件解析库
- **Jackson**: JSON序列化/反序列化
- **SLF4J**: 日志框架
- **Maven**: 依赖管理和构建工具

## 📋 系统要求

- Java 8 或更高版本
- Maven 3.6+ （开发构建）

## 🔧 开发构建

```bash
# 克隆项目
git clone <repository-url>
cd pdf-invoice-parser

# 编译项目
mvn clean compile

# 运行测试
mvn exec:java -Dexec.mainClass="com.invoice.TestRunner"

# 构建可执行JAR
mvn clean package
```

## 📖 详细文档

请查看 [使用说明.md](使用说明.md) 获取详细的使用指南和API文档。

## 🎯 支持的发票格式

- 增值税普通发票
- 增值税专用发票
- 电子普通发票
- 电子专用发票

## ⚠️ 注意事项

- 程序主要适用于文本型PDF，对于扫描版PDF效果可能不佳
- 确保PDF文件使用正确的中文编码
- 不同地区或不同版本的发票格式可能需要调整正则表达式

## 📄 许可证

本项目仅供学习和研究使用。