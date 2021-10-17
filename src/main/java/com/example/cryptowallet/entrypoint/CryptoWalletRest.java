package com.example.cryptowallet.entrypoint;

import com.example.cryptowallet.entrypoint.response.WalletPerformanceResponse;
import com.example.cryptowallet.gateway.CoincapClientAPI;
import com.example.cryptowallet.useCase.CalculateCryptoWalletPerformanceUC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoWalletRest {



    @GetMapping("/hello")
    public ResponseEntity<?> sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        CoincapClientAPI client = new CoincapClientAPI();
        CalculateCryptoWalletPerformanceUC calculatePerformanceUC = new CalculateCryptoWalletPerformanceUC(client);
        WalletPerformanceResponse response = null;
        try {
            response = calculatePerformanceUC.execute();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Limit reached", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
