package Model;

import java.math.BigDecimal;

public class Product {
    private Integer id;

    private String guid;

    private Integer stockid;

    private Integer barcodeid;

    private Integer skuid;

    private String productname;

    private String productstyle;

    private BigDecimal price;

    private java.util.Date createtime;

    private Short status;

    private Integer count;

    private java.util.Date modifytime;

    private byte[] timestamp;

    public Product(Integer id, String guid, Integer stockid, Integer barcodeid, Integer skuid, String productname, String productstyle, BigDecimal price, java.util.Date createtime, Short status, Integer count, java.util.Date modifytime) {
        this.id = id;
        this.guid = guid;
        this.stockid = stockid;
        this.barcodeid = barcodeid;
        this.skuid = skuid;
        this.productname = productname;
        this.productstyle = productstyle;
        this.price = price;
        this.createtime = createtime;
        this.status = status;
        this.count = count;
        this.modifytime = modifytime;
    }

    public Product(Integer id, String guid, Integer stockid, Integer barcodeid, Integer skuid, String productname, String productstyle, BigDecimal price, java.util.Date createtime, Short status, Integer count, java.util.Date modifytime, byte[] timestamp) {
        this.id = id;
        this.guid = guid;
        this.stockid = stockid;
        this.barcodeid = barcodeid;
        this.skuid = skuid;
        this.productname = productname;
        this.productstyle = productstyle;
        this.price = price;
        this.createtime = createtime;
        this.status = status;
        this.count = count;
        this.modifytime = modifytime;
        this.timestamp = timestamp;
    }

    public Product() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid == null ? null : guid.trim();
    }

    public Integer getStockid() {
        return stockid;
    }

    public void setStockid(Integer stockid) {
        this.stockid = stockid;
    }

    public Integer getBarcodeid() {
        return barcodeid;
    }

    public void setBarcodeid(Integer barcodeid) {
        this.barcodeid = barcodeid;
    }

    public Integer getSkuid() {
        return skuid;
    }

    public void setSkuid(Integer skuid) {
        this.skuid = skuid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    public String getProductstyle() {
        return productstyle;
    }

    public void setProductstyle(String productstyle) {
        this.productstyle = productstyle == null ? null : productstyle.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public java.util.Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(java.util.Date createtime) {
        this.createtime = createtime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public java.util.Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(java.util.Date modifytime) {
        this.modifytime = modifytime;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }
}
