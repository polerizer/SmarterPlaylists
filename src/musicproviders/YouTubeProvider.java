/**
 * Nov 25, 2013
 * YouTubeProvider.java
 * Daniel Pok
 * AP Java 6th
 */
package musicproviders;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.api.services.youtube.model.ResourceId;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;

import data.Track;

/**
 * @author poler_000
 *
 */
public class YouTubeProvider implements MusicProvider {

	private YouTube youtube;
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static String DEFAULT_API_ENDPOINT = "http://gdata.youtube.com/feeds/api/videos";
	public static String DEFAULT_APPNAME = "smarterplaylisttestapp";
	private String apiEndpoint;
	private YouTubeService service;
	private Credential credential;
	private String client_id, client_secret;

	public YouTubeProvider(){
		service = new YouTubeService(DEFAULT_APPNAME);
	}

	public YouTubeProvider(String appName, String clientId, String clientSecret){
		service = new YouTubeService(appName);
		client_id = clientId;
		client_secret = clientSecret;
		apiEndpoint = DEFAULT_API_ENDPOINT;
		instantiateYouTube(appName);
	}

	public YouTubeProvider(String appName, String clientId, String clientSecret, CredentialPack credPack){
		service = new YouTubeService(appName);
		client_id = clientId;
		client_secret = clientSecret;
		apiEndpoint = DEFAULT_API_ENDPOINT;
		Credential cred = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY)
				.setClientSecrets(clientId, clientSecret).build()
				.setAccessToken(credPack.access_token).setRefreshToken(credPack.refresh_token);

		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred).setApplicationName(appName).build();
	}

	public void instantiateYouTube(String appName){
		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize()).setApplicationName(appName).build();
	}

	public Credential authorize() {
		YouTubeAuthorization auth = new YouTubeAuthorization(client_id, client_secret);
		GoogleCredential cred;
		try {
			cred = auth.getGoogleCredential();
			credential = cred;
			return cred;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}

	public String manualProviderSideID(){
		return manualProviderSideID(null, null, null);
	}

	public String manualProviderSideID(String artist, String track, String album){
		BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
		PrintStream cout = System.out;

		if(artist == null && track == null && album == null){
			artist = (artist != null? artist : "");
			track = (track != null? track : "");
			album = (album != null? album : "");
			cout.println("Find Youtube Video: ");
		} else{
			artist = (artist != null? artist : "");
			track = (track != null? track : "");
			album = (album != null? album : "");
			cout.printf("Find Youtube Video for %s by %s on %s: %n", track, artist, album);
		}

		while(true){
			try {
				YouTubeQuery query = new YouTubeQuery(new URL(apiEndpoint));
				query = new YouTubeQuery(new URL(apiEndpoint));

				cout.printf("Query String (default %s %s %s): %n", artist, track, album);
				String line = cin.readLine();
				if(line == null || line.isEmpty()){
					line = String.format("%s %s %s", artist, track, album);
				}
				if(line.isEmpty()){
					cout.println("We can't execute a blank query. Try again? (Y/N): ");
					line = cin.readLine();
					if(!line.toLowerCase().equals("y")){
						return null;
					} else{
						continue;
					}
				}
				query.setFullTextQuery(line);
				
				//published, rating, relevance, view_count, updated
				cout.println("Order By [RELEVANCE, RATING, VIEW_COUNT, PUBLISHED](default RELEVANCE): ");
				line = cin.readLine().toUpperCase();
				if(line.equals("RELEVANCE")){
					query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
				}else if (line.equals("RATING")){
					query.setOrderBy(YouTubeQuery.OrderBy.RATING);
				}else if (line.equals("VIEW_COUNT")){
					query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
				}else if (line.equals("PUBLISHED")){
					query.setOrderBy(YouTubeQuery.OrderBy.PUBLISHED);
				}else{
					query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
				}

				query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
				VideoFeed videoFeed = service.query(query, VideoFeed.class);
				List<VideoEntry> videos = videoFeed.getEntries();
				if (videos == null || videos.size() == 0){
					cout.print("No videos found. Try again? (Y/N): ");
					line = cin.readLine();
					if(!line.toLowerCase().equals("y")){
						return null;
					}
				} else{
					cout.println("Found " + videos.size() + " videos:");
					for(int i = 0; i < videos.size(); i++){
						cout.println("\n================= Entry " + i + "=================");
						printVideo(videos.get(i));
					}
					
					while(true){
						cout.print("Pick a video number (or \"quit\",\"retry\"): ");
						line = cin.readLine();
						if(line.toLowerCase().equals("quit")){
							return null; 
						} else if(line.toLowerCase().equals("retry")){
							break;
						} else{
							try{
								return videos.get(Integer.parseInt(line)).getMediaGroup().getVideoId();
							} catch(Exception ex){
								cout.println("Could not locate the video with that number.");
							}
						}
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (ServiceException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public String printVideo(VideoEntry video){
		String title = video.getTitle().getPlainText();
		String id = video.getMediaGroup().getVideoId();
		Long views = (video.getStatistics() != null? video.getStatistics().getViewCount() : -1);
		Float rating = (video.getRating() != null? video.getRating().getAverage() : 0.0f);
		String uploader = video.getMediaGroup().getUploader();
		String description = video.getMediaGroup().getDescription().getPlainTextContent();
		String duration = secondsToTime((video.getMediaGroup().getYouTubeContents().size() > 0 ? video.getMediaGroup().getYouTubeContents().get(0).getDuration() : 0));
		String result = String.format("[Title]: %s [ID]: %s [Length]: %s%n[Views]: %d [Rating]: %f[Uploader]: %s %n[Description]: %s",
				title, id, duration, views, rating,
				uploader, description);

		System.out.println(result);
		return result;
	}
	
	public String secondsToTime(int sec){
		int days = (int) sec / 86400;
		sec = sec % 86400;
		int hours = (int) sec / 3600;
		sec = sec % 3600;
		int mins = (int) sec / 60;
		sec = sec % 60;
		
		String result = "";
		if(days > 0){
			result += String.format("%d Days ", days);
		}
		if(hours > 0){
			result += String.format("%d Hours ", hours);
		}
		if(mins > 0){
			result += String.format("%d Minutes ", mins);
		}
		result += String.format("%d Seconds", sec);
		return result;
	}
	
	@Override
	public String getProviderSideID(String artist, String track, String album) {
		return getProviderSideID(artist, track, album, false);
	}

	public String getProviderSideID(String artist, String track, String album, boolean prompt) {
		artist = (artist != null? artist : "");
		track = (track != null? track : "");
		album = (album != null? album : "");

		try {
			YouTubeQuery query = new YouTubeQuery(new URL(apiEndpoint));
			query = new YouTubeQuery(new URL(apiEndpoint));
			query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
			query.setFullTextQuery(String.format("%s %s %s", artist, track, album));
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
			VideoFeed videoFeed = service.query(query, VideoFeed.class);
			List<VideoEntry> videos = videoFeed.getEntries();
			if (videos == null || videos.size() == 0){
				if(prompt){
					return manualProviderSideID();
				} else{
					return null;		
				}
			} else{
				for(int i = 0; i < 3 && i < videos.size(); i++){
					VideoEntry video = videos.get(i);
					String vTitle = video.getTitle().getPlainText();
					if((vTitle.toUpperCase().contains(artist.toUpperCase())) && (vTitle.toUpperCase().contains(track.toUpperCase()))){
						return video.getMediaGroup().getVideoId();
					}
				}
				if(prompt){
					BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
					PrintStream cout = System.out;
					cout.printf("\nPoor match found for %s by %s on %s%n", track, artist, album);
					printVideo(videos.get(0));
					cout.print("Accept this video? (y/n) ");
					String line = cin.readLine();
					if(line.toUpperCase().equals("Y")){
						return videos.get(0).getMediaGroup().getVideoId();
					} else{
						return manualProviderSideID(artist, track, album);
					}
				} else{
					return videos.get(0).getMediaGroup().getVideoId();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ServiceException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getProviderSideID(Track track){
		return getProviderSideID(track, false);
	}

	public String getProviderSideID(Track track, boolean tryHarder){
		String artist = (track.artist() != null? track.artist() : "");
		if(artist.isEmpty() && track.album() != null){
			artist = (track.album().artist() != null? track.album().artist() : "");
		}
		String title = (track.title() != null? track.title() : "");
		String album = (track.album() != null && track.album().title() != null? track.album().title() : "");
		return getProviderSideID(artist, title, album, tryHarder);
	}

	public ArrayList<String> getIds(ArrayList<Track> tracks){
		return getIds(tracks, false);
	}

	public ArrayList<String> getIds(ArrayList<Track> tracks, boolean prompt){
		if(tracks == null){
			return null;
		}else{
			ArrayList<String> ids = new ArrayList<String>();
			for(Track i:tracks){
				ids.add(getProviderSideID(i, true));
			}
			return ids;
		}
	}

	@Override
	public String getProviderSidePlaylist(ArrayList<Track> tracks) {
		return getProviderSidePlaylist(tracks, true);
	}

	public String getProviderSidePlaylist(ArrayList<Track> tracks, boolean isPrivate) {
		return getProviderSidePlaylist(tracks, isPrivate, false);
	}

	public String getProviderSidePlaylist(ArrayList<Track> tracks, boolean isPrivate, boolean prompt) {
		ArrayList<String> ids = getIds(tracks, prompt);
		try {
			String playlistId = insertPlaylist(isPrivate);
			for(String i: ids){
				if(i != null){
					insertPlaylistItem(playlistId, i);
				}else{
					System.out.println("Skipping track b/c it was null.");
				}
			}
			return playlistId;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveCredential(String fileName) throws IOException{
		saveCredential(fileName, credential, client_id, client_secret);
	}

	/**Holds a credential as well as the user's related info as publicly accessible fields
	 * 
	 * @author poler_000
	 *
	 */
	public static class CredentialPack implements Serializable{

		private static final long serialVersionUID = -2565296615214637306L;
		public String refresh_token, access_token;
		public String client_id, client_secret;

		public CredentialPack(String refresh_token, String access_token, String client_id, String client_secret){
			this.refresh_token = refresh_token;
			this.access_token = access_token;
			this.client_id = client_id;
			this.client_secret = client_secret;
		}
	}

	public static void saveCredential(String fileName, Credential cred, String id, String secret) throws IOException{
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream oout = new ObjectOutputStream(fout);
		CredentialPack pack = new CredentialPack(cred.getRefreshToken(), cred.getAccessToken(), id, secret);
		oout.writeObject(pack);
		oout.close();
		fout.close();
	}

	public static CredentialPack loadCredential(String fileName) throws IOException, ClassNotFoundException{
		FileInputStream fin = new FileInputStream(fileName);
		ObjectInputStream oin = new ObjectInputStream(fin);
		CredentialPack cred = (CredentialPack) oin.readObject();
		oin.close();
		fin.close();
		return cred;
	}
	/**
	 * Creates YouTube Playlist and adds it to the authorized account.
	 */
	private String insertPlaylist(boolean isPrivate) throws IOException {

		/*
		 * We need to first create the parts of the Playlist before the playlist itself.  Here we are
		 * creating the PlaylistSnippet and adding the required data.
		 */
		PlaylistSnippet playlistSnippet = new PlaylistSnippet();
		playlistSnippet.setTitle("Test Playlist " + Calendar.getInstance().getTime());
		playlistSnippet.setDescription("A private playlist created with the YouTube API v3");

		// Here we set the privacy status (required).
		PlaylistStatus playlistStatus = new PlaylistStatus();
		if(isPrivate){
			playlistStatus.setPrivacyStatus("private");
		} else{
			playlistStatus.setPrivacyStatus("public");
		}


		/*
		 * Now that we have all the required objects, we can create the Playlist itself and assign the
		 * snippet and status objects from above.
		 */
		Playlist youTubePlaylist = new Playlist();
		youTubePlaylist.setSnippet(playlistSnippet);
		youTubePlaylist.setStatus(playlistStatus);

		/*
		 * This is the object that will actually do the insert request and return the result.  The
		 * first argument tells the API what to return when a successful insert has been executed.  In
		 * this case, we want the snippet and contentDetails info.  The second argument is the playlist
		 * we wish to insert.
		 */
		YouTube.Playlists.Insert playlistInsertCommand =
				youtube.playlists().insert("snippet,status", youTubePlaylist);
		Playlist playlistInserted = playlistInsertCommand.execute();

		// Pretty print results.

		System.out.println("New Playlist name: " + playlistInserted.getSnippet().getTitle());
		System.out.println(" - Privacy: " + playlistInserted.getStatus().getPrivacyStatus());
		System.out.println(" - Description: " + playlistInserted.getSnippet().getDescription());
		System.out.println(" - Posted: " + playlistInserted.getSnippet().getPublishedAt());
		System.out.println(" - Channel: " + playlistInserted.getSnippet().getChannelId() + "\n");
		return playlistInserted.getId();

	}

	/**
	 * Creates YouTube PlaylistItem with specified video id and adds it to the specified playlist id
	 * for the authorized account.
	 *
	 * @param playlistId assign to newly created playlistitem
	 * @param videoId YouTube video id to add to playlistitem
	 */
	private String insertPlaylistItem(String playlistId, String videoId) throws IOException {

		/*
		 * The Resource type (video,playlist,channel) needs to be set along with the resource id. In
		 * this case, we are setting the resource to a video id, since that makes sense for this
		 * playlist.
		 */
		ResourceId resourceId = new ResourceId();
		resourceId.setKind("youtube#video");
		resourceId.setVideoId(videoId);

		/*
		 * Here we set all the information required for the snippet section.  We also assign the
		 * resource id from above to the snippet object.
		 */
		PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
		playlistItemSnippet.setTitle("First video in the test playlist");
		playlistItemSnippet.setPlaylistId(playlistId);
		playlistItemSnippet.setResourceId(resourceId);

		/*
		 * Now that we have all the required objects, we can create the PlaylistItem itself and assign
		 * the snippet object from above.
		 */
		PlaylistItem playlistItem = new PlaylistItem();
		playlistItem.setSnippet(playlistItemSnippet);

		/*
		 * This is the object that will actually do the insert request and return the result.  The
		 * first argument tells the API what to return when a successful insert has been executed.  In
		 * this case, we want the snippet and contentDetails info.  The second argument is the
		 * playlistitem we wish to insert.
		 */
		YouTube.PlaylistItems.Insert playlistItemsInsertCommand =
				youtube.playlistItems().insert("snippet,contentDetails", playlistItem);
		PlaylistItem returnedPlaylistItem = playlistItemsInsertCommand.execute();

		// Pretty print results.

		System.out.println("New PlaylistItem name: " + returnedPlaylistItem.getSnippet().getTitle());
		System.out.println(" - Video id: " + returnedPlaylistItem.getSnippet().getResourceId().getVideoId());
		System.out.println(" - Posted: " + returnedPlaylistItem.getSnippet().getPublishedAt());
		System.out.println(" - Channel: " + returnedPlaylistItem.getSnippet().getChannelId());
		return returnedPlaylistItem.getId();

	}


}
