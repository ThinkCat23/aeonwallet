package com.aeon.app.ui.blog;

/*
Copyright 2020 ivoryguru

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;

import com.aeon.app.ui.contact.ContactContent;
import com.aeon.app.ui.contact.ContactFragment;
import com.aeon.app.ui.transfer.TransferFragment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BlogContent {

    public static final List<Blog> ITEMS = new ArrayList<Blog>();
    public static final Map<String, Blog> ITEM_MAP = new HashMap<String, Blog>();

    static {
        new Thread(new Runnable(){
        @Override
        public void run() {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document doc = null;
            try {
                doc = db.parse(new URL("https://medium.com/feed/@aeon-community").openStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            if(doc!=null){
                NodeList blogs = doc.getElementsByTagName("item");
                for (int temp = 0; temp < blogs.getLength(); temp++) {
                    Node blog = blogs.item(temp);
                    NodeList childList = blog.getChildNodes();
                    Bitmap img = null;
                    String title = null;
                    String url = null;
                    Boolean isBlogPost = false;
                    for (int temp2 = 0; temp2 < childList.getLength(); temp2++) {
                        Node childNode = childList.item(temp2);
                        if ("title".equals(childNode.getNodeName())) {
                            title= childList.item(temp2).getTextContent();
                        }
                        else if ("content:encoded".equals(childNode.getNodeName())) {
                            String pattern = "<img alt=\"[^\"]*\" src=\"([^\"]+)\" \\/>";
                            String content = childList.item(temp2).getTextContent();
                            Pattern p = Pattern.compile(pattern);
                            Matcher m = p.matcher(content);
                            if(m.find()){
                                img = getBitmapFromURL(m.group(1));
                            }
                        }
                        else if ("link".equals(childNode.getNodeName())) {
                            url =childList.item(temp2).getTextContent();
                        }
                        else if ("category".equals(childNode.getNodeName())) {
                            isBlogPost = true;
                        }
                    }
                    if(title!=null && isBlogPost) {
                        ITEMS.add(new Blog(title, url, img));
                    }
                }
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TransferFragment.blogAdapter.notifyDataSetChanged();
                }
            });
        }
            }).start();
    }
    public static class Blog {
        public final String name;
        public final String url;
        public final Bitmap image;

        public Blog(String name, String url, Bitmap image) {
            this.name = name;
            this.url = url;
            this.image = image;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            int width, height;
            height = myBitmap.getHeight();
            width = myBitmap.getWidth();

            Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas c = new Canvas(bmpGrayscale);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(myBitmap, 0, 0, paint);
            return bmpGrayscale;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
