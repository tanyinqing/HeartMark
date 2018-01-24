package com.yikang.heartmark.common.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.yikang.heartmark.application.AppContext;
import com.yikang.heartmark.common.business.other.DataHandler;
import com.yikang.heartmark.common.business.other.HttpServiceHandler;
import com.yikang.heartmark.common.business.other.LoginUIInterface;
import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.ResultData;
import com.yikang.heartmark.model.chat.User;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtil {

    final static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);
    private final static String DEFAULT_ENCODING = "UTF-8";

    public static byte[] doPost(String strUrl, byte[] reqData) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();

            httpcon.setDoOutput(true);
            httpcon.setDoInput(true);
            httpcon.setUseCaches(false);
            httpcon.setInstanceFollowRedirects(true);

            httpcon.setRequestMethod("POST");

            //
//			httpcon.setConnectTimeout(30 * 1000);
//			httpcon.setReadTimeout(30 * 1000);
            httpcon.setConnectTimeout(2 * 60 * 1000);
            httpcon.setReadTimeout(2 * 60 * 1000);


            httpcon.connect();
            OutputStream os = httpcon.getOutputStream();
            os.write(reqData);
            os.flush();
            InputStream is = httpcon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            for (int len = 0; (len = is.read(buf)) != -1; ) {
                baos.write(buf, 0, len);
            }
            is.close();

            byte[] resData = baos.toByteArray();
            baos.close();
            httpcon.disconnect();
            return resData;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            return null;
        }
    }

    public static void addRequestHeader(HttpURLConnection httpConn) {

        try {
            httpConn.setRequestProperty("Accept-LANGUAGE", LocalUtil.getCurLang());

            AppContext appContext = AppContext.getAppContext();
            StringBuilder cookieBuilder = new StringBuilder();
            cookieBuilder.append("time_zone=" + java.net.URLEncoder.encode(LocalUtil.getTimeZone(), "UTF-8"));

            User user = appContext.getCurrentUser();
            if (user != null) {
                cookieBuilder.append("; access_token=" + user.getAccessToken());
                cookieBuilder.append("; user_id=" + user.getUserInfo().getUserId());
            }
            httpConn.setRequestProperty("Cookie", cookieBuilder.toString());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void addRequestHeader(AsyncHttpClient httpClient) {
        try {
            httpClient.addHeader("Accept-LANGUAGE", LocalUtil.getCurLang());

            AppContext appContext = AppContext.getAppContext();
            StringBuilder cookieBuilder = new StringBuilder();
            cookieBuilder.append("time_zone=" + java.net.URLEncoder.encode(LocalUtil.getTimeZone(), "UTF-8"));

            User user = appContext.getCurrentUser();
            if (user != null) {
                cookieBuilder.append("; access_token=" + user.getAccessToken());
                cookieBuilder.append("; user_id=" + user.getUserInfo().getUserId());
            }
            httpClient.addHeader("cookie", cookieBuilder.toString());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ResultData sendRequestSync(String url, JSONObject reqJson, Context context) {

        ResultData map = new ResultData();
        try {
            SyncHttpClient client = new SyncHttpClient();
            client.setTimeout(Constants.SYNC_TIMEOUT);
            HttpRequestUtil.addRequestHeader(client);
            StringEntity entity = new StringEntity(reqJson.toString(), "utf-8");
            client.post(context, url, entity, "appliaction/json", getSyncResponseHandler(map));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString(), e);
            map.setLocalError(e.getMessage());
        }
        return map;
    }

    public static ResultData sendRequestSyncEncrypted(String url, JSONObject reqJson) {

        ResultData map = new ResultData();
        try {
            SyncHttpClient client = new SyncHttpClient();
            HttpRequestUtil.addRequestHeader(client);
            JSONObject jsonObject = encryptJson(reqJson);
            client.setTimeout(Constants.SYNC_TIMEOUT);

            StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
            client.post(null, url, entity, "appliaction/json", getSyncEncryptedResponseHandler(map));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.toString(), e);
            map.setLocalError(e.getMessage());
        }

        return map;
    }

    public static void sendSimpleRequestAsync(String url, JSONObject reqJson, Context context, final DataHandler<JSONObject> dataHandler) {

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constants.REQUEST_TIMEOUT);
            HttpRequestUtil.addRequestHeader(client);
            StringEntity stringEntity = new StringEntity(reqJson.toString(), "utf-8");
            client.post(context, url, stringEntity, "application/json", getAsyncResponseHandler(dataHandler));
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }

    public static void sendSimpleRequestAsyncEncrypted(String url, JSONObject reqJson, Context context, final DataHandler<JSONObject> dataHandler) {

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constants.REQUEST_TIMEOUT);
            HttpRequestUtil.addRequestHeader(client);
            StringEntity stringEntity = new StringEntity(HttpRequestUtil.encryptJson(reqJson).toString(), "utf-8");
            client.post(context, url, stringEntity, "application/json", getAsyncEncryptedResponseHandler(dataHandler));
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
    }

    public static void sendRequestAsync(String url, JSONObject reqJson, Context context, final HttpServiceHandler httpHandler) {
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constants.REQUEST_TIMEOUT);
            client.addHeader("Accept-LANGUAGE", Constants.LANGUAGE);
            HttpRequestUtil.addRequestHeader(client);
            if (null != reqJson) {
                StringEntity stringEntity = new StringEntity(reqJson.toString(), "utf-8");
                client.post(context, url, stringEntity, "application/json", getAsyncResponseHandler(httpHandler));
            } else {
                client.post(url, getAsyncResponseHandler(httpHandler));
            }

        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

    }

    public static void sendRequestAsyncEncrypted(String url, JSONObject reqJson, Context context, final HttpServiceHandler httpHandler) {

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constants.REQUEST_TIMEOUT);
            HttpRequestUtil.addRequestHeader(client);
            JSONObject jsonObject = encryptJson(reqJson);
            StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
            client.post(context, url, entity, "appliaction/json", getAsyncEncryptedResponseHandler(httpHandler));
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }

    }

    public static AsyncHttpResponseHandler getAsyncResponseHandler(final HttpServiceHandler httpHandler) {

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int resonCode, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String errorReson = throwable.getMessage();
                    httpHandler.onError(Constants.SERVER_ERROR, "", throwable);
                } catch (Exception e) {
                    logger.error(throwable.toString(), throwable);
                    //by chengl
                    if (throwable.toString().contains("refused")) {
                        //todo
                        //CustomToash.showToast(AppContext.getAppContext().getCurrentActivity(), "网络不稳定，请稍候重试", 2000);
                        return;
                    }
                    httpHandler.onError(Constants.SERVER_ERROR, "", throwable);
                }
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String recvData = new String(bytes, "utf-8");

                    JSONObject recvJson = new JSONObject(recvData);
                    ResultData map = new ResultData(recvJson);

                    if (map.getCode() == Constants.ACCESS_TOKEN_KICKED) {
                        LoginUIInterface.logoffUI();
                        return;
                    }
                    showExceptionCode(map);

                    httpHandler.onResponse(map.getCode(), map.getReason(), map.getJsonObject());
                } catch (Exception ex) {
                    logger.error(ex.toString(), ex);
                    httpHandler.onError(Constants.SERVER_ERROR, ex.toString(), ex);
                }
            }
        };
        return responseHandler;
    }

    public static AsyncHttpResponseHandler getAsyncEncryptedResponseHandler(final HttpServiceHandler httpHandler) {

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int resonCode, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
                    String errorReson = throwable.getMessage();
                    httpHandler.onError(Constants.SERVER_ERROR, "", throwable);
                } catch (Exception e) {
                    logger.error(throwable.toString(), throwable);
                    //by chengl
                    if (throwable.toString().equals("java.net.SocketTimeoutException")) {
                        //todo
                        //CustomToash.showToast(AppContext.getAppContext().getCurrentActivity(), "网络不稳定，请稍候重试", 2000);
                        return;
                    }
                    httpHandler.onError(Constants.SERVER_ERROR, "", throwable);
                }
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    //todo
//                    byte[] aesKey = AppContext.getAppContext().getAESKey();
//                    String recvData = AESUtil.decrypt(new String(bytes, "utf-8"), aesKey);
//
//                    JSONObject recvJson = new JSONObject(recvData);
//                    ResultData map = new ResultData(recvJson);
//
//                    showExceptionCode(map);
//                    httpHandler.onResponse(map.getCode(), map.getReason(), map.getJsonObject());
                } catch (Exception ex) {
                    logger.error(ex.toString(), ex);
                    httpHandler.onError(Constants.SERVER_ERROR, ex.toString(), ex);
                }
            }
        };
        return responseHandler;
    }

    private static void showExceptionCode(ResultData map) {
        int exceptionInfo = 0;
        switch (map.getCode()) {
            /*case Constants.PICKDELETED:
                exceptionInfo=R.string.cardDeleted;
                break;
            case Constants.VOTESELF:
                exceptionInfo=R.string.votedSelf;
                break;
            case Constants.REPLACEVOTE:
                exceptionInfo=R.string.cardreVoted;
                break;
            case Constants.HASEXPIRED:
                exceptionInfo=R.string.cardExpried;
                break;
            case Constants.VOTEPICK:
                exceptionInfo=R.string.cardvoted;
                break;*/
            default:
                break;
        }
        if (exceptionInfo == 0)
            return;
        //  CustomToash.showToast(AppContext.getAppContext().getCurrentActivity(),exceptionInfo, 2000);
        // TipsToast.getInstance().makeTextRaw(AppContext.getAppContext().getCurrentActivity(),exceptionInfo,2000);
//        tipsToast.setDuration(Toast.LENGTH_SHORT);
//        tipsToast.show();
        return;
    }


    public static AsyncHttpResponseHandler getSyncResponseHandler(final ResultData map) {

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                logger.error(throwable.toString(), throwable);
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String recvData = new String(bytes, "utf-8");
                    JSONObject recvJson = new JSONObject(recvData);
                    map.setJsonData(recvJson);
                } catch (Exception ex) {
                    logger.error(ex.toString(), ex);
                    map.setLocalError(ex.getMessage());
                }
            }
        };
        return responseHandler;
    }

    public static AsyncHttpResponseHandler getSyncEncryptedResponseHandler(final ResultData map) {

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                logger.error(throwable.toString(), throwable);
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    //todo
//                    byte[] aesKey = AppContext.getAppContext().getAESKey();
//                    String recvData = AESUtil.decrypt(new String(bytes, "utf-8"), aesKey);
//
//                    JSONObject recvJson = new JSONObject(recvData);
//                    map.setJsonData(recvJson);

                } catch (Exception ex) {
                    logger.error(ex.toString(), ex);
                    map.setLocalError(ex.getMessage());
                }
            }
        };
        return responseHandler;
    }


    private static AsyncHttpResponseHandler getAsyncResponseHandler(final DataHandler dataHandler) {

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int reasonCode, Header[] headers, byte[] bytes, Throwable throwable) {
                logger.error(throwable.toString(), throwable);
                dataHandler.onData(404, "neterror", "");
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String recvData = new String(bytes, "utf-8");

                    JSONObject recvJson = new JSONObject(recvData);
                    ResultData map = new ResultData(recvJson);

                    if (map.getCode() == Constants.ACCESS_TOKEN_KICKED) {
                        LoginUIInterface.logoffUI();
                    }

                    showExceptionCode(map);
                    dataHandler.onData(map.getCode(), map.getReason(), map.getJsonObject());
                } catch (Exception ex) {
                    logger.error(ex.toString(), ex);
                }
            }
        };
        return responseHandler;
    }

    private static AsyncHttpResponseHandler getAsyncEncryptedResponseHandler(final DataHandler dataHandler) {

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int reasonCode, Header[] headers, byte[] bytes, Throwable throwable) {
                logger.error(throwable.toString(), throwable);
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    //todo
//                    byte[] aesKey = AppContext.getAppContext().getAESKey();
//                    String recvData = AESUtil.decrypt(new String(bytes, "utf-8"), aesKey);
//
//                    JSONObject recvJson = new JSONObject(recvData);
//                    ResultData map = new ResultData(recvJson);
//
//                    showExceptionCode(map);
//
//                    dataHandler.onData(map.getCode(), map.getReason(), map.getJsonObject());
                } catch (Exception ex) {
                    logger.error(ex.toString(), ex);
                }
            }
        };
        return responseHandler;
    }

    public static JSONObject encryptJson(JSONObject jsonObj) {
        try {
            //todo
//            AppContext context = AppContext.getAppContext();
//
//            byte[] pubmodulus = StringUtil.hex2ByteArray(context.getPublicKeyJson().getString("public_modulus"));
//            byte[] pubexponent = StringUtil.hex2ByteArray(context.getPublicKeyJson().getString("public_exponent"));
//
//            RSAPublicKey pubKey = RSACipherUtil.generateRSAPublicKey(pubmodulus, pubexponent);
//            byte[] aesKey = context.getAESKey();
//
//            String encrypted_keydata = Base64Util.encode(RSACipherUtil.encrypt(pubKey, aesKey));
//
//            String cipher_text = AESUtil.encrypt(jsonObj.toString(), aesKey);

            JSONObject encrypt_Json = new JSONObject();
//            encrypt_Json.put("secret_key", encrypted_keydata);
//            encrypt_Json.put("cipher_txt", cipher_text);

            return encrypt_Json;
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
            throw new RuntimeException(ex);
        }
    }
}
