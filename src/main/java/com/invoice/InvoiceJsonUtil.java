package com.invoice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 发票信息JSON处理工具类
 */
public class InvoiceJsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceJsonUtil.class);
    private static final ObjectMapper objectMapper = createObjectMapper();
    
    /**
     * 创建ObjectMapper实例
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
    
    /**
     * 将发票信息保存为JSON文件
     * 
     * @param invoiceInfo 发票信息
     * @param jsonFile JSON文件
     * @throws IOException IO异常
     */
    public static void saveToJson(InvoiceInfo invoiceInfo, File jsonFile) throws IOException {
        try {
            objectMapper.writeValue(jsonFile, invoiceInfo);
            logger.info("发票信息已保存到JSON文件: {}", jsonFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("保存JSON文件失败: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 从JSON文件读取发票信息
     * 
     * @param jsonFile JSON文件
     * @return 发票信息
     * @throws IOException IO异常
     */
    public static InvoiceInfo loadFromJson(File jsonFile) throws IOException {
        try {
            InvoiceInfo invoiceInfo = objectMapper.readValue(jsonFile, InvoiceInfo.class);
            logger.info("从JSON文件加载发票信息: {}", jsonFile.getAbsolutePath());
            return invoiceInfo;
        } catch (IOException e) {
            logger.error("读取JSON文件失败: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 将发票信息转换为JSON字符串
     * 
     * @param invoiceInfo 发票信息
     * @return JSON字符串
     * @throws IOException 转换异常
     */
    public static String toJsonString(InvoiceInfo invoiceInfo) throws IOException {
        try {
            return objectMapper.writeValueAsString(invoiceInfo);
        } catch (IOException e) {
            logger.error("转换为JSON字符串失败: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * 从JSON字符串解析发票信息
     * 
     * @param jsonString JSON字符串
     * @return 发票信息
     * @throws IOException 解析异常
     */
    public static InvoiceInfo fromJsonString(String jsonString) throws IOException {
        try {
            return objectMapper.readValue(jsonString, InvoiceInfo.class);
        } catch (IOException e) {
            logger.error("从JSON字符串解析失败: {}", e.getMessage(), e);
            throw e;
        }
    }
}