package com.log8430.group9;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandController {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/command/path")
    public File path(@RequestParam(value="name") String name) {
    	return new File(name, "test");
    }
}
