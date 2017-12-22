package com.kfr2359.homefinancemobile.logic;

import com.kfr2359.homefinancemobile.MainActivity;
import com.kfr2359.homefinancemobile.storage.MoneyOpsLocalStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoneyOp extends GenericEntity {
    private Float amount;
    private String desciption;
    private Date op_date;
    private Item item;
    private DateFormat df;

    public MoneyOp(Integer id, Float amount, String desciption, Date op_date, Item item) {
        super(id);
        this.amount = amount;
        this.desciption = desciption;
        this.op_date = op_date;
        this.item = item;
        df = new SimpleDateFormat(MainActivity.DATEFORMAT_PATTERN);
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Date getOpDate() {
        return op_date;
    }

    public void setOpDate(Date op_date) {
        this.op_date = op_date;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return df.format(op_date);
    }
}
