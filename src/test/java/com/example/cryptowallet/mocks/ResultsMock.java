package com.example.cryptowallet.mocks;

import com.example.cryptowallet.gateway.response.AssetItem;

import java.util.ArrayList;
import java.util.List;

public final class ResultsMock {

    public static List<AssetItem> getAssetsSuccessMock(){
        List<AssetItem> assets = new ArrayList<AssetItem>();
        AssetItem item1 = new AssetItem();
        item1.setId("bitcoin");
        item1.setSymbol("BTC");
        assets.add(item1);

        AssetItem item2 = new AssetItem();
        item2.setId("ethereum");
        item2.setSymbol("ETH");
        assets.add(item2);

        return  assets;
    }

    public static double getAssetPrice1SuccessMock(){
        return Double.valueOf("55000.0001");
    }

    public static double getAssetPrice2SuccessMock(){
        return Double.valueOf("31000.0001");
    }

    public static List<AssetItem> getAssetsErrorMock() throws Exception {
        throw new Exception();
    }

    public static double getAssetPriceErroMock() throws Exception {
        throw new Exception();
    }
}
