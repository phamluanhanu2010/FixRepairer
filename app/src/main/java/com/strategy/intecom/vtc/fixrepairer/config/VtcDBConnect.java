package com.strategy.intecom.vtc.fixrepairer.config;

import android.content.ContentValues;
import android.content.Context;

import com.strategy.intecom.vtc.fixrepairer.model.VtcModelUser;


/**
 * Created by Mr. Ha on 25/05/2016.
 */

public class VtcDBConnect extends VtcDBBase {

	private Context context;

	public VtcDBConnect(Context context) {
		super(context);
		this.context = context;
	}
//
//	/**
//	 * Storage User
//	 */
//	public VtcModelUser getUserFromDB() {
//		/**
//		 * Get User
//		 *
//		 * if exist return VtcModelUser list, else return null
//		 *
//		 * */
//		String QUERRY_GET_USER = "SELECT * FROM " + VtcDBBase.TABLE_NAME_USER + " LIMIT 1";
//
//		return new VtcModelUser().setDatacCursor(OpenDB().rawQuery(QUERRY_GET_USER, new String[]{}));
//	}
//
//	public boolean initInsertUser(VtcModelUser user) {
//
//		/**
//		 * Check User
//		 *
//		 * if exist updateUserIntoDB(user), else updateUserIntoDB(user);
//		 *
//		 * */
//
//		if (getUserFromDB() == null)
//			return setUserIntoDB(user);
//		else
//			return updateUserIntoDB(user);
//	}
//
//	public boolean setUserIntoDB(VtcModelUser user) {
//
//		ContentValues insertUser = new ContentValues();
////		insertUser.put(VtcDBBase.User_id, user.getUser_id());
//		insertUser.put(VtcDBBase.User_full_name, user.getUser_full_name());
//		insertUser.put(VtcDBBase.User_CMTND, user.getUser_CMTND());
//		insertUser.put(VtcDBBase.User_Address, user.getUser_Address());
//		insertUser.put(VtcDBBase.User_Phone_Num, user.getUser_Phone_Num());
//		insertUser.put(VtcDBBase.User_Email, user.getUser_Email());
//		insertUser.put(VtcDBBase.User_City, user.getUser_City());
////		insertUser.put(VtcDBBase.User_Fields, user.getUser_Fields());
//		insertUser.put(VtcDBBase.User_Description, user.getUser_Description());
//
//		/**
//		 * Insert User
//		 *
//		 * return true == success, return false == not success
//		 *
//		 * */
//
//		boolean isOk = false;
//
//		if (OpenDB().insert(VtcDBBase.TABLE_NAME_USER, null, insertUser) > 0)
//			isOk = true;
//		else isOk = false;
//
//		return isOk;
//	}
//
//	public boolean updateUserIntoDB(VtcModelUser user) {
//
//		ContentValues updatetUser = new ContentValues();
//        updatetUser.put(VtcDBBase.User_full_name, user.getUser_full_name());
//        updatetUser.put(VtcDBBase.User_CMTND, user.getUser_CMTND());
//        updatetUser.put(VtcDBBase.User_Address, user.getUser_Address());
//        updatetUser.put(VtcDBBase.User_Phone_Num, user.getUser_Phone_Num());
//        updatetUser.put(VtcDBBase.User_Email, user.getUser_Email());
//        updatetUser.put(VtcDBBase.User_City, user.getUser_City());
////        updatetUser.put(VtcDBBase.User_Fields, user.getUser_Fields());
//        updatetUser.put(VtcDBBase.User_Description, user.getUser_Description());
//
//		/**
//		 * Update User by id User
//		 *
//		 * return true == success, return false == not success
//		 *
//		 * */
//		boolean isOk = false;
//
//		if (OpenDB().update(VtcDBBase.TABLE_NAME_USER, updatetUser, VtcDBBase.User_id + " = ?", new String[]{String.valueOf(user.getId())}) > 0)
//			isOk = true;
//		else isOk = false;
//
//		return isOk;
//	}

	public boolean deleteUserFromDB(int id) {

		/**
		 * Delete User by id User
		 *
		 * return true == success, return false == not success
		 *
		 * */
		boolean isOk = false;

		if (OpenDB().delete(VtcDBBase.TABLE_NAME_USER, VtcDBBase.User_id + " = ?", new String[]{String.valueOf(id)}) > 0)
			isOk = true;
		else isOk = false;

		return isOk;
	}

	public boolean deleteUserFromDB(Context context) {

		/**
		 * Delete User
		 *
		 * return true == success, return false == not success
		 *
		 * */
		boolean isOk = false;

		if (OpenDB().delete(VtcDBBase.TABLE_NAME_USER, null, new String[]{}) > 0)
			isOk = true;
		else isOk = false;

		return isOk;
	}

}
