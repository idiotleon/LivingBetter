package tek.first.livingbetter.habit.jsonparsing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tek.first.livingbetter.habit.Distance;
import tek.first.livingbetter.habit.model.InfoCollectedModel;

public class ProcessJSON {

    public static ArrayList<InfoCollectedModel> processJson(String jsonStuff) throws JSONException {
        ArrayList<InfoCollectedModel> resultArrayList = new ArrayList<>();
        JSONObject json = new JSONObject(jsonStuff);
        JSONArray businesses = json.getJSONArray("businesses");
        JSONObject region = json.getJSONObject("region");
        JSONObject center = region.getJSONObject("center");
        String currentLatitude = center.getString("latitude");
        String currentLongitude = center.getString("longitude");
        for (int i = 0; i < businesses.length(); i++) {
            JSONObject business = businesses.getJSONObject(i);
            String name = business.getString("name");
            float rating = Float.parseFloat(business.getString("rating"));
            int review_count = Integer.parseInt(business.getString("review_count"));
            String category = business.getString("categories");
            String categoryResult = handlecategory(category);
            String imageUrl;
            try {
                imageUrl = business.getString("image_url");
                imageUrl = imageUrl.replace("ms", "l");
            } catch (Exception e) {
                imageUrl = "";
            }
            String snippetText = business.getString("snippet_text");
            String phone_number;
            try {
                phone_number = business.getString("display_phone");
            } catch (Exception e) {
                phone_number = "";
            }

            String mobile_url = business.getString("mobile_url");
            JSONObject location = business.getJSONObject("location");
            String display_address = location.getString("display_address");
            String address = handleaddress(display_address);

            JSONObject coordinate = location.getJSONObject("coordinate");
            String res_latitude = coordinate.getString("latitude");
            String res_longitude = coordinate.getString("longitude");
            Double distance = Distance.getDistance(Double.parseDouble(currentLatitude), Double.parseDouble(currentLongitude), Double.parseDouble(res_latitude), Double.parseDouble(res_longitude));
            InfoCollectedModel infoCollectedModel = new InfoCollectedModel(name, rating, review_count, categoryResult, imageUrl, snippetText, address
                    , phone_number, mobile_url, distance, Double.parseDouble(res_latitude), Double.parseDouble(res_longitude));
            resultArrayList.add(infoCollectedModel);
        }
        return resultArrayList;
    }

    private static String handleaddress(String display_address) {
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

    private static String handlecategory(String category) {

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

