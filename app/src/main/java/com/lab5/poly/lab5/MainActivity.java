package com.lab5.poly.lab5;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Adapter adapter;
    List<Model> models;
    LinearLayoutManager layoutManager;
    RecyclerView lvListNews;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edUrl);

        button = findViewById(R.id.btnDowndLoad);
        lvListNews = findViewById(R.id.rvList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadRssFromInternetTask loadRssFromInternetTask = new LoadRssFromInternetTask(MainActivity.this);
                String urr = editText.getText().toString().trim();
                loadRssFromInternetTask.execute("https://news.zing.vn/rss/giao-duc.rss");
            }
        });


    }

    class LoadRssFromInternetTask extends AsyncTask<String, Long, List<Model>> {


        private Context context;

        public LoadRssFromInternetTask(Context context) {
            this.context = context;
            Log.e("START", "START");

        }


        // ham xu ly ngam
        @Override
        protected List<Model> doInBackground(String... strings) {


            ArrayList<Model> newsArrayList = new ArrayList();

            try {
                URL url = new URL(strings[0]);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();

                // We will get the XML from an input stream
                xpp.setInput(getInputStream(url), "UTF_8");


                /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
                 * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
                 * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
                 * so we should skip the "<title>" tag which is a child of "<channel>" tag,
                 * and take in consideration only "<title>" tag which is a child of "<item>"
                 *
                 * In order to achieve this, we will make use of a boolean variable.
                 */

                // Returns the type of current event: START_TAG, END_TAG, etc..
                int eventType = xpp.getEventType();
                String text = "";

                Model news = null;
                // den end_document thi ket thuc
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String nameTag = xpp.getName();
                    switch (eventType) {
                        //bat dau doc tu 1 tag hoac 1 the
                        //name tag la ten the
                        case XmlPullParser.START_TAG:

                            Log.e("Name", xpp.getName());
                            if (nameTag.equalsIgnoreCase("item")) {
                                news = new Model();
                                Log.e("CREATE", "NEWS");
                            }
                            break;
                        // den khi co doan text
                        case XmlPullParser.TEXT:
                            text = xpp.getText();
                            break;
                        //den khi co the dong
                        case XmlPullParser.END_TAG:
                            if (nameTag.equals("item"))
                                newsArrayList.add(news);
                            else if (news != null & nameTag.equalsIgnoreCase("title"))
                                news.title = text.trim();
                            else if (news != null & nameTag.equalsIgnoreCase("link"))
                                news.link = text.trim();
                            else if (news != null & nameTag.equalsIgnoreCase("pubDate"))
                                news.pubDate = text.trim();
                            else if (news!=null & nameTag.equalsIgnoreCase("description"))
                                news.description = text.trim();
                            else if (news!=null & nameTag.equalsIgnoreCase("image"))
                                news.image = text.trim();
                            Log.e("END_TAG " + nameTag, text + "");
                            break;
                        default:
                            break;

                    }
                    //di chuyen den the khac
                    eventType = xpp.next(); //move to next element
                }

                Log.e("SIZE", newsArrayList.size() + "");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsArrayList;
        }

        private InputStream getInputStream(URL url) {
            try {
                return url.openConnection().getInputStream();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Model> news) {
            super.onPostExecute(news);

            adapter = new Adapter(context, news);
            layoutManager = new LinearLayoutManager(context);
            lvListNews.setLayoutManager(layoutManager);
            lvListNews.setAdapter(adapter);

        }
    }
}
