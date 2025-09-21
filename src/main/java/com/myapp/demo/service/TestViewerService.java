package com.myapp.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.myapp.demo.Repository.ITestViewerService;
import com.myapp.demo.testview.TestView;

import io.jsonwebtoken.Claims;

@Service
public class TestViewerService implements ITestViewerService{
	TestView tv = new TestView();
	@Override
	public String getDiffernce() throws Exception {
		String template_file = "C:\\Users\\ounim\\Documents\\exercice\\htm1.html";
		FileReader fw = new FileReader(new File(template_file), StandardCharsets.UTF_8);
		BufferedReader bw = new BufferedReader(fw);

		String html1 = "";
		String strCurrentLine1 = "";
		while ((strCurrentLine1 = bw.readLine()) != null) {
			html1 = html1 + "\r\n" + strCurrentLine1;
		}
		bw.close();

		String template_file2 = "C:\\Users\\ounim\\Documents\\exercice\\htm2.html";
		FileReader fw2 = new FileReader(new File(template_file2), StandardCharsets.UTF_8);
		BufferedReader bw2 = new BufferedReader(fw2);

		String html2 = "";
		String strCurrentLine2 = "";
		while ((strCurrentLine2 = bw2.readLine()) != null) {
			html2 = html2 + "\r\n" + strCurrentLine2;
		}
		bw2.close();
		Element E1 = Jsoup.parse(html1);
		Element E2 = Jsoup.parse(html2);
		return tv.compare(E1, E2);
	}

}
