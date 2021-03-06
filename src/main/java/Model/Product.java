package Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Integer id;

    private String guid;

    private Integer stockid;

    private Integer barcodeid;

    private Integer skuid;

    private String productname;

    private String productstyle;

    private BigDecimal price;

    private LocalDateTime createtime;

    private Short status;

    private Integer count;

    private LocalDateTime modifytime;
    //  java.util.Date
    //  sql  byte[] 不能用Byte[]     //byte[] 不能用Byte[]。因为反射赋值时候找不到对应类型。fastJson
   // mysql  Long
    private byte[] timestamp;

//    private Long timestamp;

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

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public void setCreatetime(LocalDateTime createtime) {
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

    public LocalDateTime getModifytime() {
        return modifytime;
    }

    public void setModifytime(LocalDateTime modifytime) {
        this.modifytime = modifytime;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }


//    public Long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Long timestamp) {
//        this.timestamp = timestamp;
//    }
}
