package com.jb.utaheracleia.bluetoothtest;

import android.util.JsonWriter;

import com.jb.utaheracleia.bluetoothtest.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JsonConverter extends MainActivity
{
    void SetDate(AtecActivity activity)
    {
        activity.TimeStarted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    String GetJsonString(AtecActivity activity) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("TaskId", activity.TaskId);
            jsonObject.put("TimeStarted", activity.TimeStarted);
            jsonObject.put("ActivityType", activity.ActivityType.ordinal());
        }
        catch(JSONException exception)
        {
            ShowToast("Error converting to JSON");
        }
        return jsonObject.toString();
    }
}
/*
    public enum ActivityType {
        Introduction,
        Practice,
        Test
    }

    public class AtecActivity {
        public int TaskId;
        public String TimeStarted;
        public ActivityType ActivityType;
    }

    public enum AtecCommand {
        StartTest,
        EndTest,
        StartActivity,
        EndActivity
    }
 */
