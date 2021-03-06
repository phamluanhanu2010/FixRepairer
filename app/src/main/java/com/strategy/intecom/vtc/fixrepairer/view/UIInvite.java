package com.strategy.intecom.vtc.fixrepairer.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.strategy.intecom.vtc.fixrepairer.R;
import com.strategy.intecom.vtc.fixrepairer.utils.Const;

import java.util.List;

/**
 * Created by Luan Pham on 6/7/16.
 */
public class UIInvite extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_invite);

        initControler();
    }

    private void initControler(){
        ImageView btnBack = (ImageView)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        Button btnShareFacebook = (Button) findViewById(R.id.btn_ShareFacebook);
        btnShareFacebook.setOnClickListener(this);
        Button btnInviteSMS = (Button) findViewById(R.id.btn_InviteSMS);
        btnInviteSMS.setOnClickListener(this);
        Button btnClipboard = (Button) findViewById(R.id.btn_Clipboard);
        btnClipboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_ShareFacebook:
                shareLinkOnFacebook();
                this.finish();
                break;
            case R.id.btn_InviteSMS:
                inviteFriendBySMS();
                this.finish();
                break;
            case R.id.btn_Clipboard:
                copyLinkToClipboard();
                this.finish();
                break;
            default:
                return;

        }
    }

    private void copyLinkToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Fixex", Const.URL_TO_SHARE);
        clipboard.setPrimaryClip(clip);
    }

    private void inviteFriendBySMS() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));

        sendIntent.putExtra("sms_body", Const.URL_TO_SHARE);

        startActivity(sendIntent);
    }

    private void shareLinkOnFacebook() {
        // TODO Const.URL_TO_SHARE is not correct now. need to update
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, Const.URL_TO_SHARE);

        // See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        // As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + Const.URL_TO_SHARE;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }

        startActivity(intent);
    }
}
