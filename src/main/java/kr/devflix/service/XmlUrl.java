package kr.devflix.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.SimpleDateFormat;
import java.util.Date;

@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "url")
@Getter
public class XmlUrl {

    private static final String PRIORRITY_HIGH = "1.0";
    private static final String PRIORRITY_MEDIUM = "0.5";

    @XmlElement
    private String loc;

    @XmlElement
    private String lastmod = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    @XmlElement
    private String changefreq = "daily";

    @XmlElement
    private String priority;

    public XmlUrl() {
    }

    public XmlUrl(final String log, final String priority) {
        this.loc = log;
        this.priority = priority;
    }
}
