package com.handlers;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileHandler {
    private static Logger log = LoggerFactory.getLogger(FileHandler.class);

    public FileHandler sendFile(WebElement element, String filePath){
        File file = new File(filePath);
        element.sendKeys(file.getAbsolutePath());
        log.info("File send");
        return this;
    }
}
