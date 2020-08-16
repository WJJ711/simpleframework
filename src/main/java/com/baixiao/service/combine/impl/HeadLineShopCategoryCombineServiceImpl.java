package com.baixiao.service.combine.impl;



import com.baixiao.entity.bo.HeadLine;
import com.baixiao.entity.bo.ShopCategory;
import com.baixiao.entity.dto.MainPageInfoDTO;
import com.baixiao.entity.dto.Result;
import com.baixiao.service.combine.HeadLineShopCategoryCombineService;
import com.baixiao.service.solo.HeadLineService;
import com.baixiao.service.solo.ShopCategoryService;
import org.simpleframework.core.annotation.Service;
import org.simpleframework.core.inject.annotation.Autowired;

import java.util.List;

@Service("HeadLineShopCategoryCombineServiceImpl")
public class HeadLineShopCategoryCombineServiceImpl implements HeadLineShopCategoryCombineService {
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Override
    public Result<MainPageInfoDTO> getMainPageInfo() {
        System.out.println("com.baixiao.service.combine.impl.HeadLineShopCategoryCombineServiceImpl.getMainPageInfo执行了");
        //1.获取头条列表
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        Result<List<HeadLine>> HeadLineResult = headLineService.queryHeadLine(headLineCondition, 1, 4);
        //2.获取店铺类别列表
        ShopCategory shopCategoryCondition = new ShopCategory();
        Result<List<ShopCategory>> shopCategoryResult =shopCategoryService.queryShopCategory(shopCategoryCondition, 1, 100);
        //3.合并两者并返回
        Result<MainPageInfoDTO> result = mergeMainPageInfoResult(HeadLineResult, shopCategoryResult);
        return result;
    }

    private Result<MainPageInfoDTO> mergeMainPageInfoResult(Result<List<HeadLine>> headLineResult, Result<List<ShopCategory>> shopCategoryResult) {
        return  null;
    }
}
