package com.example.cryptowallet.useCase;

import com.example.cryptowallet.entrypoint.response.WalletPerformanceResponse;
import com.example.cryptowallet.gateway.CoincapClientAPI;
import com.example.cryptowallet.mocks.ResultsMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CalculateCryptoWalletPerformanceUCTest {
    private CalculateCryptoWalletPerformanceUC calculateCryptoWalletPerformanceUC;

    @Mock
    private CoincapClientAPI client = new CoincapClientAPI();

    @BeforeEach
    void initUseCase() {
        MockitoAnnotations.openMocks(this);
        calculateCryptoWalletPerformanceUC = new CalculateCryptoWalletPerformanceUC(client);
    }

    @Test
    void executeUCWithSucess() {
        // Given I have the services mocked to return success
        try {
            when(client.getAssets())
                    .thenReturn(ResultsMock.getAssetsSuccessMock());
            when(client.getAssetPrice("bitcoin"))
                    .thenReturn(ResultsMock.getAssetPrice1SuccessMock());
            when(client.getAssetPrice("ethereum"))
                    .thenReturn(ResultsMock.getAssetPrice2SuccessMock());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // When the Use Case is executed
            final WalletPerformanceResponse response = calculateCryptoWalletPerformanceUC.execute();

            // Then response should has the expected values
            assertEquals(445467.97, response.getTotal());
            assertEquals("ETH", response.getBestAsset());
            assertEquals(77.22, response.getBestPerformance());
            assertEquals("BTC", response.getWorstAsset());
            assertEquals(14.0, response.getWorstPerformance());
        } catch (Exception e) {
            assertEquals(false, true);
        }
    }

    @Test
    void executeUCWithAssetsApiError() {
        // Given I have the assets api mocked with error
        try {
            when(client.getAssets())
                    .thenReturn(ResultsMock.getAssetsSuccessMock());
            when(client.getAssetPrice("bitcoin"))
                    .thenReturn(ResultsMock.getAssetPriceErroMock());
            when(client.getAssetPrice("ethereum"))
                    .thenReturn(ResultsMock.getAssetPrice2SuccessMock());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // When the Use Case is executed
            final WalletPerformanceResponse response = calculateCryptoWalletPerformanceUC.execute();

            // Then response should return exception
            assertEquals(false, true);
        } catch (Exception e) {
            assertEquals(true, true);
        }
    }

    @Test
    void executeUCWithPriceApiError() {
        // Given I have the price api mocked with error
        try {
            when(client.getAssets())
                    .thenReturn(ResultsMock.getAssetsErrorMock());
            when(client.getAssetPrice("bitcoin"))
                    .thenReturn(ResultsMock.getAssetPrice1SuccessMock());
            when(client.getAssetPrice("ethereum"))
                    .thenReturn(ResultsMock.getAssetPrice2SuccessMock());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // When the Use Case is executed
            final WalletPerformanceResponse response = calculateCryptoWalletPerformanceUC.execute();

            // Then response should return exception
            assertEquals(false, true);
        } catch (Exception e) {
            assertEquals(true, true);
        }
    }

}
