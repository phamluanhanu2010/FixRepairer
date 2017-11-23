package com.strategy.intecom.vtc.fixrepairer.config;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.dialog.DlNewJob;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeActionConnection;
import com.strategy.intecom.vtc.fixrepairer.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.fixrepairer.interfaces.RequestListener;
import com.strategy.intecom.vtc.fixrepairer.utils.ParserJson;
import com.strategy.intecom.vtc.fixrepairer.utils.PreferenceUtil;
import com.strategy.intecom.vtc.fixrepairer.utils.Utils;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCoreAPI;

import org.json.JSONObject;

import io.socket.client.Socket;


/**
 * Created by Mr. Ha on 6/2/16.
 */
public class VtcNWConnection extends VtcHttpConnection {

    private ProgressDialog progress;

    private Handler mHandler;

    public VtcNWConnection(Activity context, RequestListener requestConnection) {
        super(context, requestConnection);
    }

    /**
     *
     * <d>Call when request Api server</d>
     * <d>show dialog process</d>
     *
     */
    private void initShowDialogProcess() {

        AppCore.showLog("-----------------------initShowDialogProcess-----------------------------");

        if(mHandler != null){
            mHandler = null;
        }

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if (progress != null) {

                    if (progress.isShowing()) {
                        progress.cancel();
                    }
                    progress = null;
                }
                if(getContext() != null) {
                    getContext().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress = ProgressDialog.show(getContext(),
                                    getContext().getResources().getString(R.string.title_dialog_message),
                                    getContext().getResources().getString(R.string.title_dialog_process_confirm), true);

                            mHandler = null;
                        }
                    });
                }
            }
        };

        if (mHandler != null) {
            Message message = mHandler.obtainMessage();
            message.sendToTarget();
        }
    }

    /**
     *
     * <d>Call when request Api server</d>
     * <d>Dismiss dialog process</d>
     *
     */
    private void initCloseDialogProcess() {

        if (progress != null && progress.isShowing()) {

            progress.dismiss();
            progress = null;
        }
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     * @param isPost setting method post
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, boolean isPost) {

        onExcuteProcess(actionConnection, sAPi, null, isPost, true);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi) {

        onExcuteProcess(actionConnection, sAPi, null, true, true);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param parameter out put
     * @param sAPi link Api connect to server
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, JSONObject parameter) {

        onExcuteProcess(actionConnection, sAPi, parameter, true, true);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param parameter out put
     * @param sAPi link Api connect to server
     * @param isPost setting method post
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, JSONObject parameter, boolean isPost) {

        onExcuteProcess(actionConnection, sAPi, parameter, isPost, true);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     * @param param out put
     * @param isShowProcess = true then show dialog process,
     *                      = false then dismiss dialog process.
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, JSONObject param, boolean isPost, boolean isShowProcess) {

        VtcModelConnect vtcModelConnect = new VtcModelConnect();
        vtcModelConnect.setActionConnection(actionConnection);
        vtcModelConnect.setAPI(sAPi);
        vtcModelConnect.setParameter(param);
        vtcModelConnect.setPost(isPost);
        vtcModelConnect.setShowProcess(isShowProcess);

        onExcute(vtcModelConnect);
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * @param actionConnection Type connection you wan request
     * @param sAPi link Api connect to server
     * @param file out put list file
     *
     */
    public void onExcuteProcess(TypeActionConnection actionConnection, String sAPi, String file, boolean isShowProcess) {

        VtcModelConnect vtcModelConnect = new VtcModelConnect();
        vtcModelConnect.setActionConnection(actionConnection);
        vtcModelConnect.setAPI(sAPi);
        vtcModelConnect.setFile(file);

        vtcModelConnect.setShowProcess(isShowProcess);

        onExcute(vtcModelConnect);
    }

    private void onExcute(VtcModelConnect vtcModelConnect) {
        ProcessConnection execute = new ProcessConnection(vtcModelConnect);

        if (execute.getStatus() == AsyncTask.Status.RUNNING) {

            setPoolQueue(vtcModelConnect.getAPI(), vtcModelConnect);
        } else {

            boolean isStatusNetWork = AppCore.getGpsTracker(getContext()).isConnection();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

                execute.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isStatusNetWork);
            } else {

                execute.execute(isStatusNetWork);
            }
        }
    }

    /**
     *
     * <d>Call when wan connect server request API</d>
     *
     * <d>Thread process connect and receiver data from server</d>
     *
     */
    private class ProcessConnection extends AsyncTask<Boolean, String, String> {

        private VtcModelConnect vtcModelConnect;

        public ProcessConnection(VtcModelConnect vtcModelConnect) {
            this.vtcModelConnect = vtcModelConnect;
        }

        private boolean isCheckNull(){
            if(vtcModelConnect == null){
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (isCheckNull() && vtcModelConnect.isShowProcess()) {
                initShowDialogProcess();
            }
        }

        @Override
        protected String doInBackground(Boolean... params) {
            String sData = "";

            if (params[0]) {
                if (isCheckNull()) {

                    switch (vtcModelConnect.getActionConnection()) {
                        case TYPE_ACTION_UPLOAD_AVATAR:

                            return uploadFile(vtcModelConnect.getFile(), vtcModelConnect.getAPI());
                        default:

                            String parameter = "";

                            if (vtcModelConnect.getParameter() != null) {
                                parameter = String.valueOf(vtcModelConnect.getParameter());
                            }

                            AppCore.showLog("---------- : doInBackground " + vtcModelConnect.getAPI() + " -- " + parameter);

                            return initRequestConnection(vtcModelConnect.getAPI(), parameter, vtcModelConnect.isPost());
                    }
                }

            } else {
                // No Internet Connection
                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_NO_INTERNET);
            }

            return sData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (getRequestConnection() != null) {

                if (getErrorConnection() == TypeErrorConnection.TYPE_CONNECTION && !s.equals("")) {

                    if(ParserJson.getStatusSuccess(s)){

                        getRequestConnection().onPostExecuteSuccess(vtcModelConnect.getActionConnection(), ParserJson.getResponseData(s), ParserJson.getStatusMsg(s));
                    }else {
                        int errorCode = ParserJson.getErrorCode(s);

                        switch (TypeErrorConnection.forValue(errorCode)){

                            case TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE:

                                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR_CODE_VERIFY_CODE);
                                break;

                            case TYPE_CONNECTION_ERROR_CODE_FORGOT_PASSWORD:
                                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR_CODE_FORGOT_PASSWORD);
                                break;

                            case TYPE_CONNECTION_ERROR_CODE_ORDER_OVERWRITE:
                                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR_CODE_ORDER_OVERWRITE);
                                break;

                            case TYPE_CONNECTION_ERROR_CODE_ORDER_CLOSE:
                                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR_CODE_ORDER_CLOSE);
                                break;

                            case TYPE_CONNECTION_ERROR_CODE_EXIST_ACCEPT:
                                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR_CODE_EXIST_ACCEPT);
                                break;

                            default:
                                setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR_FROM_SERVER);
                                break;
                        }

                        getRequestConnection().onPostExecuteError(getErrorConnection(), ParserJson.getStatusMsg(s));
                    }

                } else {

                    getRequestConnection().onPostExecuteError(getErrorConnection(), "");
                }
            }

            if (vtcModelConnect.isShowProcess()) {
                initCloseDialogProcess();
            }

            if (getApiQueueSize() > 0) {

                String sApi = getApiQueue();

                onExcute(getVtcModelConnect(sApi));
            }
        }
    }


    /**
     *
     * <d>Call when wan connect server Socket request API</d>
     *
     */
    public void onExcuteConnectSocket() {
        initConnectSocket(AppCore.getPreferenceUtil(getContext()).getValueString(PreferenceUtil.AUTH_TOKEN));
    }

    /**
     *
     * <d>Call when wan connect server Socket request API</d>
     *
     */
    public void onExcuteDisconnectSocket() {
        disConnectSocket();
    }

    /**
     *
     * <d>Call when wan Off Event Socket</d>
     *
     */
    public void onExcuteOffEventSocket() {
        offEventOrderOfferSocket();
    }

    /**
     *
     * <d>Call when wan On Event Socket</d>
     *
     */
    public void onExcuteOnEventSocket() {
        onEventOrderOfferSocket();
    }
}
