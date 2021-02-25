package com.devflix;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.DefaultJavaScriptErrorListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
public class SpringRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setJavaScriptErrorListener(new DefaultJavaScriptErrorListener());
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.waitForBackgroundJavaScript(3000);

        HtmlPage page = webClient.getPage(new URL("https://tech.kakao.com/blog/"));


        for (HtmlElement element : ul) {
            System.out.println(element.asXml());
        }
    }
}
