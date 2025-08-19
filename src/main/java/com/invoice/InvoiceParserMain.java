package com.invoice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * PDF电子发票解析主程序
 */
public class InvoiceParserMain {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceParserMain.class);
    
    public static void main(String[] args) {
        logger.info("PDF电子发票解析程序启动");
        
        PdfInvoiceParser parser = new PdfInvoiceParser();
        Scanner scanner = new Scanner(System.in);
        
        try {
            // 如果命令行提供了文件路径参数
            if (args.length > 0) {
                for (String filePath : args) {
                    processInvoiceFile(parser, filePath);
                }
            } else {
                // 交互式模式
                runInteractiveMode(parser, scanner);
            }
        } catch (Exception e) {
            logger.error("程序执行过程中出现错误: {}", e.getMessage(), e);
        } finally {
            scanner.close();
        }
        
        logger.info("PDF电子发票解析程序结束");
    }
    
    /**
     * 交互式模式
     */
    private static void runInteractiveMode(PdfInvoiceParser parser, Scanner scanner) {
        System.out.println("=== PDF电子发票解析程序 ===");
        System.out.println("请输入PDF发票文件路径（输入 'quit' 退出）：");
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if ("quit".equalsIgnoreCase(input) || "exit".equalsIgnoreCase(input)) {
                System.out.println("程序退出");
                break;
            }
            
            if (input.isEmpty()) {
                System.out.println("请输入有效的文件路径");
                continue;
            }
            
            processInvoiceFile(parser, input);
        }
    }
    
    /**
     * 处理发票文件
     */
    private static void processInvoiceFile(PdfInvoiceParser parser, String filePath) {
        try {
            File pdfFile = new File(filePath);
            
            if (!pdfFile.exists()) {
                System.err.println("错误: 文件不存在 - " + filePath);
                return;
            }
            
            if (!pdfFile.isFile()) {
                System.err.println("错误: 不是有效文件 - " + filePath);
                return;
            }
            
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                System.err.println("警告: 文件可能不是PDF格式 - " + filePath);
            }
            
            System.out.println("正在解析文件: " + pdfFile.getAbsolutePath());
            
            // 解析PDF发票
            InvoiceInfo invoiceInfo = parser.parsePdfInvoice(pdfFile);
            
            // 验证解析结果
            if (parser.validateInvoiceInfo(invoiceInfo)) {
                System.out.println("解析成功！");
                System.out.println(invoiceInfo.toString());
                
                // 可选：保存解析结果到JSON文件
                saveInvoiceToJson(invoiceInfo, pdfFile);
                
            } else {
                System.out.println("解析完成，但发票信息可能不完整：");
                System.out.println(invoiceInfo.toString());
            }
            
        } catch (IOException e) {
            System.err.println("解析文件时出错: " + e.getMessage());
            logger.error("解析文件出错: {}", filePath, e);
        } catch (Exception e) {
            System.err.println("处理文件时出现未知错误: " + e.getMessage());
            logger.error("处理文件出现未知错误: {}", filePath, e);
        }
        
        System.out.println("----------------------------------------");
    }
    
    /**
     * 保存发票信息到JSON文件（可选功能）
     */
    private static void saveInvoiceToJson(InvoiceInfo invoiceInfo, File pdfFile) {
        try {
            String jsonFileName = pdfFile.getName().replaceAll("\\.pdf$", ".json");
            File jsonFile = new File(pdfFile.getParent(), jsonFileName);
            
            InvoiceJsonUtil.saveToJson(invoiceInfo, jsonFile);
            System.out.println("解析结果已保存到: " + jsonFile.getAbsolutePath());
            
        } catch (Exception e) {
            System.err.println("保存JSON文件时出错: " + e.getMessage());
            logger.warn("保存JSON文件出错", e);
        }
    }
    
    /**
     * 显示使用帮助
     */
    public static void printUsage() {
        System.out.println("使用方法:");
        System.out.println("  java -jar pdf-invoice-parser.jar [PDF文件路径...]");
        System.out.println("  或者直接运行程序进入交互模式");
        System.out.println();
        System.out.println("示例:");
        System.out.println("  java -jar pdf-invoice-parser.jar invoice1.pdf invoice2.pdf");
        System.out.println("  java -jar pdf-invoice-parser.jar");
        System.out.println();
        System.out.println("支持的功能:");
        System.out.println("  - 解析PDF格式的电子发票");
        System.out.println("  - 提取发票号码、金额、日期等关键信息");
        System.out.println("  - 输出详细的发票信息");
        System.out.println("  - 可选择保存解析结果为JSON格式");
    }
}