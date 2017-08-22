package com.tc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tc.produce.Sender;

@RestController
public class ComputeController {
	@Autowired
    private Sender sender;
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws Exception {
		sender.send();
		return "vdsa";
	}
}
