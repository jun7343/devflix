package kr.devflix.service;

import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

@Service
@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "urlset", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
public class XmlUrlSet {

    @XmlElements({@XmlElement(name = "url", type = XmlUrl.class)})
    private List<XmlUrl> xmlUrlList = new LinkedList<>();

    public boolean addUrl(XmlUrl xmlUrl) {
        return xmlUrlList.add(xmlUrl);
    }

    public List<XmlUrl> getXmlUrlList() {
        return xmlUrlList;
    }
}
