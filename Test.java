package com.invoice;

import java.io.File;

/**
 * 简单的测试类
 */
public class Test {
    
    public static void main(String[] args) {
        // 测试PDF发票解析功能
        System.out.println("PDF电子发票解析测试程序");
        
        // 如果有测试文件，可以在这里测试
        if (args.length > 0) {
            String testFile = args[0];
            System.out.println("测试文件: " + testFile);
            
            try {
                PdfInvoiceParser parser = new PdfInvoiceParser();
                InvoiceInfo invoiceInfo = parser.parsePdfInvoice(new File(testFile));
                
                System.out.println("解析结果:");
                System.out.println(invoiceInfo);
                
            } catch (Exception e) {
                System.err.println("测试失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("请使用 InvoiceParserMain 类进行完整的发票解析");
            System.out.println("或者提供测试PDF文件路径作为参数");
        }
    }
}
