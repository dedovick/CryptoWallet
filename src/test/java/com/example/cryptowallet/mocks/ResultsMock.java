package com.example.cryptowallet.mocks;

import com.example.cryptowallet.gateway.response.AssetItem;

import java.util.ArrayList;
import java.util.List;

public final class ResultsMock {

    public static List<AssetItem> getAssetsSuccessMock(){
        List<AssetItem> assets = new ArrayList<AssetItem>();
        assets.add(AssetItem
                .builder()
                .id("bitcoin")
                .symbol("BTC")
                .build());
        assets.add(AssetItem
                .builder()
                .id("ethereum")
                .symbol("ETH")
                .build());
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
