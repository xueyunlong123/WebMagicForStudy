package webmagic.downloader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import webmagic.Page;
import webmagic.Request;
import webmagic.Site;
import webmagic.Task;
import webmagic.selector.PlainText;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xueyunlong on 16-12-16.
 * use abuyun proxy
 */
@Slf4j
public class HttpClientUtilProxyAbuYun implements Downloader {

    /**
     * Downloads web pages and store in Page object.
     *
     * @param request request
     * @param task    task
     * @return page
     */
    @Override
    public Page download(Request request, Task task) {
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }
        CloseableHttpClient httpClient = null;

        String result = null;
        HttpResponse response = null;
        //准备代理服务身份验证信息
        //代理服务器
        final String ProxyHost = "proxy.abuyun.com";
        final Integer ProxyPort =9020;

        // 代理隧道验证信息
        final String ProxyUser = "HML66NR29K9JC6BD";
        final String ProxyPass = "5C16D27CB4D877FA";

        HttpHost target = new HttpHost(ProxyHost, ProxyPort, "http");

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(ProxyUser, ProxyPass));
        Page page =  new Page();
        try{
            //准备request信息
            String charset = "GBK";
            if(StringUtils.isNotEmpty(site.getCharset())){
                charset=site.getCharset();
            }
            Map<String, String> headers = site.getHeaders();
            List headers1 = new ArrayList();
            if(headers.size() != 0){
                headers.forEach((key,value)->{
                    BasicHeader header = new BasicHeader(key,value);
                    headers1.add(header);
                });
            }

            //Create httpclient use headers，credsProvider
            BasicCookieStore basicCookieStore = new BasicCookieStore();
            BasicClientCookie basicClientCookie = new BasicClientCookie("cna","/Ya1EP5jrVsCAWombe313kW4");
            basicClientCookie.setDomain(".1688.com");
            basicCookieStore.addCookie(basicClientCookie);
            httpClient = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider)
                    .setDefaultHeaders(headers1)
//                    .setDefaultCookieStore(basicCookieStore)
                    .build();
            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local
            // auth cache
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(target, basicAuth);
            // Add AuthCache to the execution context
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);
            if (request.getMethod() == null || request.getMethod().equals("GET")){
                HttpGet httpGet = new HttpGet(request.getUrl().toString());
                response = httpClient.execute(target,httpGet,localContext);
            }else if (request.getMethod().equals("POST")){
                HttpPost httpPost = new HttpPost();
                response = httpClient.execute(target,httpPost,localContext);
            }

//            if(site.getBody() != null){
//                HttpEntity httpEntity = new StringEntity(site.getBody());
//                httpPost.setEntity(httpEntity);
//            }

            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                    log.info(result);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        page.setRawText(result);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        log.info("downloader by HttpClientUtilProxyAbuYun");
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }
}
