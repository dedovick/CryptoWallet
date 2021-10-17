package com.example.cryptowallet.useCase;

import com.example.cryptowallet.entity.Asset;
import com.example.cryptowallet.entrypoint.response.WalletPerformanceResponse;
import com.example.cryptowallet.gateway.CoincapClientAPI;
import com.example.cryptowallet.gateway.response.AssetItem;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CalculateCryptoWalletPerformanceUC {

    private int count;
    private boolean error;
    private CoincapClientAPI client;

    public CalculateCryptoWalletPerformanceUC(CoincapClientAPI client) {
        this.client = client;
        this.count = 0;
        this.error = false;
    }

    public WalletPerformanceResponse execute() throws Exception {

        WalletPerformanceResponse response = new WalletPerformanceResponse();
        Resource resource = new ClassPathResource("wallet.csv");

        try (BufferedReader csvReader = new BufferedReader(new FileReader(resource.getFile()))) {
            List<Asset> assets = new ArrayList<Asset>();
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] attributes = row.split(";");
                assets.add(Asset
                        .builder()
                        .symbol(attributes[0])
                        .quantity(Double.valueOf(attributes[1].replaceAll(",", ".")))
                        .price(Double.valueOf(attributes[2].replaceAll(",", ".")))
                        .build());
            }

            ExecutorService pool = Executors.newFixedThreadPool(3);

            List<AssetItem> assetItens = getAssetsFromAPI(0);
            for (Asset asset : assets){
                Optional<AssetItem> assetItem = assetItens.stream().filter(line -> line.getSymbol().equals(asset.getSymbol())).findFirst();
                if(!assetItem.isEmpty()){
                    Runnable r = new Runnable(){
                        public void run() {
                            double price = 0;
                            try {
                                price = getAssetPriceFromAPI(assetItem.get().getId(), 0);
                                updateData(asset, price, response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            increaseCount();
                        }
                    };
                    pool.execute(r);
                }else {
                    increaseCount();
                }
            }
            while (count < assets.size());

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if(error){
            throw new Exception();
        }
        BigDecimal bigDecimal = new BigDecimal(response.getTotal());
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        response.setTotal(bigDecimal.doubleValue());
        return response;
    }

    private double getAssetPriceFromAPI(String id, int attempts) throws IOException {

        try{
            double assetPrice = this.client.getAssetPrice(id);
            return assetPrice;
        }catch (IOException e){
            if (attempts > 4){
                return getAssetPriceFromAPI(id, attempts + 1);
            }
            setErrorTrue();
            throw e;
        }
    }

    private List<AssetItem> getAssetsFromAPI(int attempts) throws IOException {

        try {
            List<AssetItem> assetItens = this.client.getAssets();
            return assetItens;
        } catch (IOException e) {
            e.printStackTrace();
            if (attempts > 4){
                return getAssetsFromAPI(attempts + 1);
            }
            setErrorTrue();
            throw e;
        }
    }

    private synchronized void updateData(Asset asset, double priceUsd, WalletPerformanceResponse response){
         response.setTotal(response.getTotal() + (asset.getQuantity() * priceUsd));
        BigDecimal bigDecimal = new BigDecimal(priceUsd/asset.getPrice());
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        double performance = bigDecimal.doubleValue();
        if(null == response.getBestAsset() || response.getBestPerformance() < performance){
            response.setBestPerformance(performance);
            response.setBestAsset(asset.getSymbol());
        }
        if(null == response.getWorstAsset() || response.getWorstPerformance() > performance){
            response.setWorstPerformance(performance);
            response.setWorstAsset(asset.getSymbol());
        }
    }

    private synchronized void increaseCount(){
        this.count++;
    }

    private void setErrorTrue(){
        this.error = true;
    }
}
