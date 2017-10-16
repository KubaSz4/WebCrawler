package sample;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Model {

    final static int SUCCESS = 0;
    final static int WRONG_URL = 1;
    final static int CONNECTION_FAILED = 2;
    final static int NO_PREVIOUS_PAGE = 3;
    private static final String IMG_REGEXP = "img[^>]*src=\"[^\"']*[\"']";
    private static final String IMAGE_REGEXP = "href=\"[^\"']*[\"']";
    private static final String HTTP = "http";
    private static final String HREF = "href=\"";
    private static final String SRC = "src=\"";

    private Stack<String> urlStack;

    private String currentUrl;
    private List<String> urlList;
    private long imageSize;
    private int imageNumber;
    private Connection databaseConnection;

    Model(){
        urlList = new ArrayList<>();
        urlStack = new Stack<>();
        connectToDatabase();
    }

    private void connectToDatabase() {
        createDirectoryIfNeeded();
        String path = "jdbc:sqlite:database/database.db";

        try{
            databaseConnection = DriverManager.getConnection(path);

            StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS urls (\n");
            stringBuilder.append("id integer PRIMARY KEY, \n");
            stringBuilder.append("url text NOT NULL\n");
            stringBuilder.append(");");

            String tableCreateStatement = stringBuilder.toString();

            databaseConnection.createStatement().execute(tableCreateStatement);

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void createDirectoryIfNeeded() {
        File directory = new File("database");
        if(!directory.exists()){
            try{
                directory.mkdir();
            } catch (SecurityException e){
                System.err.println("Cannot create directory \"database\" in " + System.getProperty("user.dir"));
            }
        }
    }


    public int LoadNewPage(String urlString){
        int result = loadPage(urlString);

        if (result == Model.SUCCESS){
            urlStack.add(currentUrl);
            currentUrl = urlString;
        }

        return result;
    }

    public int loadPreviousPage(String urlString){
        if(urlString == null){
            return NO_PREVIOUS_PAGE;
        }

        int result = loadPage(urlString);

        if (result == Model.SUCCESS){
            currentUrl = urlString;
            urlStack.pop();
        }

        return result;

    }

    public int loadPage(String urlString){
        URL url;

        try{
            url = new URL(urlString);
        }catch(Exception e){
            return WRONG_URL;
        }

        Set<String> urlSet = new HashSet<>();
        Set<String> imageUrlSet = new HashSet<>();

        try {
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                urlSet.addAll(findAllUrls(urlString, inputLine, HREF, IMAGE_REGEXP));
                imageUrlSet.addAll(findAllUrls(urlString, inputLine, SRC, IMG_REGEXP));
            }

        } catch(IOException e){
            return CONNECTION_FAILED;
        }

        imageNumber = imageUrlSet.size();
        imageSize = asyncCountImageSize(imageUrlSet);
        urlList = new ArrayList<>(urlSet);
        insertUrlIntoDatabase(urlString);

        return SUCCESS;
    }


    private long asyncCountImageSize(Set<String> imageUrlSet) {

        ExecutorService threadpool = Executors.newCachedThreadPool();
        List<Future<Long>> futureList = new ArrayList<>();

        for (String urlString : imageUrlSet){
            futureList.add(threadpool.submit(() -> {
                try{
                    return new URL(urlString).openConnection().getContentLengthLong();
                }catch(Exception e){
                    return 0L;
                }
            }));
        }

        long result = 0;

        for (Future<Long> future : futureList){
            try {
                result += future.get();
            } catch (Exception e) {}
        }

        return result;
    }

    private Set<String> findAllUrls(String urlString, String inputLine, String linkPrefix, String regexp) {
        Set<String> result = new HashSet<>();

        Matcher m = Pattern.compile(regexp)
                .matcher(inputLine);

        while (m.find()) {
            String str = m.group();
            try {
                str = str.substring(str.indexOf(linkPrefix)+ linkPrefix.length(), str.length() - 1);
                if(str.startsWith(HTTP) == false){
                    str = urlString+str;
                }
                result.add(str);
            }catch (Exception ignored) {}
        }

        return result;
    }

    private void insertUrlIntoDatabase(String url){
        String insertStatement = "INSERT INTO urls (url) VALUES (\""+url+"\");";

        try {
            getDatabaseConnection().createStatement().execute(insertStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getDatabaseConnection(){
        if(databaseConnection == null){
            connectToDatabase();
        }
        return databaseConnection;
    }

    public ArrayList<String> getUrlHistory(int size){
        String selectStatement = "SELECT DISTINCT url FROM urls ORDER BY id DESC LIMIT "+size+";";

        ArrayList<String> urlHistory = new ArrayList<>();

        try {
            ResultSet resultSet = getDatabaseConnection().createStatement().executeQuery(selectStatement);
            while(resultSet.next()){
                urlHistory.add(resultSet.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urlHistory;
    }

    public List<String> getUrlList(){
        return urlList;
    }

    public long getImageSize(){
        return imageSize;
    }

    public int getImageNumber() {
        return imageNumber;
    }

    public String getPreviousUrl() {
        if(urlStack.empty()){
            return null;
        }
        return urlStack.peek();
    }

    public String getCurrentUrl() {
        return currentUrl;
    }
}
