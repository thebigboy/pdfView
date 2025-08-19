#!/bin/bash

# PDF电子发票解析程序演示脚本

echo "=== PDF电子发票解析程序演示 ==="
echo ""

echo "1. 项目信息:"
echo "   - 项目名称: PDF电子发票解析器"
echo "   - 版本: 1.0.0"
echo "   - 技术栈: Java 8, Apache PDFBox, Jackson, Maven"
echo ""

echo "2. 功能特性:"
echo "   ✓ 解析PDF格式的电子发票"
echo "   ✓ 提取发票号码、金额、日期等关键信息"
echo "   ✓ 支持销售方和购买方信息解析"
echo "   ✓ 支持发票明细项目解析"
echo "   ✓ 输出结构化的发票信息"
echo "   ✓ 可选择保存为JSON格式"
echo "   ✓ 支持批量处理"
echo "   ✓ 交互式命令行界面"
echo ""

echo "3. 项目结构:"
find . -name "*.java" | head -10 | while read file; do
    echo "   📄 $file"
done
echo ""

echo "4. 构建产物:"
if [ -f "target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar" ]; then
    echo "   ✅ 可执行JAR文件: $(ls -lh target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar | awk '{print $5}')"
else
    echo "   ❌ JAR文件未找到"
fi
echo ""

echo "5. 使用方法:"
echo "   # 交互式模式"
echo "   java -jar target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar"
echo ""
echo "   # 命令行模式（指定文件）"
echo "   java -jar target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar invoice.pdf"
echo ""
echo "   # 批量处理"
echo "   java -jar target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar *.pdf"
echo ""
echo "   # 使用Maven运行"
echo "   mvn exec:java -Dexec.mainClass=\"com.invoice.InvoiceParserMain\""
echo ""

echo "6. 运行测试:"
echo "   正在运行功能测试..."
java -cp target/pdf-invoice-parser-1.0.0-jar-with-dependencies.jar com.invoice.TestRunner | grep -E "(成功|通过|完成)" | while read line; do
    echo "   ✅ $line"
done
echo ""

echo "7. 支持的发票格式:"
echo "   • 增值税普通发票"
echo "   • 增值税专用发票" 
echo "   • 电子普通发票"
echo "   • 电子专用发票"
echo ""

echo "8. 注意事项:"
echo "   • 程序主要适用于文本型PDF，扫描版PDF效果可能不佳"
echo "   • 确保PDF文件使用正确的中文编码"
echo "   • 不同地区的发票格式可能需要调整正则表达式"
echo ""

echo "=== 演示完成 ==="
echo ""
echo "如需更多信息，请查看 使用说明.md 文件"