package tek.first.livingbetter.habit.jsonparsing;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.ArrayList;

import tek.first.livingbetter.R;
import tek.first.livingbetter.habit.Distance;
import tek.first.livingbetter.habit.model.InfoCollectedModel;


/**
 * Code sample for accessing the Yelp API V2.
 * <p/>
 * This program demonstrates the capability of the Yelp API version 2.0 by using the Search API to
 * query for businesses by a search term and location, and the Business API to query additional
 * information about the top result from the search query.
 * <p/>
 * <p/>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 */
public class Yelp {

  /*
   * Update OAuth credentials below from the Yelp Developers API site:
   * http://www.yelp.com/developers/getting_started/api_access
   */

    private OAuthService service;
    private Token accessToken;

    public static Yelp getYelp(Context context) {
        return new Yelp(context.getString(R.string.CONSUMER_KEY), context.getString(R.string.CONSUMER_SECRET),
                context.getString(R.string.TOKEN), context.getString(R.string.TOKEN_SECRET));
    }

    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    public String search(String term, double latitude, double longitude, String limit) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        request.addQuerystringParameter("limit", limit);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public String search(String term, String location, String limit) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", limit);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public String search(String term, String limit) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("limit", limit);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public ArrayList<InfoCollectedModel> processJson(String jsonString) throws JSONException {

        final String LB_YELP_BUSINESS = "businesses";
        final String LB_YELP_REGION = "region";
        final String LB_YELP_CENTER = "center";
        final String LB_YELP_LATITUDE = "latitude";
        final String LB_YELP_LONGITUDE = "longitude";
        final String LB_YELP_NAME = "name";
        final String LB_YELP_RATING = "rating";
        final String LB_YELP_REVIEW_COUNT = "review_count";
        final String LB_YELP_CATEGORIES = "categories";
        final String LB_YELP_IMAGE_URL = "image_url";
        final String LB_YELP_SNIPPET_TEXT = "snippet_text";
        final String LB_YELP_DISPLAY_PHONE = "display_phone";
        final String LB_YELP_MOBILE_URL = "mobile_url";
        final String LB_YELP_LOCATION = "location";
        final String LB_YELP_DISPLAY_ADDRESS = "display_address";
        final String LB_YELP_COORDINATE = "coordinate";

        ArrayList<InfoCollectedModel> resultArrayList = new ArrayList<>();
        JSONObject json = new JSONObject(jsonString);
        JSONArray businesses = json.getJSONArray(LB_YELP_BUSINESS);
        JSONObject region = json.getJSONObject(LB_YELP_REGION);
        JSONObject center = region.getJSONObject(LB_YELP_CENTER);
        String currentLatitude = center.getString(LB_YELP_LATITUDE);
        String currentLongitude = center.getString(LB_YELP_LONGITUDE);
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            String name = business.getString(LB_YELP_NAME);
            float rating = Float.parseFloat(business.getString(LB_YELP_RATING));
            int reviewCount = Integer.parseInt(business.getString(LB_YELP_REVIEW_COUNT));
            String category = business.getString(LB_YELP_CATEGORIES);
            String categoryResult = handleCategory(category);
            String imageUrl;
            try {
                imageUrl = business.getString(LB_YELP_IMAGE_URL);
                imageUrl = imageUrl.replace("ms", "l");
            } catch (Exception ex) {
                imageUrl = "";
                ex.printStackTrace();
            }
            String snippetText = business.getString(LB_YELP_SNIPPET_TEXT);
            String phoneNumber;
            try {
                phoneNumber = business.getString(LB_YELP_DISPLAY_PHONE);
            } catch (Exception e) {
                phoneNumber = "";
            }

            String mobileUrl = business.getString(LB_YELP_MOBILE_URL);
            JSONObject location = business.getJSONObject(LB_YELP_LOCATION);
            String displayAddress = location.getString(LB_YELP_DISPLAY_ADDRESS);
            String address = handleAddress(displayAddress);

            JSONObject coordinate = location.getJSONObject(LB_YELP_COORDINATE);
            String resLatitude = coordinate.getString(LB_YELP_LATITUDE);
            String resLongitude = coordinate.getString(LB_YELP_LONGITUDE);
            Double distance = Distance.getDistance(Double.parseDouble(currentLatitude),
                    Double.parseDouble(currentLongitude), Double.parseDouble(resLatitude), Double.parseDouble(resLongitude));

            InfoCollectedModel infoCollectedModel = new InfoCollectedModel(name, rating, reviewCount, categoryResult, imageUrl, snippetText, address
                    , phoneNumber, mobileUrl, distance, Double.parseDouble(resLatitude), Double.parseDouble(resLongitude));
            resultArrayList.add(infoCollectedModel);
        }

        return resultArrayList;
    }

    private static String handleAddress(String display_address) {
        String[] s = display_address.split("\"");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if (s[i].length() > 1) {
                sb.append(s[i]);
                sb.append("\n");
            }
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    private static String handleCategory(String category) {

        String res[] = category.split("\"|,");
        StringBuilder sb = new StringBuilder();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < res.length; i++) {
            if (res[i].length() > 2) {
                list.add(res[i]);
            }
        }
        for (int i = 0; i < list.size(); i += 2) {
            sb.append(list.get(i));
            sb.append(",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
}