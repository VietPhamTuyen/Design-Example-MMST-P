package plt.tud.de.example;

/**
 * Created by Viet on 18.12.2015.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;


@SuppressWarnings("ALL")
public class LDQueryActivity {
    Handler handler;

    // intent extra parameters and their defaults
    String uri = "http://eatld.et.tu-dresden.de/mti-mmst2015_g2_1";
    String endpoint = "http://eatld.et.tu-dresden.de/sparql-auth";

    static String sparql_username = "mmst2015_g2_1";
    static String sparql_password = "mmst2015_g2_1";

    private static UsernamePasswordCredentials credentials;

    static ArrayList<String> listItems = new ArrayList<String>();

    static ArrayAdapter<String> adapter;

    static MainActivity main;

    static Controller controller = new Controller();
//TODO synch?

    public static void LDQueryActivity() {

    }


    public void changeEndpoint(String endpoint) {
        this.endpoint = endpoint;

    }

    /**
     * composeQuery generates a query that gets all the predicates and
     * attributes of the
     *
     * @param uri String - the URI of the sensor (without <>)
     * @return the sparql query (String)
     */
    public String composeQuery(String uri, String requestedData, String plan, String tourID, String workingStep) {

        String query = "";
        if (requestedData == "getMaintenancePlan") {
            Log.i("composeQuery" , "1 getMaintenancePlan");

            String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
            String rdfsns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
            String mto = "http://eatld.et.tu-dresden.de/mto/";
            String opc = "http://eatld.et.tu-dresden.de/opcua/";
            String owl = "http://www.w3.org/2002/07/owl#";
            String purl = "http://purl.org/dc/elements/1.1/";

            query = "PREFIX rdfs: <" + rdfs + "> "
                    + "PREFIX rdfsns: <" + rdfsns + ">"
                    + "PREFIX mto: <" + mto + "> "
                    + "PREFIX opc: <" + opc + ">"
                    + "PREFIX owl: <" + owl + ">"
                    + "PREFIX purl: <" + purl + ">"

                    + "SELECT DISTINCT ?s ?maintenancePlan ?id ?kennzeichen FROM <" + uri + "> "
                    + "WHERE {"
                    + "?s mto:maintenancePlan ?maintenancePlan."

                    + "?s mto:refIdTour ?id."
                    + "FILTER (!(?id= \"\" ))"
                    + "FILTER regex(?id, \""+tourID+"\")"       //TODO

                    + "?s rdfs:label ?kennzeichen."
                    + "}"
            ;


        } else if (requestedData == "getWorkingSteps") {
            Log.i("composeQuery" , "2 getWorkingSteps");
            String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
            String rdfsns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
            String mto = "http://eatld.et.tu-dresden.de/mto/";
            String opc = "http://eatld.et.tu-dresden.de/opcua/";
            String owl = "http://www.w3.org/2002/07/owl#";
            String purl = "http://purl.org/dc/elements/1.1/";

            query = "PREFIX rdfs: <" + rdfs + "> "
                    + "PREFIX rdfsns: <" + rdfsns + ">"
                    + "PREFIX mto: <" + mto + "> "
                    + "PREFIX opc: <" + opc + ">"
                    + "PREFIX owl: <" + owl + ">"
                    + "PREFIX purl: <" + purl + ">"
                    + "SELECT DISTINCT ?s ?maintenancePlan ?id ?kennzeichen ?workingstep FROM <" + uri + ">"
                    + "WHERE {"
                    + "?s mto:maintenancePlan ?maintenancePlan."
                    + "FILTER regex(?maintenancePlan, \""+plan+"\")" //TODO

                    + "?s mto:refIdTour ?id."
                    + "FILTER (!(?id= \"\" ))"
                    + "FILTER regex(?id, \""+tourID+"\")" //TODO

                    + "?s rdfs:label ?kennzeichen."

                    + "?s mto:hasWorkingStep ?workingstep."

                    + "}";


        } else if (requestedData == "getWorkingTitle") {
            Log.i("composeQuery" , "3 getWorkingTitle");
            String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
            String rdfsns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
            String mto = "http://eatld.et.tu-dresden.de/mto/";
            String opc = "http://eatld.et.tu-dresden.de/opcua/";
            String owl = "http://www.w3.org/2002/07/owl#";
            String purl = "http://purl.org/dc/elements/1.1/";

            query = "PREFIX rdfs: <" + rdfs + "> "
                    + "PREFIX rdfsns: <" + rdfsns + ">"
                    + "PREFIX mto: <" + mto + "> "
                    + "PREFIX opc: <" + opc + ">"
                    + "PREFIX owl: <" + owl + ">"
                    + "PREFIX purl: <" + purl + ">"
                    + "SELECT DISTINCT ?s ?maintenancePlan ?id ?kennzeichen ?workingstep ?workinglabel FROM <" + uri + ">"
                    + "WHERE {"
                    + "?s mto:maintenancePlan ?maintenancePlan."
                    + "FILTER regex(?maintenancePlan, \"" +plan+ "\")"      //TODO

                    + "?s mto:refIdTour ?id."
                    + "FILTER (!(?id= \"\" ))"
                    + "FILTER regex(?id, \"" +tourID+ "\")"       //TODO

                    + "?s rdfs:label ?kennzeichen."

                    + "?s mto:hasWorkingStep ?workingstep."
                    + "FILTER regex(?workingstep, \"" +workingStep+ "\")"  //TODO

                    + "?workingstep rdfs:label ?workinglabel."

                    + "?workingstep purl:title ?titel."


                    + "}  LIMIT 1";

        }


        return query;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * create a new request on the Linked Data endpoint
     */

    protected void createLD(String requestedData, String plan, String tourID, String workingStep) {
        // display layout and bind listeners to data-structures


        // Handle result of a Query
        handler = new QueryResultHandler();
        // Eventually query sensor data
        querySensorData(requestedData, plan, tourID, workingStep);



    }


    /**
     * Query the data from server, using the endpoint
     */
    public void querySensorData(String requestedData, String plan, String tourID, String workingStep) {
        ArrayList<String> queryTaskParams = new ArrayList<String>();
        queryTaskParams.add(requestedData);
        queryTaskParams.add(plan);
        queryTaskParams.add(tourID);
        queryTaskParams.add(workingStep);

        AsyncSparqlTaskQuery queryTask = new AsyncSparqlTaskQuery();
        queryTask.execute(queryTaskParams);
    }

    /**
     * Displays a toast if no network connection is available
     */
    protected void displayNoNetworkConnection() {
        Log.i("LinkedData", "DC");
        Toast.makeText(main, R.string.noNetwork, Toast.LENGTH_LONG).show();
    }

    /**
     * Asynchronous task for querying data
     */
    private class AsyncSparqlTaskQuery extends AsyncTask<ArrayList<String>, Void, Void> {

        int networkFailure = 0;

        @Override
        protected void onProgressUpdate(Void... values) {
            if (networkFailure == 1) {
                //display toast
                displayNoNetworkConnection();
                Log.i("error", "no LD connection");
                networkFailure = 0;
            }
        }


        @Override
        protected synchronized Void doInBackground(ArrayList<String>... queryTaskParams) {
            /*
            *			Receive data via http Request
			*/
            String requestedData = queryTaskParams[0].get(0);
            String plan = queryTaskParams[0].get(1);
            String tourID = queryTaskParams[0].get(2);
            String workingStep = queryTaskParams[0].get(3);
            //Timeout for http Connection
            HttpParams httpParams = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            int timeoutConnection = 3000;

            //Connection Timeout leads to an SocketTimeoutException
            HttpConnectionParams.setConnectionTimeout(httpParams, timeoutConnection);
            //https://github.com/plt-tud/r43ples/blob/master/src/main/java/de/tud/plt/r43ples/triplestoreInterface/HttpInterface.java



            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            credentials = new UsernamePasswordCredentials(sparql_username, sparql_password);
            httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
            HttpPost request = new HttpPost(endpoint);


            //set up HTTP Post Request (look at http://virtuoso.openlinksw.com/dataspace/doc/dav/wiki/Main/VOSSparqlProtocol for Protocol)
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("format", "application/json"));
            nameValuePairs.add(new BasicNameValuePair("query", composeQuery(uri, requestedData, plan,tourID,workingStep)));
            nameValuePairs.add(new BasicNameValuePair("default-graph-uri", null));
            nameValuePairs.add(new BasicNameValuePair("named-graph-uri", null));

            try {
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                Log.i("debugg", nameValuePairs.toString());

            } catch (UnsupportedEncodingException e1) {

                e1.printStackTrace();
                Log.e(LDQueryActivity.class.getName(), e1.getMessage());
            }
            try { //Execute Query
                HttpResponse response = httpClient.execute(request);
                Log.d("HTTP Request", "status: " + response.getStatusLine().getStatusCode());

                // Get the response
                BufferedReader rd = new BufferedReader
                        (new InputStreamReader(response.getEntity().getContent()));

                String line = "";
                String json = "";

                //read only 20 lines for this example
                int i = 40;

                //read the response line by line
                while (((line = rd.readLine()) != null) && (i-- != 0)) {
                    json = json + "\n" + line;
                   // Log.i("json", "line: "+ line);
                }

                //only 10 lines are read, so delete comma at the end
                if (json.endsWith(",")) {
                    json = json.substring(0, json.length() - 1);
                }
                //correct termination of json string, so it gets parsed correctly
                json = json + "]}}";
                //TODO json= json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1);



                //inform Handler about json dataset
                Message msg = new Message();
                msg.setTarget(handler);

                if (requestedData == "getMaintenancePlan") {
                    msg.what = 90;
                } else if (requestedData == "getWorkingSteps") {
                    msg.what = 80;
                }else if (requestedData == "getWorkingTitle") {
                    msg.what = 70;
                }
                Bundle data = new Bundle();
                data.putString("result", json);
                data.putString("plan", plan);
                data.putString("tourID", tourID); //TODO tourID
                data.putString("workingStep", workingStep);
                msg.setData(data);

                msg.sendToTarget();


            } catch (IOException e) {
                e.printStackTrace();
                Log.e(LDQueryActivity.class.getName(), e.getMessage());

				/*
				 * There may be no network connection or a timeout.
				 * Show toast to let the user check connections.
				 */
                networkFailure = 1;
                // publishProgress(params);
            }

            return null;
        }
    }

    public void deleteList() {
        listItems = new ArrayList<String>();
    }


    /**
     * The handler for processing the results from the query
     * <p/>
     * message == 90  -> "getMaintenancePlan"
     * message == 80  -> "getWorkingSteps"
     * message == 70  -> "getWorkingTitle"
     */
    private static class QueryResultHandler extends Handler {

        public void handleMessage(Message message) {
            Log.i("Handler", "get it");
            if (message.what == 90) { //getProp
                Log.i("what", "90");

                Bundle resultBundle = message.getData();
                String result = resultBundle.getString("result");


                if (null == result)
                    result = "No Result received!";
                try {
                    //parse result JSON string into JSON Object
                    JSONObject results = new JSONObject(result);

                    //Create JSON Array out of JSONObject
                    JSONArray titles = results.getJSONObject("results")
                            .getJSONArray("bindings");

                    for (int i = 0; i < titles.length(); i++) {
                        JSONObject c = titles.getJSONObject(i);
                        String s = c.getJSONObject("s") //TODO need? (3x)
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String maintenancePlan = c.getJSONObject("maintenancePlan")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String id = c.getJSONObject("id")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String kennzeichen = c.getJSONObject("kennzeichen")
                                .getString("value")
                                .replaceFirst("^.*#", "");

                        //TODO add the other parameter to the string

                        //fill list

                       // Bundle resultBundle2 = message.getData();
                       // String bundleNode = resultBundle2.getString("node");
                       // listItems.add(bundleNode + ": " + s + " , " + maintenancePlan + " , " + id + " , " + kennzeichen);

                        controller.createPlan(maintenancePlan, id, kennzeichen);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error", e.getMessage());
                }


            } else if (message.what == 80) { //getNodeID
                Log.i("what", "80");

                Bundle resultBundle = message.getData();
                String result = resultBundle.getString("result");
                Log.i("normal", " "+ resultBundle.getString("maintenancePlan")+" "+resultBundle.getString("id")+" "+resultBundle.getString("kennzeichen")+" "+resultBundle.getString("workingstep"));

                if (null == result)
                    result = "No Result received!";
                try {
                    //parse result JSON string into JSON Object

                    JSONObject results = new JSONObject(result);
                    //Create JSON Array out of JSONObject
                    JSONArray titles = results.getJSONObject("results")
                            .getJSONArray("bindings");

                    for (int i = 0; i < titles.length(); i++) {
                        JSONObject c = titles.getJSONObject(i);
                        String s = c.getJSONObject("s")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String maintenancePlan = c.getJSONObject("maintenancePlan")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String id = c.getJSONObject("id")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String kennzeichen = c.getJSONObject("kennzeichen")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String workingLink = c.getJSONObject("workingstep")
                                .getString("value")
                                .replaceFirst("^.*#", "");



                        controller.saveStep(maintenancePlan,id, workingLink);


                        //TODO call something
                        //fill list
                        //listItems.add(p);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error", e.getMessage());
                    Log.i("error", " "+ resultBundle.getString("maintenancePlan")+" "+resultBundle.getString("id")+" "+resultBundle.getString("kennzeichen")+" "+resultBundle.getString("workingstep"));
                }


            }else if (message.what == 70) { //getNodeID
                Log.i("what", "70");

                Bundle resultBundle = message.getData();
                String result = resultBundle.getString("result");
                if (null == result)
                    result = "No Result received!";
                try {
                    //parse result JSON string into JSON Object

                    JSONObject results = new JSONObject(result);
                    //Create JSON Array out of JSONObject
                    JSONArray titles = results.getJSONObject("results")
                            .getJSONArray("bindings");
                    for (int i = 0; i < titles.length(); i++) {
                        JSONObject c = titles.getJSONObject(i);
                        String s = c.getJSONObject("s")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String maintenancePlan = c.getJSONObject("maintenancePlan")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String id = c.getJSONObject("id")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String kennzeichen = c.getJSONObject("kennzeichen")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String workingLink = c.getJSONObject("workingstep")
                                .getString("value")
                                .replaceFirst("^.*#", "");
                        String workinglabel = c.getJSONObject("workinglabel")
                                .getString("value")
                                .replaceFirst("^.*#", "");


                        controller.saveWorkingLabel(maintenancePlan, workingLink, workinglabel);


                        //TODO fill controller List
                        //fill list
                        //listItems.add(p);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("error", e.getMessage());
                }


            }
        }
    }

    ;
}