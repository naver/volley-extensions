package com.navercorp.volleyextensions.sample.volleyer.twitter.util;

import com.navercorp.volleyextensions.volleyer.util.StringUtils;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtils {

	public static ProgressDialog createDialog(Context context, String initialMessage) {
		ProgressDialog dialog = new ProgressDialog(context);
		changeDialogMessage(dialog, initialMessage);
		return dialog;
	}

	public static void changeDialogMessage(ProgressDialog dialog, String message) {
		if (StringUtils.isEmpty(message)) {
			return;
		}

		dialog.setMessage(message);
		dialog.show();

	}

	public static void hideDialog(ProgressDialog dialog) {
		if (dialog == null) {
			return;
		}
		dialog.dismiss();
	}

}
