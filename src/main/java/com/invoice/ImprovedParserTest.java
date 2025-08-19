package com.invoice;

import java.math.BigDecimal;

/**
 * 改进后解析器的测试程序
 */
public class ImprovedParserTest {
    
    public static void main(String[] args) {
        // 提供的PDF文本内容
        String text = " 开票人： 孟婕\n" +
                "didi\n" +
                "电子发票（增值税专用发票） 发票号码: 25117000001017822384\n" +
                "开票日期: 2025年08月14日\n" +
                "备\n" +
                "注\n" +
                "销\n" +
                "售\n" +
                "方\n" +
                "信\n" +
                "息\n" +
                "购\n" +
                "买\n" +
                "方\n" +
                "信\n" +
                "息\n" +
                "名称：南京医购优选供应链管理有限公司\n" +
                "统一社会信用代码/纳税人识别号：91320104MA27BF34X1\n" +
                "名称：北京小桔国际旅行社有限公司\n" +
                "统一社会信用代码/纳税人识别号：91110108MA01RDGF56\n" +
                "项目名称 规格型号 单 位 数 量 单 价 金 额 税率/征收率 税 额\n" +
                "*经纪代理服务*代订住 次 1 720.75 720.75 6% 43.25\n" +
                "宿费\n" +
                "合 计 720.75¥ 43.25¥\n" +
                "价 税 合 计 （ 大 写 ） （ 小 写 ） 764.00¥\n" +
                "购方开户银行:中国银行股份有限公司南京新城科技园支行    银行账号:479376924681\n" +
                "购方地址:南京市秦淮区永丰大道36号南京天安数码城01幢11层1103~1105室    电话:025-68538253\n" +
                "柒佰陆拾肆圆整";
        
        System.out.println("=== 改进后的解析器测试 ===\n");
        
        try {
            // 创建发票信息对象
            InvoiceInfo invoiceInfo = new InvoiceInfo();
            
            // 创建解析器实例
            PdfInvoiceParser parser = new PdfInvoiceParser();
            
            // 手动调用各个解析方法（模拟解析过程）
            System.out.println("开始解析各个字段...\n");
            
            // 使用反射调用私有方法进行测试
            java.lang.reflect.Method parseInvoiceNumberMethod = parser.getClass().getDeclaredMethod("parseInvoiceNumber", String.class, InvoiceInfo.class);
            parseInvoiceNumberMethod.setAccessible(true);
            parseInvoiceNumberMethod.invoke(parser, text, invoiceInfo);
            
            java.lang.reflect.Method parseInvoiceDateMethod = parser.getClass().getDeclaredMethod("parseInvoiceDate", String.class, InvoiceInfo.class);
            parseInvoiceDateMethod.setAccessible(true);
            parseInvoiceDateMethod.invoke(parser, text, invoiceInfo);
            
            java.lang.reflect.Method parseSellerInfoMethod = parser.getClass().getDeclaredMethod("parseSellerInfo", String.class, InvoiceInfo.class);
            parseSellerInfoMethod.setAccessible(true);
            parseSellerInfoMethod.invoke(parser, text, invoiceInfo);
            
            java.lang.reflect.Method parseBuyerInfoMethod = parser.getClass().getDeclaredMethod("parseBuyerInfo", String.class, InvoiceInfo.class);
            parseBuyerInfoMethod.setAccessible(true);
            parseBuyerInfoMethod.invoke(parser, text, invoiceInfo);
            
            java.lang.reflect.Method parseAmountInfoMethod = parser.getClass().getDeclaredMethod("parseAmountInfo", String.class, InvoiceInfo.class);
            parseAmountInfoMethod.setAccessible(true);
            parseAmountInfoMethod.invoke(parser, text, invoiceInfo);
            
            java.lang.reflect.Method parseInvoiceItemsMethod = parser.getClass().getDeclaredMethod("parseInvoiceItems", String.class, InvoiceInfo.class);
            parseInvoiceItemsMethod.setAccessible(true);
            parseInvoiceItemsMethod.invoke(parser, text, invoiceInfo);
            
            // 输出解析结果
            System.out.println("=== 解析结果 ===");
            System.out.println("发票号码: " + invoiceInfo.getInvoiceNumber());
            System.out.println("开票日期: " + invoiceInfo.getInvoiceDate());
            System.out.println("销售方名称: " + invoiceInfo.getSellerName());
            System.out.println("销售方税号: " + invoiceInfo.getSellerTaxNumber());
            System.out.println("购买方名称: " + invoiceInfo.getBuyerName());
            System.out.println("购买方税号: " + invoiceInfo.getBuyerTaxNumber());
            System.out.println("价税合计: ¥" + invoiceInfo.getTotalAmount());
            System.out.println("不含税金额: ¥" + invoiceInfo.getAmountWithoutTax());
            System.out.println("税额: ¥" + invoiceInfo.getTaxAmount());
            System.out.println("发票明细项目数: " + invoiceInfo.getItems().size());
            
            if (!invoiceInfo.getItems().isEmpty()) {
                System.out.println("\n发票明细:");
                for (int i = 0; i < invoiceInfo.getItems().size(); i++) {
                    InvoiceInfo.InvoiceItem item = invoiceInfo.getItems().get(i);
                    System.out.println("  项目" + (i + 1) + ": " + item.getItemName());
                    System.out.println("    单位: " + item.getUnit());
                    System.out.println("    数量: " + item.getQuantity());
                    System.out.println("    单价: ¥" + item.getUnitPrice());
                    System.out.println("    金额: ¥" + item.getAmount());
                    System.out.println("    税率: " + (item.getTaxRate() != null ? item.getTaxRate().multiply(BigDecimal.valueOf(100)) + "%" : "未知"));
                    System.out.println("    税额: ¥" + item.getTaxAmount());
                }
            }
            
            // 验证解析结果
            System.out.println("\n=== 验证结果 ===");
            boolean isValid = parser.validateInvoiceInfo(invoiceInfo);
            System.out.println("验证结果: " + (isValid ? "✅ 通过" : "❌ 失败"));
            
        } catch (Exception e) {
            System.err.println("测试过程中出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}