package me.kaimu.hastebin;

import me.revoltix.moduleloader.exceptions.ErrorInPostHastebinException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class Hastebin {

	public String post(String text, boolean raw) {
		byte[] postData = text.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;

		String requestURL = "https://hastebin.com/documents";
		URL url = null;
		try {
			url = new URL(requestURL);
		} catch (MalformedURLException e) {
			try {
				throw new ErrorInPostHastebinException("Failed to open stream to URL", e);
			} catch (ErrorInPostHastebinException ex) {
				ex.printStackTrace();
			}
		}
		HttpsURLConnection conn = null;
		try {
			assert url != null;
			conn = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			try {
				throw new ErrorInPostHastebinException("Hastebin isn't reachable", e);
			} catch (ErrorInPostHastebinException ex) {
				ex.printStackTrace();
			}
		}
		assert conn != null;
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			try {
				throw new ErrorInPostHastebinException("POST can't be activated", e);
			} catch (ErrorInPostHastebinException ex) {
				ex.printStackTrace();
			}
		}
		conn.setRequestProperty("User-Agent", "Hastebin Java Api");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);

		String response = null;
		DataOutputStream wr;
		try {
			wr = new DataOutputStream(conn.getOutputStream());
			wr.write(postData);
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			response = reader.readLine();
		} catch (IOException e) {
			try {
				throw new ErrorInPostHastebinException("Post to Hastebin Failed", e);
			} catch (ErrorInPostHastebinException ex) {
				ex.printStackTrace();
			}
		}

		assert response != null;
		if (response.contains("\"key\"")) {
			response = response.substring(response.indexOf(":") + 2, response.length() - 2);
		
			String postURL = raw ? "https://www.toptal.com/developers/hastebin/raw/" : "https://www.toptal.com/developers/hastebin/";
			response = postURL + response;
		}
		
		return response;
	}

}
