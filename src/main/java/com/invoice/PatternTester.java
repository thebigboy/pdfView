package com.invoice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式模式测试器
 */
public class PatternTester {
    
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
        
        System.out.println("=== 正则表达式模式测试 ===\n");
        
        // 测试发票号码
        testPattern("发票号码", "发票号码[：:\\s]*([0-9]{8,25})", text);
        
        // 测试开票日期
        testPattern("开票日期", "开票日期[：:\\s]*(\\d{4}年\\d{1,2}月\\d{1,2}日|\\d{4}-\\d{1,2}-\\d{1,2}|\\d{4}/\\d{1,2}/\\d{1,2})", text);
        
        // 测试销售方名称
        testPattern("销售方名称", "销售方[\\s\\S]*?名称[：:]([^\\n\\r]+?)(?=\\n|统一社会信用代码|纳税人识别号)", text);
        
        // 测试销售方税号
        testPattern("销售方税号", "销售方[\\s\\S]*?(?:统一社会信用代码/纳税人识别号|纳税人识别号)[：:]([A-Z0-9]{15,20})", text);
        
        // 测试购买方名称
        testPattern("购买方名称", "购买方[\\s\\S]*?名称[：:]([^\\n\\r]+?)(?=\\n|统一社会信用代码|纳税人识别号)", text);
        
        // 测试购买方税号
        testPattern("购买方税号", "购买方[\\s\\S]*?(?:统一社会信用代码/纳税人识别号|纳税人识别号)[：:]([A-Z0-9]{15,20})", text);
        
        // 测试价税合计
        testPattern("价税合计", "价\\s*税\\s*合\\s*计[\\s\\S]*?[（\\(]\\s*小\\s*写\\s*[）\\)][\\s]*([0-9,]+\\.?[0-9]*)[¥￥]?", text);
        
        // 测试合计行（不含税金额和税额）
        testPattern("合计行", "合\\s*计[\\s\\S]*?([0-9,]+\\.?[0-9]*)[¥￥][\\s]*([0-9,]+\\.?[0-9]*)[¥￥]", text);
        
        // 测试发票明细项目
        testPattern("发票明细", "\\*([^*]+)\\*([^\\n\\r]*?)\\s+(\\S+)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?)\\s+(\\d+(?:\\.\\d+)?%)\\s+(\\d+(?:\\.\\d+)?)", text);
        
        System.out.println("=== 测试完成 ===");
    }
    
    private static void testPattern(String patternName, String regex, String text) {
        System.out.println("测试 " + patternName + ":");
        System.out.println("正则表达式: " + regex);
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        
        boolean found = false;
        while (matcher.find()) {
            found = true;
            System.out.print("匹配结果: ");
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.print("组" + i + "=[" + matcher.group(i) + "] ");
            }
            System.out.println();
        }
        
        if (!found) {
            System.out.println("❌ 未找到匹配");
        } else {
            System.out.println("✅ 匹配成功");
        }
        
        System.out.println("----------------------------------------\n");
    }
}