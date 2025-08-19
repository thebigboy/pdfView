package com.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * 电子发票信息实体类
 */
public class InvoiceInfo {
    private String invoiceNumber;           // 发票号码
    private String invoiceCode;             // 发票代码
    private LocalDate invoiceDate;          // 开票日期
    private String sellerName;              // 销售方名称
    private String sellerTaxNumber;         // 销售方纳税人识别号
    private String buyerName;               // 购买方名称
    private String buyerTaxNumber;          // 购买方纳税人识别号
    private BigDecimal totalAmount;         // 价税合计金额
    private BigDecimal taxAmount;           // 税额
    private BigDecimal amountWithoutTax;    // 不含税金额
    private String remarks;                 // 备注
    private List<InvoiceItem> items;        // 发票明细项目

    public InvoiceInfo() {
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxNumber() {
        return sellerTaxNumber;
    }

    public void setSellerTaxNumber(String sellerTaxNumber) {
        this.sellerTaxNumber = sellerTaxNumber;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxNumber() {
        return buyerTaxNumber;
    }

    public void setBuyerTaxNumber(String buyerTaxNumber) {
        this.buyerTaxNumber = buyerTaxNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getAmountWithoutTax() {
        return amountWithoutTax;
    }

    public void setAmountWithoutTax(BigDecimal amountWithoutTax) {
        this.amountWithoutTax = amountWithoutTax;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== 电子发票信息 ===\n");
        sb.append("发票号码: ").append(invoiceNumber).append("\n");
        sb.append("发票代码: ").append(invoiceCode).append("\n");
        sb.append("开票日期: ").append(invoiceDate).append("\n");
        sb.append("销售方名称: ").append(sellerName).append("\n");
        sb.append("销售方税号: ").append(sellerTaxNumber).append("\n");
        sb.append("购买方名称: ").append(buyerName).append("\n");
        sb.append("购买方税号: ").append(buyerTaxNumber).append("\n");
        sb.append("价税合计: ¥").append(totalAmount).append("\n");
        sb.append("税额: ¥").append(taxAmount).append("\n");
        sb.append("不含税金额: ¥").append(amountWithoutTax).append("\n");
        sb.append("备注: ").append(remarks).append("\n");
        
        if (!items.isEmpty()) {
            sb.append("=== 发票明细 ===\n");
            for (int i = 0; i < items.size(); i++) {
                sb.append("项目 ").append(i + 1).append(": ").append(items.get(i)).append("\n");
            }
        }
        
        return sb.toString();
    }

    /**
     * 发票明细项目类
     */
    public static class InvoiceItem {
        private String itemName;            // 项目名称
        private String specification;       // 规格型号
        private String unit;               // 单位
        private BigDecimal quantity;       // 数量
        private BigDecimal unitPrice;      // 单价
        private BigDecimal amount;         // 金额
        private BigDecimal taxRate;        // 税率
        private BigDecimal taxAmount;      // 税额

        // Getters and Setters
        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }

        public BigDecimal getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
        }

        @Override
        public String toString() {
            return String.format("%s [规格: %s, 数量: %s %s, 单价: ¥%s, 金额: ¥%s, 税率: %s%%, 税额: ¥%s]",
                    itemName, specification, quantity, unit, unitPrice, amount, 
                    taxRate != null ? taxRate.multiply(BigDecimal.valueOf(100)) : "0", taxAmount);
        }
    }
}