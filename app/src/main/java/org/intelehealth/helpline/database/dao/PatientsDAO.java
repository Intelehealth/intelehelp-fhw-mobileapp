package org.intelehealth.helpline.database.dao;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.intelehealth.helpline.models.FamilyMemberRes;
import org.intelehealth.helpline.services.MyIntentService;
import org.intelehealth.helpline.utilities.DateAndTimeUtils;
import org.intelehealth.helpline.utilities.Logger;
import org.intelehealth.helpline.app.AppConstants;
import org.intelehealth.helpline.app.IntelehealthApplication;
import org.intelehealth.helpline.models.Patient;
import org.intelehealth.helpline.models.dto.PatientAttributeTypeMasterDTO;
import org.intelehealth.helpline.models.dto.PatientAttributesDTO;
import org.intelehealth.helpline.models.dto.PatientDTO;
import org.intelehealth.helpline.models.pushRequestApiCall.Attribute;
import org.intelehealth.helpline.utilities.exception.DAOException;

public class PatientsDAO {

    private int updatecount = 0;
    private long createdRecordsCount = 0;

    public boolean insertPatients(List<PatientDTO> patientDTO) throws DAOException {

        boolean isInserted = true;
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        try {
            for (PatientDTO patient : patientDTO) {
                createPatients(patient, db);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            isInserted = false;
            throw new DAOException(e.getMessage(), e);
        } finally {
            db.endTransaction();
        }

        return isInserted;
    }

    public boolean createPatients(PatientDTO patient, SQLiteDatabase db) throws DAOException {
        boolean isCreated = true;
        ContentValues values = new ContentValues();
        try {
            values.put("uuid", patient.getUuid());
            values.put("openmrs_id", patient.getOpenmrsId());
            values.put("first_name", patient.getFirstname());
            values.put("middle_name", patient.getMiddlename());
            values.put("last_name", patient.getLastname());
            values.put("address1", patient.getAddress1());
            values.put("address2", patient.getAddress2());
            values.put("country", patient.getCountry());
            values.put("date_of_birth", DateAndTimeUtils.formatDateFromOnetoAnother(patient.getDateofbirth(), "MMM dd, yyyy hh:mm:ss a", "yyyy-MM-dd"));
            values.put("gender", patient.getGender());
            values.put("postal_code", patient.getPostalcode());
            values.put("state_province", patient.getStateprovince());
            values.put("city_village", patient.getCityvillage());
            values.put("modified_date", AppConstants.dateAndTimeUtils.currentDateTime());
            values.put("dead", patient.getDead());
            values.put("sync", patient.getSyncd());
            createdRecordsCount = db.insertWithOnConflict("tbl_patient", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLException e) {
            isCreated = false;
            throw new DAOException(e.getMessage(), e);
        }
        return isCreated;

    }

    public boolean insertPatientToDB(PatientDTO patientDTO, String uuid) throws DAOException {
        boolean isCreated = true;
        long createdRecordsCount1 = 0;
        SQLiteDatabase db = null;
        db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        List<PatientAttributesDTO> patientAttributesList = new ArrayList<PatientAttributesDTO>();
        try {

            Logger.logD("create", "create has to happen");
            values.put("uuid", uuid);
            values.put("openmrs_id", patientDTO.getOpenmrsId());
            values.put("first_name", patientDTO.getFirstname());
            values.put("middle_name", patientDTO.getMiddlename());
            values.put("last_name", patientDTO.getLastname());
            values.put("phone_number", patientDTO.getPhonenumber());
            values.put("address1", patientDTO.getAddress1());
            values.put("address2", patientDTO.getAddress2());
            values.put("country", patientDTO.getCountry());
            values.put("date_of_birth", patientDTO.getDateofbirth());
            values.put("gender", patientDTO.getGender());
            values.put("postal_code", patientDTO.getPostalcode());
            values.put("city_village", patientDTO.getCityvillage());
            values.put("state_province", patientDTO.getStateprovince());
            values.put("modified_date", AppConstants.dateAndTimeUtils.currentDateTime());
            values.put("patient_photo", patientDTO.getPatientPhoto());
            values.put("dead", patientDTO.getDead());
            values.put("sync", false);
            patientAttributesList = patientDTO.getPatientAttributesDTOList();
            if (patientAttributesList != null)
                insertPatientAttributes(patientAttributesList, db);
            Logger.logD("pulldata", "datadumper" + values);
            createdRecordsCount1 = db.insert("tbl_patient", null, values);
            db.setTransactionSuccessful();
            Logger.logD("created records", "created records count" + createdRecordsCount1);
        } catch (SQLException e) {
            isCreated = false;
            throw new DAOException(e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        return isCreated;

    }

    public boolean updatePatientToDB(Patient patientDTO, String uuid, List<PatientAttributesDTO> patientAttributesDTOS) throws DAOException {
        boolean isCreated = true;
        long createdRecordsCount1 = 0;
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        ContentValues values = new ContentValues();
        String whereclause = "Uuid=?";
        db.beginTransaction();
        List<PatientAttributesDTO> patientAttributesList = new ArrayList<PatientAttributesDTO>();
        try {

            Logger.logD("create", "create has to happen");
            values.put("uuid", uuid);
            values.put("openmrs_id", patientDTO.getOpenmrs_id());
            values.put("first_name", patientDTO.getFirst_name());
            values.put("middle_name", patientDTO.getMiddle_name());
            values.put("last_name", patientDTO.getLast_name());
            values.put("phone_number", patientDTO.getPhone_number());
            values.put("address1", patientDTO.getAddress1());
            values.put("address2", patientDTO.getAddress2());
            values.put("country", patientDTO.getCountry());
            values.put("date_of_birth", patientDTO.getDate_of_birth());
            values.put("gender", patientDTO.getGender());
            values.put("postal_code", patientDTO.getPostal_code());
            values.put("city_village", patientDTO.getCity_village());
            values.put("state_province", patientDTO.getState_province());
            values.put("modified_date", AppConstants.dateAndTimeUtils.currentDateTime());
            values.put("patient_photo", patientDTO.getPatient_photo());
            values.put("dead", false);
            values.put("sync", false);

            insertPatientAttributes(patientAttributesDTOS, db);
            Logger.logD("pulldata", "datadumper" + values);
            createdRecordsCount1 = db.update("tbl_patient", values, whereclause, new String[]{uuid});
            db.setTransactionSuccessful();
            Logger.logD("created records", "created records count" + createdRecordsCount1);
        } catch (SQLException e) {
            isCreated = false;
            throw new DAOException(e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        return isCreated;

    }

    public boolean patientAttributes(List<PatientAttributesDTO> patientAttributesDTOS) throws DAOException {
        boolean isInserted = true;
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < patientAttributesDTOS.size(); i++) {
                values.put("uuid", patientAttributesDTOS.get(i).getUuid());
                values.put("person_attribute_type_uuid", patientAttributesDTOS.get(i).getPersonAttributeTypeUuid());
                values.put("patientuuid", patientAttributesDTOS.get(i).getPatientuuid());
                values.put("value", patientAttributesDTOS.get(i).getValue());
                values.put("modified_date", AppConstants.dateAndTimeUtils.currentDateTime());
                values.put("sync", "TRUE");
                db.insertWithOnConflict("tbl_patient_attribute", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            isInserted = false;
            throw new DAOException(e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        return isInserted;
    }

    public List<Attribute> getPatientAttributes(String patientuuid) throws DAOException {
        List<Attribute> patientAttributesList = new ArrayList<>();
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            String query = "SELECT * from tbl_patient_attribute WHERE patientuuid= '" + patientuuid + "'";
            Cursor cursor = db.rawQuery(query, null, null);
            Attribute attribute = new Attribute();
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    attribute = new Attribute();
                    attribute.setAttributeType(cursor.getString(cursor.getColumnIndex("person_attribute_type_uuid")));
                    attribute.setValue(cursor.getString(cursor.getColumnIndex("value")));
                    patientAttributesList.add(attribute);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            db.endTransaction();

        }
        return patientAttributesList;
    }

    //Fetch householdID value using Patient UUID
    public String getHouseHoldValue(String patientuuid) throws DAOException {
        String houseHoldID = "";
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            Cursor idCursor = db.rawQuery("SELECT value FROM tbl_patient_attribute where patientuuid = ? AND person_attribute_type_uuid=? AND voided='0' COLLATE NOCASE", new String[]{patientuuid, "10720d1a-1471-431b-be28-285d64767093"});

            if (idCursor.getCount() != 0) {
                while (idCursor.moveToNext()) {
                    houseHoldID = idCursor.getString(idCursor.getColumnIndexOrThrow("value"));
                }
            }
            idCursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            throw new DAOException(e);
        } finally {
            db.endTransaction();
        }
        return houseHoldID;
    }

    //Fetch all patient UUID's from HouseHoldValue
    public List<String> getPatientUUIDs(String houseHoldValue) throws DAOException {
        List<String> patientUUIDs = new ArrayList<>();
        LinkedHashSet<String> patientUUIDs_hashset = new LinkedHashSet<>();
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT patientuuid FROM tbl_patient_attribute where value = ? AND (sync='0' OR sync='TRUE') COLLATE NOCASE", new String[]{houseHoldValue});

            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    patientUUIDs_hashset.add(cursor.getString(cursor.getColumnIndexOrThrow("patientuuid")));
                }
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        } finally {
            db.endTransaction();
        }

        patientUUIDs.addAll(patientUUIDs_hashset);
        return patientUUIDs;
    }

    public List<FamilyMemberRes> getPatientName(String patientuuid) throws DAOException {

        List<FamilyMemberRes> listPatientNames = new ArrayList<>();

        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT openmrs_id,first_name,middle_name,last_name FROM tbl_patient where uuid = ? COLLATE NOCASE", new String[]{patientuuid});
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    FamilyMemberRes familyMemberRes = new FamilyMemberRes();
                    familyMemberRes.setOpenMRSID(cursor.getString(cursor.getColumnIndexOrThrow("openmrs_id")));
                    familyMemberRes.setName(cursor.getString(cursor.getColumnIndexOrThrow("first_name"))+ " " + cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
                    listPatientNames.add(familyMemberRes);
//                  middle_name = cursor.getString(cursor.getColumnIndexOrThrow("middle_name"));
                }
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException s) {
            FirebaseCrashlytics.getInstance().recordException(s);
            throw new DAOException(s);
        } finally {
            db.endTransaction();
        }
        return listPatientNames;
    }

    public String getAttributesName(String attributeuuid) throws DAOException {
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        String name = "";
        try {
            String query = "SELECT name from tbl_patient_attribute_master WHERE uuid= '" + attributeuuid + "'";
            Cursor cursor = db.rawQuery(query, null, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    cursor.moveToNext();
                }
            }
            else if(cursor.getCount() == 0 &&
                    attributeuuid.equalsIgnoreCase("1c718819-345c-4368-aad6-d69b4c267db7")) {
                name = "Education Level";
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            throw new DAOException(e.getMessage());
        } finally {
            db.endTransaction();

        }
        return name;
    }

    public boolean insertPatientAttributes(List<PatientAttributesDTO> patientAttributesDTOS, SQLiteDatabase db) throws DAOException {
        boolean isInserted = true;
        ContentValues values = new ContentValues();
        db.beginTransaction();
        try {
            for (int i = 0; i < patientAttributesDTOS.size(); i++) {
                values.put("uuid", patientAttributesDTOS.get(i).getUuid());
                values.put("person_attribute_type_uuid", patientAttributesDTOS.get(i).getPersonAttributeTypeUuid());
                values.put("patientuuid", patientAttributesDTOS.get(i).getPatientuuid());
                values.put("value", patientAttributesDTOS.get(i).getValue());
                values.put("modified_date", AppConstants.dateAndTimeUtils.currentDateTime());
                values.put("sync", false);
                db.insertWithOnConflict("tbl_patient_attribute", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            isInserted = false;
            FirebaseCrashlytics.getInstance().recordException(e);
            throw new DAOException(e.getMessage(), e);
        } finally {
            db.endTransaction();
        }


        return isInserted;

    }


    public boolean patinetAttributeMaster(List<PatientAttributeTypeMasterDTO> patientAttributeTypeMasterDTOS) throws DAOException {
        boolean isInserted = true;
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (int i = 0; i < patientAttributeTypeMasterDTOS.size(); i++) {
                values.put("uuid", patientAttributeTypeMasterDTOS.get(i).getUuid());
                values.put("name", patientAttributeTypeMasterDTOS.get(i).getName());
                values.put("modified_date", AppConstants.dateAndTimeUtils.currentDateTime());
                values.put("sync", "TRUE");
                db.insertWithOnConflict("tbl_patient_attribute_master", null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            isInserted = false;
            FirebaseCrashlytics.getInstance().recordException(e);
            throw new DAOException(e.getMessage(), e);
        } finally {
            db.endTransaction();
        }

        return isInserted;
    }

    public String getUuidForAttribute(String attr) {
        String attributeUuid = "";
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        Cursor cursor = db.rawQuery("SELECT uuid FROM tbl_patient_attribute_master where name = ? COLLATE NOCASE", new String[]{attr});
        Log.d("count", "count: "+cursor.getCount());
        //cursor.getcount() = -1 if the column doesnt exists...
        //0 if the row is not present...
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                attributeUuid = cursor.getString(cursor.getColumnIndexOrThrow("uuid"));
            }
        }
        else if(cursor.getCount() == 0 && attr.equalsIgnoreCase("Education Level")) {
            attributeUuid = "1c718819-345c-4368-aad6-d69b4c267db7";
        }

        cursor.close();

        return attributeUuid;
    }

    public boolean updateOpemmrsId(String openmrsId, String synced, String uuid) throws DAOException {
        boolean isUpdated = true;
        Logger.logD("patinetdao", "updateopenmrs " + uuid + openmrsId + synced);
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        String whereclause = "uuid=?";
        String[] whereargs = {uuid};
        try {
            values.put("openmrs_id", openmrsId);
            values.put("sync", synced);
            values.put("uuid", uuid);
            int i = db.update("tbl_patient", values, whereclause, whereargs);
            Logger.logD("patient", "description" + i);
            db.setTransactionSuccessful();
        } catch (SQLException sql) {
            Logger.logD("patient", "patient" + sql.getMessage());
            FirebaseCrashlytics.getInstance().recordException(sql);
            throw new DAOException(sql.getMessage());
        } finally {
            db.endTransaction();


        }
        Intent intent = new Intent(IntelehealthApplication.getAppContext(), MyIntentService.class);
        IntelehealthApplication.getAppContext().startService(intent);
        return isUpdated;
    }

    public List<PatientDTO> unsyncedPatients() throws DAOException {
        List<PatientDTO> patientDTOList = new ArrayList<>();
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            Cursor idCursor = db.rawQuery("SELECT * FROM tbl_patient where (sync = ? OR sync=?) COLLATE NOCASE", new String[]{"0", "false"});
            PatientDTO patientDTO = new PatientDTO();
            if (idCursor.getCount() != 0) {
                while (idCursor.moveToNext()) {
                    patientDTO = new PatientDTO();
                    patientDTO.setUuid(idCursor.getString(idCursor.getColumnIndexOrThrow("uuid")));
                    patientDTO.setOpenmrsId(idCursor.getString(idCursor.getColumnIndexOrThrow("openmrs_id")));
                    patientDTO.setFirstname(idCursor.getString(idCursor.getColumnIndexOrThrow("first_name")));
                    patientDTO.setLastname(idCursor.getString(idCursor.getColumnIndexOrThrow("last_name")));
                    patientDTO.setMiddlename(idCursor.getString(idCursor.getColumnIndexOrThrow("middle_name")));
                    patientDTO.setGender(idCursor.getString(idCursor.getColumnIndexOrThrow("gender")));
                    patientDTO.setDateofbirth(idCursor.getString(idCursor.getColumnIndexOrThrow("date_of_birth")));
                    patientDTO.setPhonenumber(idCursor.getString(idCursor.getColumnIndexOrThrow("phone_number")));
                    patientDTO.setCountry(idCursor.getString(idCursor.getColumnIndexOrThrow("country")));
                    patientDTO.setStateprovince(idCursor.getString(idCursor.getColumnIndexOrThrow("state_province")));
                    patientDTO.setCityvillage(idCursor.getString(idCursor.getColumnIndexOrThrow("city_village")));
                    patientDTO.setAddress1(idCursor.getString(idCursor.getColumnIndexOrThrow("address1")));
                    patientDTO.setAddress2(idCursor.getString(idCursor.getColumnIndexOrThrow("address2")));
                    patientDTO.setPostalcode(idCursor.getString(idCursor.getColumnIndexOrThrow("postal_code")));
                    patientDTOList.add(patientDTO);
                }
            }
            idCursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            throw new DAOException(e);
        } finally {
            db.endTransaction();

        }

        return patientDTOList;
    }


    public boolean updatePatientPhoto(String patientuuid, String profilePhotoPath) throws DAOException {
        boolean isUpdated = true;
        Logger.logD("patinetdao", "patientphoto " + patientuuid + profilePhotoPath);
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        String whereclause = "uuid=?";
        String[] whereargs = {patientuuid};
        try {
            values.put("patient_photo", profilePhotoPath);
            values.put("uuid", patientuuid);
            int i = db.update("tbl_patient", values, whereclause, whereargs);
            Logger.logD("patient", "description" + i);
            db.setTransactionSuccessful();
        } catch (SQLException sql) {
            Logger.logD("patient", "patient" + sql.getMessage());
            isUpdated = false;
            FirebaseCrashlytics.getInstance().recordException(sql);
            throw new DAOException(sql.getMessage());
        } finally {
            db.endTransaction();


        }
        return isUpdated;
    }

    public String getOpenmrsId(String patientuuid) throws DAOException {
        String id = "";
        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT openmrs_id FROM tbl_patient where uuid = ? COLLATE NOCASE", new String[]{patientuuid});
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    id = cursor.getString(cursor.getColumnIndexOrThrow("openmrs_id"));
                }
            }
            cursor.close();
            db.setTransactionSuccessful();
        } catch (SQLException s) {
            FirebaseCrashlytics.getInstance().recordException(s);
            throw new DAOException(s);
        } finally {
            db.endTransaction();

        }
        return id;

    }

    public static String fetch_gender(String patientUuid) {
        String gender = "";

        SQLiteDatabase db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        Cursor cursor = db.query("tbl_patient", new String[]{"gender"}, "uuid=?",
                new String[]{patientUuid}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return gender;
    }


}
