package com.invoice;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PDF电子发票解析器
 */
public class PdfInvoiceParser {
    private static final Logger logger = LoggerFactory.getLogger(PdfInvoiceParser.class);
    
    // 常用的正则表达式模式
    private static final Pattern INVOICE_NUMBER_PATTERN = Pattern.compile("发票号码[：:\\s]*([0-9]{8,25})");
    private static final Pattern INVOICE_CODE_PATTERN = Pattern.compile("发票代码[：:\\s]*([0-9]{10,15})");
    private static final Pattern DATE_PATTERN = Pattern.compile("开票日期[：:\\s]*(\\d{4}年\\d{1,2}月\\d{1,2}日|\\d{4}-\\d{1,2}-\\d{1,2}|\\d{4}/\\d{1,2}/\\d{1,2})");
    
    // 改进的销售方和购买方信息匹配模式（基于实际文本格式）
    private static final Pattern SELLER_NAME_PATTERN = Pattern.compile("名称[：：]([^\\n\\r]+?)\\n统一社会信用代码/纳税人识别号[：：]([A-Z0-9]{15,20})\\n名称[：：]");
    private static final Pattern SELLER_TAX_PATTERN = Pattern.compile("名称[：：]([^\\n\\r]+?)\\n统一社会信用代码/纳税人识别号[：：]([A-Z0-9]{15,20})");
    
    // 购买方信息（第二个出现的名称和税号）
    private static final Pattern BUYER_NAME_PATTERN = Pattern.compile("统一社会信用代码/纳税人识别号[：：][A-Z0-9]{15,20}\\n名称[：：]([^\\n\\r]+?)\\n统一社会信用代码/纳税人识别号[：：]([A-Z0-9]{15,20})");
    private static final Pattern BUYER_TAX_PATTERN = Pattern.compile("名称[：：][^\\n\\r]+?\\n统一社会信用代码/纳税人识别号[：：][A-Z0-9]{15,20}\\n名称[：：][^\\n\\r]+?\\n统一社会信用代码/纳税人识别号[：：]([A-Z0-9]{15,20})");
    
    // 改进的金额匹配模式
    private static final Pattern TOTAL_AMOUNT_PATTERN = Pattern.compile("价\\s*税\\s*合\\s*计[\\s\\S]*?[（\\(]\\s*小\\s*写\\s*[）\\)][\\s]*([0-9,]+\\.?[0-9]*)[¥￥]?");
    private static final Pattern TAX_AMOUNT_PATTERN = Pattern.compile("合\\s*计[\\s\\S]*?([0-9,]+\\.?[0-9]*)[¥￥][\\s]*([0-9,]+\\.?[0-9]*)[¥￥]");
    private static final Pattern AMOUNT_WITHOUT_TAX_PATTERN = Pattern.compile("合\\s*计[\\s]*([0-9,]+\\.?[0-9]*)[¥￥]");
    
    /**
     * 解析PDF发票文件
     * 
     * @param pdfFile PDF文件
     * @return 解析后的发票信息
     * @throws IOException 文件读取异常
     */
    public InvoiceInfo parsePdfInvoice(File pdfFile) throws IOException {
        logger.info("开始解析PDF发票文件: {}", pdfFile.getAbsolutePath());
        
        InvoiceInfo invoiceInfo = new InvoiceInfo();
        
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);
            
            logger.debug("提取的PDF文本内容: \n{}", text);
            
            // 解析各个字段
            parseInvoiceNumber(text, invoiceInfo);
            parseInvoiceCode(text, invoiceInfo);
            parseInvoiceDate(text, invoiceInfo);
            parseSellerInfo(text, invoiceInfo);
            parseBuyerInfo(text, invoiceInfo);
            parseAmountInfo(text, invoiceInfo);
            parseRemarks(text, invoiceInfo);
            parseInvoiceItems(text, invoiceInfo);
            
        } catch (IOException e) {
            logger.error("解析PDF文件时出错: {}", e.getMessage(), e);
            throw e;
        }
        
        logger.info("PDF发票解析完成");
        return invoiceInfo;
    }
    
    /**
     * 解析发票号码
     */
    private void parseInvoiceNumber(String text, InvoiceInfo invoiceInfo) {
        Matcher matcher = INVOICE_NUMBER_PATTERN.matcher(text);
        if (matcher.find()) {
            invoiceInfo.setInvoiceNumber(matcher.group(1));
            logger.debug("解析到发票号码: {}", matcher.group(1));
        }
    }
    
    /**
     * 解析发票代码
     */
    private void parseInvoiceCode(String text, InvoiceInfo invoiceInfo) {
        Matcher matcher = INVOICE_CODE_PATTERN.matcher(text);
        if (matcher.find()) {
            invoiceInfo.setInvoiceCode(matcher.group(1));
            logger.debug("解析到发票代码: {}", matcher.group(1));
        }
    }
    
    /**
     * 解析开票日期
     */
    private void parseInvoiceDate(String text, InvoiceInfo invoiceInfo) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            String dateStr = matcher.group(1);
            LocalDate date = parseDate(dateStr);
            if (date != null) {
                invoiceInfo.setInvoiceDate(date);
                logger.debug("解析到开票日期: {}", date);
            }
        }
    }
    
    /**
     * 解析销售方信息
     */
    private void parseSellerInfo(String text, InvoiceInfo invoiceInfo) {
        // 使用更直接的方法：查找第一个"名称："和"统一社会信用代码/纳税人识别号："
        Pattern namePattern = Pattern.compile("名称[：：]([^\\n\\r]+?)\\n");
        Pattern taxPattern = Pattern.compile("统一社会信用代码/纳税人识别号[：：]([A-Z0-9]{15,20})");
        
        Matcher nameMatcher = namePattern.matcher(text);
        Matcher taxMatcher = taxPattern.matcher(text);
        
        // 第一个匹配的是销售方
        if (nameMatcher.find()) {
            invoiceInfo.setSellerName(nameMatcher.group(1).trim());
            logger.debug("解析到销售方名称: {}", nameMatcher.group(1).trim());
        }
        
        if (taxMatcher.find()) {
            invoiceInfo.setSellerTaxNumber(taxMatcher.group(1));
            logger.debug("解析到销售方税号: {}", taxMatcher.group(1));
        }
    }
    
    /**
     * 解析购买方信息
     */
    private void parseBuyerInfo(String text, InvoiceInfo invoiceInfo) {
        // 查找所有"名称："和"统一社会信用代码/纳税人识别号："，第二个匹配的是购买方
        Pattern namePattern = Pattern.compile("名称[：：]([^\\n\\r]+?)\\n");
        Pattern taxPattern = Pattern.compile("统一社会信用代码/纳税人识别号[：：]([A-Z0-9]{15,20})");
        
        Matcher nameMatcher = namePattern.matcher(text);
        Matcher taxMatcher = taxPattern.matcher(text);
        
        // 跳过第一个匹配（销售方），取第二个匹配（购买方）
        if (nameMatcher.find() && nameMatcher.find()) {
            invoiceInfo.setBuyerName(nameMatcher.group(1).trim());
            logger.debug("解析到购买方名称: {}", nameMatcher.group(1).trim());
        }
        
        if (taxMatcher.find() && taxMatcher.find()) {
            invoiceInfo.setBuyerTaxNumber(taxMatcher.group(1));
            logger.debug("解析到购买方税号: {}", taxMatcher.group(1));
        }
    }
    
    /**
     * 解析金额信息
     */
    private void parseAmountInfo(String text, InvoiceInfo invoiceInfo) {
        // 解析价税合计 - 从小写金额中提取
        Matcher totalMatcher = TOTAL_AMOUNT_PATTERN.matcher(text);
        if (totalMatcher.find()) {
            String amountStr = totalMatcher.group(1).replace(",", "");
            try {
                BigDecimal totalAmount = new BigDecimal(amountStr);
                invoiceInfo.setTotalAmount(totalAmount);
                logger.debug("解析到价税合计: {}", totalAmount);
            } catch (NumberFormatException e) {
                logger.warn("解析价税合计金额失败: {}", amountStr);
            }
        }
        
        // 解析合计行的不含税金额和税额
        Matcher taxMatcher = TAX_AMOUNT_PATTERN.matcher(text);
        if (taxMatcher.find()) {
            try {
                // 第一个数字是不含税金额，第二个数字是税额
                String amountWithoutTaxStr = taxMatcher.group(1).replace(",", "");
                String taxAmountStr = taxMatcher.group(2).replace(",", "");
                
                BigDecimal amountWithoutTax = new BigDecimal(amountWithoutTaxStr);
                BigDecimal taxAmount = new BigDecimal(taxAmountStr);
                
                invoiceInfo.setAmountWithoutTax(amountWithoutTax);
                invoiceInfo.setTaxAmount(taxAmount);
                
                logger.debug("解析到不含税金额: {}", amountWithoutTax);
                logger.debug("解析到税额: {}", taxAmount);
            } catch (NumberFormatException e) {
                logger.warn("解析合计行金额失败: {}", e.getMessage());
            }
        }
        
        // 如果上面的方法没有成功，尝试单独解析不含税金额
        if (invoiceInfo.getAmountWithoutTax() == null) {
            Matcher amountMatcher = AMOUNT_WITHOUT_TAX_PATTERN.matcher(text);
            if (amountMatcher.find()) {
                String amountStr = amountMatcher.group(1).replace(",", "");
                try {
                    BigDecimal amountWithoutTax = new BigDecimal(amountStr);
                    invoiceInfo.setAmountWithoutTax(amountWithoutTax);
                    logger.debug("解析到不含税金额: {}", amountWithoutTax);
                } catch (NumberFormatException e) {
                    logger.warn("解析不含税金额失败: {}", amountStr);
                }
            }
        }
    }
    
    /**
     * 解析备注信息
     */
    private void parseRemarks(String text, InvoiceInfo invoiceInfo) {
        Pattern remarksPattern = Pattern.compile("备注[：:]\\s*([^\\n\\r]+)");
        Matcher matcher = remarksPattern.matcher(text);
        if (matcher.find()) {
            invoiceInfo.setRemarks(matcher.group(1).trim());
            logger.debug("解析到备注: {}", matcher.group(1));
        }
    }
    
    /**
     * 解析发票明细项目
     */
    private void parseInvoiceItems(String text, InvoiceInfo invoiceInfo) {
        List<InvoiceInfo.InvoiceItem> items = new ArrayList<>();
        
        // 针对这种发票格式的明细解析
        // 匹配格式: *经纪代理服务*代订住 次 1 720.75 720.75 6% 43.25
        Pattern itemPattern = Pattern.compile("\\*([^*]+)\\*([^\\n\\r]*?)\\s+(\\S+)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?%)\\s+(\\d+(?:\\.\\d+)?)");
        Matcher matcher = itemPattern.matcher(text);
        
        while (matcher.find()) {
            InvoiceInfo.InvoiceItem item = new InvoiceInfo.InvoiceItem();
            try {
                String itemName = matcher.group(1).trim() + matcher.group(2).trim();
                item.setItemName(itemName);
                item.setUnit(matcher.group(3));
                item.setQuantity(new BigDecimal(matcher.group(4)));
                item.setUnitPrice(new BigDecimal(matcher.group(5)));
                item.setAmount(new BigDecimal(matcher.group(6)));
                
                // 解析税率（去掉%符号）
                String taxRateStr = matcher.group(7).replace("%", "");
                BigDecimal taxRate = new BigDecimal(taxRateStr).divide(BigDecimal.valueOf(100));
                item.setTaxRate(taxRate);
                
                item.setTaxAmount(new BigDecimal(matcher.group(8)));
                
                items.add(item);
                logger.debug("解析到发票项目: {}", item);
            } catch (NumberFormatException e) {
                logger.warn("解析发票项目时出错: {}", e.getMessage());
            }
        }
        
        // 如果上面的模式没有匹配到，尝试更通用的模式
        if (items.isEmpty()) {
            // 尝试更宽松的匹配模式
            Pattern generalPattern = Pattern.compile("([^\\n\\r]+?)\\s+(\\S+)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?%)\\s+(\\d+(?:\\.\\d+)?)");
            Matcher generalMatcher = generalPattern.matcher(text);
            
            while (generalMatcher.find()) {
                InvoiceInfo.InvoiceItem item = new InvoiceInfo.InvoiceItem();
                try {
                    item.setItemName(generalMatcher.group(1).trim());
                    item.setUnit(generalMatcher.group(2));
                    item.setQuantity(new BigDecimal(generalMatcher.group(3)));
                    item.setUnitPrice(new BigDecimal(generalMatcher.group(4)));
                    item.setAmount(new BigDecimal(generalMatcher.group(5)));
                    
                    String taxRateStr = generalMatcher.group(6).replace("%", "");
                    BigDecimal taxRate = new BigDecimal(taxRateStr).divide(BigDecimal.valueOf(100));
                    item.setTaxRate(taxRate);
                    
                    item.setTaxAmount(new BigDecimal(generalMatcher.group(7)));
                    
                    items.add(item);
                    logger.debug("解析到发票项目（通用模式）: {}", item);
                } catch (NumberFormatException e) {
                    logger.warn("解析发票项目时出错（通用模式）: {}", e.getMessage());
                }
            }
        }
        
        invoiceInfo.setItems(items);
    }
    
    /**
     * 解析日期字符串
     */
    private LocalDate parseDate(String dateStr) {
        // 支持多种日期格式
        String[] patterns = {
            "yyyy年M月d日",
            "yyyy-M-d",
            "yyyy/M/d"
        };
        
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // 继续尝试下一种格式
            }
        }
        
        logger.warn("无法解析日期格式: {}", dateStr);
        return null;
    }
    
    /**
     * 验证发票信息的完整性
     */
    public boolean validateInvoiceInfo(InvoiceInfo invoiceInfo) {
        if (invoiceInfo == null) {
            return false;
        }
        
        // 检查必要字段
        boolean isValid = invoiceInfo.getInvoiceNumber() != null 
                && !invoiceInfo.getInvoiceNumber().isEmpty()
                && invoiceInfo.getTotalAmount() != null
                && invoiceInfo.getTotalAmount().compareTo(BigDecimal.ZERO) > 0;
        
        if (!isValid) {
            logger.warn("发票信息验证失败，缺少必要字段");
        }
        
        return isValid;
    }
}