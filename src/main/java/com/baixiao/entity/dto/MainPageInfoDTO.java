package com.baixiao.entity.dto;


import com.baixiao.entity.bo.HeadLine;
import com.baixiao.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

@Data
public class MainPageInfoDTO {
    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;
}
