package com.example.androiddevapp.bean;

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2021, by Sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: FeatureItem
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2022/2/25 2:32 下午
 */
public class FeatureItem {
    public String itemName;
    public String itemLogo;

    public FeatureItem(String itemName, String itemLogo) {
        this.itemName = itemName;
        this.itemLogo = itemLogo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemLogo() {
        return itemLogo;
    }

    public void setItemLogo(String itemLogo) {
        this.itemLogo = itemLogo;
    }
}
