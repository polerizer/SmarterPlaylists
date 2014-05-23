/**
 * Nov 25, 2013
 * YouTubeAuthorization.java
 * Daniel Pok
 * AP Java 6th
 */
package musicproviders;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

/**Implements the YouTube Devices OAuth2 Authorization Flow which requires users to authorize using a different device
 * but saves me from writing a browser control into this application.
 * @author poler_000
 *
 */
public class YouTubeAuthorization {
	
	private static final HttpTransport transport = new NetHttpTransport();
	private static final JsonFactory jsonFactory = new JacksonFactory();
	
	private String client_id, client_secret;
	private static final String AUTH_SERVER = "https://accounts.google.com/o/oauth2/device/code";
	private static final String TOKEN_SERVER = "https://accounts.google.com/o/oauth2/token";
	private static final String DEFAULT_SCOPE = "https://gdata.youtube.com https://www.googleapis.com/auth/youtube";
	private String grant_type = "http://oauth.net/grant_type/device/1.0";
	private String device_code, user_code, verificationURL;
	private String access_token, refresh_token, token_type = "Bearer";
	private Long expires_in;
	
	public YouTubeAuthorization(String client_id, String client_secret){
		this.client_id = client_id;
		this.client_secret = client_secret;
	}
	
	public String getToken() throws MalformedURLException, IOException{
		requestDeviceCode();
		String errorText = "error: haven't tried yet";
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		while(errorText.startsWith("error")){
			System.out.print("Visit the url and enter the given token... Press Enter after you authenticate.");
			cin.readLine();
			errorText = requestTokens();
		}
		return access_token;
	}
	
	public GoogleCredential getGoogleCredential() throws MalformedURLException, IOException{
		String token = getToken();
		return new GoogleCredential.Builder().setTransport(transport).setJsonFactory(jsonFactory)
				.setClientSecrets(client_id, client_secret).build()
				.setAccessToken(token).setExpiresInSeconds(expires_in)
				.setRefreshToken(refresh_token);
	}
	
	public String requestDeviceCode() throws MalformedURLException, IOException{
		return requestDeviceCode(DEFAULT_SCOPE);
	}
	
	public String requestDeviceCode(String scopes) throws MalformedURLException, IOException{
		String urlParameters = String.format("client_id=%s&scope=%s", URLEncoder.encode(client_id, "UTF-8"), URLEncoder.encode(scopes, "UTF-8"));
		String request = AUTH_SERVER;
		URL url = new URL(request); 
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("Host", "accounts.google.com");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setUseCaches (false);

		JSONObject response = doPostGetJSON(connection, urlParameters);
		device_code = response.getString("device_code");
		user_code = response.getString("user_code");
		verificationURL = (String) response.get("verification_url");
		expires_in = response.getLong("expires_in");
		//System.out.println("device_code: " + device_code);
		System.out.println("user_code: " + user_code);
		System.out.println("verification url: " + verificationURL);
		System.out.println("expires_in: " + expires_in + " seconds");
		return response.toString();
	}
	
	public String requestTokens() throws IOException{
		String urlParameters = String.format("client_id=%s&client_secret=%s&" +
				"code=%s&grant_type=%s", URLEncoder.encode(client_id, "UTF-8"), URLEncoder.encode(client_secret, "UTF-8"),
				URLEncoder.encode(device_code, "UTF-8"), URLEncoder.encode(grant_type, "UTF-8"));
		//System.out.println(urlParameters);
		String request = TOKEN_SERVER;
		URL url = new URL(request); 
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("Host", "accounts.google.com");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setUseCaches (false);

		JSONObject response = doPostGetJSON(connection, urlParameters);
		if(response.has("error")) return "error: " + response.getString("error");
		access_token = response.getString("access_token");
		token_type = response.getString("token_type");
		refresh_token = response.getString("refresh_token");
		expires_in = response.getLong("expires_in");
		return response.toString();
	}
	
	public String refreshToken() throws IOException{
		String urlParameters = String.format("client_id=%s&client_secret=%s&" +
				"refresh_token=%s&grant_type=%s", URLEncoder.encode(client_id, "UTF-8"), URLEncoder.encode(client_secret, "UTF-8"),
				URLEncoder.encode(refresh_token, "UTF-8"), URLEncoder.encode("refresh_token", "UTF-8"));
		//System.out.println(urlParameters);
		String request = TOKEN_SERVER;
		URL url = new URL(request); 
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("Host", "accounts.google.com");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setUseCaches (false);

		JSONObject response = doPostGetJSON(connection, urlParameters);
		if(response.has("error")) return "error: " + response.getString("error");
		access_token = response.getString("access_token");
		token_type = response.getString("token_type");
		expires_in = response.getLong("expires_in");
		return response.toString();
	}
	
	private JSONObject doPostGetJSON(HttpURLConnection connection, String urlParameters) throws IOException{
		connection.connect();
		
		OutputStream os = connection.getOutputStream();
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
		wr.write(urlParameters);
		wr.flush();
		wr.close();
		
		InputStream inputStream = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		String responseString = "", decodedString;
		while ((decodedString = in.readLine()) != null) {
		    if(!responseString.isEmpty()) responseString += String.format("%n");
			responseString += decodedString;
		}
		in.close();
		connection.disconnect();
		JSONObject response = new JSONObject(responseString);
		return response;
	}
	
	public String tokenType(){
		return token_type;
	}
	
}
