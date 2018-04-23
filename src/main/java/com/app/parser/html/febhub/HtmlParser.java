package com.app.parser.html.febhub;

import com.app.commons.BaseUtils;
import com.app.parser.html.febhub.vo.ArticleVO;
import com.app.support.file.FlatFileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 解讀 http://www.febhub.com/forum.php
 * <p>
 * 使用方式：
 * 1. 將任一分類網址貼上
 * 2. 在 output 查看解析後連結
 */

public class HtmlParser {

    private static final String URL_ROOT = "http://www.febhub.com/";
    private static final String URL_PAGE_PETTERN = "http://www.febhub.com/forum-126-%s.html";
    private static final String URL_PAGE_1 = "http://www.febhub.com/forum-126-1.html";
    private static final int PAGE_COUNT = 11;

    public static void main(String[] args) throws Exception {
///**
        FlatFileWriter fileWriter = new FlatFileWriter();
        fileWriter.setFilePath("src/main/java/com/app/output");
        fileWriter.setFileName("text.txt");
        fileWriter.open();



        Document doc = Jsoup.connect(URL_PAGE_1).get();

        //網頁 title
        System.out.println(doc.title());

        //分頁數
//        int pageCount = getPageCount(doc);
//        System.out.println("page count = " + pageCount);


        for (int i = 7; i <= PAGE_COUNT; i++) {
            Document tempDoc = Jsoup.connect(String.format(URL_PAGE_PETTERN, i)).get();
            //當前頁文章、Url 列表
            List<ArticleVO> articleList = getArticleList(tempDoc);
            for(ArticleVO articleVO : articleList){
                Random ran = new Random();
                int inrRandom = ran.nextInt(500) + 500;
                Thread.sleep(1000 + inrRandom);
                //取得文章中 Media Url
                String aritleUrl = URL_ROOT + articleVO.url;
//                System.out.println("articleUrl = " + aritleUrl);

                try{
                    List<String> strings = getMediaUrl(URL_ROOT + articleVO.url);
                    for(String mediaUrl : strings){
                        System.out.println("mediaUrl = " + mediaUrl +", page = " +i);
                        fileWriter.writeLine(mediaUrl);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        fileWriter.close();
// **/

//        //取得文章中 Media Url
//        List<String> strings = getMediaUrl("http://www.febhub.com/thread-14463-1-1.html");
//        for(String mediaUrl : strings){
//            System.out.println("mediaUrl = " + mediaUrl );
//        }
    }

    private static List<ArticleVO> getArticleList(Document doc) {
        Element elementTable = doc.getElementById("threadlisttableid");
//        System.out.println(elementTable.html());

        List<ArticleVO> list = new ArrayList<>();

        Elements elements = elementTable.getElementsByClass("s xst");
//        System.out.println("elements size = " + elements.size());

//        System.out.println("-------------------------");
        for (Element element : elements) {
//            System.out.println(element.html());
//            System.out.println(element.attr("href"));
            ArticleVO articleVO = new ArticleVO();
            articleVO.url = element.attr("href");
            list.add(articleVO);
        }


        return list;
    }


    private static int getPageCount(Document doc) {
        Element elementSpan = doc.getElementById("fd_page_bottom");
        System.out.println (elementSpan.html());

        Elements elementLabel = elementSpan.getElementsByClass("last");
        String raw = elementLabel.first().html();
        System.out.println (raw);

        int pageCount = 1;
        try {
            pageCount = Integer.parseInt(raw.replaceAll("\\.", "").trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return pageCount;
    }

    /**
     * 發表日往後取5天
     * @param articleUrl
     * @return
     * @throws Exception
     */
    private static List<String> getMediaUrl(String articleUrl) throws Exception {
        Document doc = Jsoup.connect(articleUrl).get();
        System.out.println (doc.html());
        Elements elementsAuthi = doc.getElementsByClass("pti");
//        System.out.println (elementsAuthi.size());

        Element elementPti = elementsAuthi.first();
//        System.out.println (elementPti.html());

        Element elementEm = elementPti.getElementsByTag("em").first();
        String raw = (elementEm.html());
        raw = raw.replaceAll("发表于", "").trim();

        Date date = BaseUtils.stringToDate(raw, "yyyy-MM-dd HH:mm:ss");

        //日期格式化
        String formatStr = BaseUtils.dateFormat(date, "yyyyMM/dd");
//        System.out.println ("formatStr = " + formatStr);

        /** 取檔案名稱邏輯有點爆炸 **/
//        Elements elementsJP = doc.getElementsByClass("jp-type-single");
        Elements elementsJP = doc.getElementsByClass("jp-gui jp-interface");
//        System.out.println(elementsJP.size());
//        System.out.println(elementsJP.first().html());

        Element elementJp = elementsJP.first();

        Elements elementsDiv = elementJp.getElementsByTag("div");


        //倒數第N的div
        String mediaName = elementsDiv.get(elementsDiv.size() - 3).html();
//        System.out.println("####" + mediaName);



        List<String> strList = new ArrayList<>();

        int i = 0;
        while(i<5){
            String dateFormat =  BaseUtils.addDate(date, i, "yyyyMM/dd");
            strList.add("http://eggtar.qiniudn.com/forum/" + dateFormat + "/" + mediaName);
            i++;
        }


        return strList;
    }


}
