package muyu.trade.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClient {

    public String post(String urlString, HashMap<String, String> data) throws Exception
    {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        StringBuilder dataString = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = data.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            dataString.append(entry.getKey());
            dataString.append("=");
            dataString.append(entry.getValue());
            if(iterator.hasNext())
                dataString.append("&");
        }
        dos.writeBytes(dataString.toString());
        dos.flush();
        dos.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String brLine;
        StringBuilder content = new StringBuilder();
        while ((brLine = br.readLine()) != null)
            content.append(brLine);
        br.close();
        return content.toString();
    }
}
