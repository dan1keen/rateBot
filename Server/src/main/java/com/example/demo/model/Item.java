package com.example.demo.model;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private int id;
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_descr")
    private String descr;
    @Column(name = "item_change")
    private String change;
    @Column(name = "item_published")
    private String pubDate;
    @Column(name = "item_date")
    private Timestamp createDate;


    public int getId() {
        return id;
    }

    public Item() {
    }

    public Item(String descr) {
        this.descr = descr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
