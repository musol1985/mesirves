package com.mesirves.app.components.rest;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mesirves.app.json.ResponseJSON;
import com.mesirves.app.json.request.SavePreviewJSON;

@RestController
@RequestMapping("ws/editor")
public class EditorREST {
	final static Logger log = LogManager.getLogger(EditorREST.class);

	@RequestMapping(method=RequestMethod.GET)
    public String current() {
		return "OK";     
    }
	
	@RequestMapping(method=RequestMethod.POST)
    public ResponseJSON<Object> saveImage(ModelMap model, @RequestBody SavePreviewJSON body)throws Exception {
        
        byte[] data = Base64.getDecoder().decode(body.imageData.replaceAll("^data:image\\/png;base64,", ""));
        try (OutputStream stream = new FileOutputStream("c:/tmp/abc.png")) {
            stream.write(data);
        }
		
		return new ResponseJSON<Object>(0);
    }
}

