package com.invoice;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 简单的测试运行器，用于验证程序功能
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("=== PDF电子发票解析程序测试 ===");
        
        // 测试发票信息模型
        testInvoiceInfoModel();
        
        // 测试JSON工具
        testJsonUtility();
        
        // 测试PDF解析器初始化
        testPdfParserInitialization();
        
        System.out.println("=== 测试完成 ===");
        
        // 显示使用说明
        System.out.println("\n使用说明:");
        System.out.println("1. 将PDF发票文件放在工作目录中");
        System.out.println("2. 运行: mvn exec:java -Dexec.mainClass=\"com.invoice.InvoiceParserMain\"");
        System.out.println("3. 或者运行: java -cp target/classes com.invoice.InvoiceParserMain [PDF文件路径]");
        System.out.println("4. 程序将自动解析PDF文件并输出发票信息");
        
        System.out.println("\n示例命令:");
        System.out.println("mvn exec:java -Dexec.mainClass=\"com.invoice.InvoiceParserMain\" -Dexec.args=\"invoice.pdf\"");
    }
    
    /**
     * 测试发票信息模型
     */
    private static void testInvoiceInfoModel() {
        System.out.println("\n1. 测试发票信息模型...");
        
        InvoiceInfo invoice = new InvoiceInfo();
        invoice.setInvoiceNumber("12345678");
        invoice.setInvoiceCode("1234567890123");
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setSellerName("测试销售方公司");
        invoice.setSellerTaxNumber("91110000123456789X");
        invoice.setBuyerName("测试购买方公司");
        invoice.setBuyerTaxNumber("91110000987654321A");
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setTaxAmount(new BigDecimal("130.00"));
        invoice.setAmountWithoutTax(new BigDecimal("870.00"));
        invoice.setRemarks("测试备注");
        
        // 添加发票明细
        InvoiceInfo.InvoiceItem item = new InvoiceInfo.InvoiceItem();
        item.setItemName("测试商品");
        item.setSpecification("规格型号");
        item.setUnit("个");
        item.setQuantity(new BigDecimal("10"));
        item.setUnitPrice(new BigDecimal("87.00"));
        item.setAmount(new BigDecimal("870.00"));
        item.setTaxRate(new BigDecimal("0.13"));
        item.setTaxAmount(new BigDecimal("130.00"));
        
        invoice.getItems().add(item);
        
        System.out.println("发票信息模型创建成功！");
        System.out.println("发票号码: " + invoice.getInvoiceNumber());
        System.out.println("总金额: ¥" + invoice.getTotalAmount());
        System.out.println("明细项目数量: " + invoice.getItems().size());
    }
    
    /**
     * 测试JSON工具
     */
    private static void testJsonUtility() {
        System.out.println("\n2. 测试JSON工具...");
        
        try {
            InvoiceInfo invoice = new InvoiceInfo();
            invoice.setInvoiceNumber("TEST123");
            invoice.setTotalAmount(new BigDecimal("100.00"));
            
            String jsonString = InvoiceJsonUtil.toJsonString(invoice);
            System.out.println("JSON序列化成功！");
            System.out.println("JSON长度: " + jsonString.length() + " 字符");
            
            InvoiceInfo parsedInvoice = InvoiceJsonUtil.fromJsonString(jsonString);
            System.out.println("JSON反序列化成功！");
            System.out.println("解析后发票号: " + parsedInvoice.getInvoiceNumber());
            
        } catch (Exception e) {
            System.err.println("JSON工具测试失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试PDF解析器初始化
     */
    private static void testPdfParserInitialization() {
        System.out.println("\n3. 测试PDF解析器初始化...");
        
        try {
            PdfInvoiceParser parser = new PdfInvoiceParser();
            System.out.println("PDF解析器初始化成功！");
            
            // 测试验证方法
            InvoiceInfo testInvoice = new InvoiceInfo();
            testInvoice.setInvoiceNumber("TEST123");
            testInvoice.setTotalAmount(new BigDecimal("100.00"));
            
            boolean isValid = parser.validateInvoiceInfo(testInvoice);
            System.out.println("发票信息验证结果: " + (isValid ? "通过" : "失败"));
            
        } catch (Exception e) {
            System.err.println("PDF解析器测试失败: " + e.getMessage());
        }
    }
}