package com.tc.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tc.example.Application;
import com.tc.produce.Sender;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class demo {
	@Autowired
    private Sender sender;
    @Test
    public void hello() throws Exception {
        sender.send();
    }
}
