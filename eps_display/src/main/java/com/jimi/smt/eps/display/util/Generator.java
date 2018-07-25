package com.jimi.smt.eps.display.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		try {
		List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        URL url = Generator.class.getResource("/mybatis/generatorConfig.xml");
        File configFile = new File(url.getPath());
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        System.out.println("ok");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
