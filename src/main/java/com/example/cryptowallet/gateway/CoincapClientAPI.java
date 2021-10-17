package com.example.cryptowallet.gateway;

import com.example.cryptowallet.gateway.response.AssetCoincapResponse;
import com.example.cryptowallet.gateway.response.AssetHistoryResponse;
import com.example.cryptowallet.gateway.response.AssetItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
@Service
public class CoincapClientAPI {

    CloseableHttpClient httpClient = HttpClients.createDefault();
    public List<AssetItem> getAssets() throws IOException {
        HttpGet request = new HttpGet("https://api.coincap.io/v2/assets");
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                ObjectMapper objectMapper = new ObjectMapper();

                AssetCoincapResponse assetCoincapResponse = objectMapper.readValue(response.getEntity().getContent(), AssetCoincapResponse.class);

                return assetCoincapResponse.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public double getAssetPrice(String id) throws IOException {
        HttpGet request = new HttpGet("https://api.coincap.io/v2/assets/" + id + "/history?interval=d1&start=1617753600000&end=1617753601000");
        try {
            CloseableHttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                ObjectMapper objectMapper = new ObjectMapper();

                AssetHistoryResponse assetHistoryResponse = objectMapper.readValue(response.getEntity().getContent(), AssetHistoryResponse.class);
                return assetHistoryResponse.getData().get(assetHistoryResponse.getData().size() - 1).getPriceUsd();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }
}
