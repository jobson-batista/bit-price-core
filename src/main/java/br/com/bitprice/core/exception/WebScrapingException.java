package br.com.bitprice.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebScrapingException extends RuntimeException {

    private String message = "Error when searching for products";
    private String description = "Unable to perform web scraping.";
}
