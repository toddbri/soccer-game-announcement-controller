package tbville.myfirstapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class MyActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;
    // public MediaPlayer mediaPlayer = new MediaPlayer();
    public MediaPlayer mediaPlayer;
    public MediaRecorder mRecorder;
    public Button TempButton;
    public ToggleButton TogButton;
    public ToggleButton CurrTButton;
    public EditText TempValue;
    public String FILE;
    public boolean enternumbers;
    public int subsSelectedVisitors;
    public int subsSelectedHome;
    public EditText result;
    public String tempPlayerNumber;
    public boolean PlayerSelectedforNumber;
    public int PlayerButtonSelected;
    public boolean Recordingnames = false;



    public void ConfirmVisitorsGoal() {
        new AlertDialog.Builder(MyActivity.this)
                .setTitle("Confirm")
                .setMessage("Announce Goal?")
                //.setIcon(R.drawable.ninja)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                PlayFile("team_recordings/goalvisitors");
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }
    public void ConfirmHomeGoal() {
        new AlertDialog.Builder(MyActivity.this)
                .setTitle("Confirm")
                .setMessage("Announce Goal?")
                        //.setIcon(R.drawable.ninja)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                PlayFile("team_recordings/goalhome");
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }
    public void Play(View view) {

        if (mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        Spinner spinner = (Spinner) findViewById(R.id.ChooseSong);
        String Path_to_File = getFilesDir()+  "/" + spinner.getSelectedItem().toString();

        //Toast.makeText(this,Path_to_File, Toast.LENGTH_LONG).show();
        try {
            mediaPlayer.setDataSource(Path_to_File);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }

        mediaPlayer.start();

    }
    public void Play_Recording(View view){
Log.v("PR","step 1");
       if (mediaPlayer !=null){
           Log.v("PR","step 2");
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Log.v("PR","step 3");
        mediaPlayer = new MediaPlayer();
        String Path_to_File = FILE;
        //Path_to_File = FILE;
        Log.v("PR", "Path is: " + Path_to_File);
       // Toast.makeText(this, Path_to_File, Toast.LENGTH_LONG).show();
        try {
            Log.v("PR","Playing "+ Path_to_File);
            mediaPlayer.setDataSource(Path_to_File);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
            Log.v("PR", Path_to_File + " was not found");
        }
    }
    public void FileManagement(View view)throws Exception{
        TogButton = (ToggleButton) findViewById(R.id.Files);
        LinearLayout FM = (LinearLayout) findViewById(R.id.FileManagement);
        if (TogButton.isChecked()) {
            FM.setVisibility(view.VISIBLE);
        }else {
            FM.setVisibility(view.GONE);
        }

    }
    public void DoneManagingFiles(View view) {
        LinearLayout FM = (LinearLayout) findViewById(R.id.FileManagement);
        TogButton = (ToggleButton) findViewById(R.id.Files);
        TogButton.setChecked(false);
        FM.setVisibility(view.GONE);
    }
    public void DeleteFile(View view) {
        final Spinner tmpSpin = (Spinner) findViewById(R.id.ChooseSong);
        new AlertDialog.Builder(MyActivity.this)
                .setTitle("Confirm Delete")
                .setMessage("Delete " + tmpSpin.getSelectedItem().toString() + "?")
                        //.setIcon(R.drawable.ninja)
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    @TargetApi(11)
                                    public void onClick(DialogInterface dialog, int id) {
                                        File fileOut = new File(getFilesDir().toString() + "/" + tmpSpin.getSelectedItem().toString());
                                        fileOut.delete();
                                        UpdatePlaylist();
                                        dialog.cancel();
                                    }
                                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                        }).show();
    }
    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MyActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle("Enter Recoding Name");

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        //final TextView resultText = (TextView) findViewById(R.id.result);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CarryOn(editText.getText().toString());

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public void CarryOn(String strFN){

    //Toast.makeText(this,strFN,Toast.LENGTH_LONG).show();
    if (strFN.length()<1) {
        showInputDialog();
    }
    File fileOut = new File(FILE);
    File fileOut2 = new File(getFilesDir()+"/" + strFN+".3gpp");
    fileOut.renameTo(fileOut2);
    UpdatePlaylist();
}
    public void EnterSetup(View view) {
        TogButton = (ToggleButton) findViewById(R.id.EnterSetup);
        LinearLayout tmpLL;
        if (TogButton.isChecked()){
            Log.v("HERE", "Entering Setup");
            tmpLL = (LinearLayout) findViewById(R.id.PlayerSetup);
            tmpLL.setVisibility(View.VISIBLE);
            RelativeLayout tmpRL = (RelativeLayout) findViewById(R.id.Announcements);
            tmpRL.setVisibility(View.INVISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.VisitorRecordings);
            tmpLL.setVisibility(View.INVISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.HomeRecordings);
            tmpLL.setVisibility(View.INVISIBLE);

        } else {
            Log.v("HERE", "Leaving Setup");
            tmpLL = (LinearLayout) findViewById(R.id.PlayerSetup);
            tmpLL.setVisibility(View.INVISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.VisitorRecordings);
            tmpLL.setVisibility(View.INVISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.HomeRecordings);
            tmpLL.setVisibility(View.INVISIBLE);
            RelativeLayout tmpRL = (RelativeLayout) findViewById(R.id.Announcements);
            tmpRL.setVisibility(View.VISIBLE);
            TogButton = (ToggleButton) findViewById(R.id.EnterNumbers);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.Record);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.ReviewRecordings);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.RecordGoalV);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.RecordGoalH);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.inforHome);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.inforVisitors);
            TogButton.setChecked(false);

           Log.v("HERE","I'm here");
            if (mRecorder !=null) {
                Log.v("HERE","not null");
                mRecorder.release();
            }
        }

    }
    public void EnterNumbers(View view) {
        TogButton = (ToggleButton) findViewById(R.id.EnterNumbers);
        EditText  ET = (EditText) findViewById(R.id.player_number);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (TogButton.isChecked()){
            ET.setText("");
            ET.setVisibility(View.VISIBLE);
            ET.requestFocus();

            imm.showSoftInput(ET, InputMethodManager.SHOW_IMPLICIT);
        }else {
            ET.setVisibility(View.INVISIBLE);
       //     imm.hideSoftInputFromInputMethod(view.getWindowToken(),0);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
        }
    }
    public void AnnounceSubs(View view) {
        subsSelectedVisitors = 0;
        subsSelectedHome = 0;
        String PN = getPackageName();
        for (int i = 1; i < 31; i++) {
            int id = getResources().getIdentifier("vp" + i, "id", PN);
            TogButton = (ToggleButton) findViewById(id);
            if ((TogButton.isChecked())  && (TogButton.getTextOff().length()>0)) {
                subsSelectedVisitors++;
                //Toast.makeText(this, "vp" + i + " is selected" + TogButton.getTextOff() + "-", Toast.LENGTH_LONG).show();
            }
            id = getResources().getIdentifier("hp" + i, "id", PN);
            TogButton = (ToggleButton) findViewById(id);
            if ((TogButton.isChecked())  && (TogButton.getTextOff().length()>0)) {
                subsSelectedHome++;
                //Toast.makeText(this, "hp" + i + " is selected", Toast.LENGTH_LONG).show();
            }
        }
        //Toast.makeText(this, "vistors: " + subsSelectedVisitors, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "home: " + subsSelectedHome, Toast.LENGTH_SHORT).show();
            if (subsSelectedVisitors>0){
                PlayFile("team_recordings/inforvisitors");
                for (int i =1;i<31;i++){
                    int id = getResources().getIdentifier("vp" +i, "id", PN);
                    TogButton = (ToggleButton) findViewById(id);
                    if (TogButton.isChecked()){
                        PlayFile("team_recordings/vp"+i);
                        TogButton.setChecked(false);
                    }
                }
            }
            if (subsSelectedHome>0){
                PlayFile("team_recordings/inforhome");
                for (int i =1;i<31;i++){
                    int id = getResources().getIdentifier("hp" +i, "id", PN);
                    TogButton = (ToggleButton) findViewById(id);
                    if (TogButton.isChecked()){
                        PlayFile("team_recordings/hp"+i);
                        TogButton.setChecked(false);
                    }
                }
            }
        for (int i = 1; i < 31; i++) {
            int id = getResources().getIdentifier("vp" + i, "id", PN);
            TogButton = (ToggleButton) findViewById(id);
            TogButton.setChecked(false);
            id = getResources().getIdentifier("hp" + i, "id", PN);
            TogButton = (ToggleButton) findViewById(id);
           TogButton.setChecked(false);
        }

    }
    public void ReviewRecordings (View view){
        TogButton = (ToggleButton) findViewById(R.id.Record) ;
        TogButton.setChecked(false);
        if (mRecorder != null) {
            mRecorder.release();
        }
        TogButton = (ToggleButton) findViewById(R.id.ReviewRecordings);
        LinearLayout tmpLL;
        if (TogButton.isChecked()){
            tmpLL = (LinearLayout) findViewById(R.id.VisitorRecordings);
            tmpLL.setVisibility(View.VISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.HomeRecordings);
            tmpLL.setVisibility(View.VISIBLE);
        }else {
            tmpLL = (LinearLayout) findViewById(R.id.VisitorRecordings);
            tmpLL.setVisibility(View.INVISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.HomeRecordings);
            tmpLL.setVisibility(View.INVISIBLE);
        }


    }
    public void Record(View view) {
        TogButton = (ToggleButton) findViewById(R.id.Record);
        if (mRecorder != null) {
            mRecorder.release();
        }
        LinearLayout tmpLL;
        if (TogButton.isChecked()) {
            Log.v("Setup","Entering Recording Setup");
            tmpLL = (LinearLayout) findViewById(R.id.VisitorRecordings);
            tmpLL.setVisibility(View.VISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.HomeRecordings);
            tmpLL.setVisibility(View.VISIBLE);
            TogButton = (ToggleButton) findViewById(R.id.ReviewRecordings) ;
            TogButton.setChecked(false);

        } else {
            Log.v("Setup","Leaving Recording Setup");
            tmpLL = (LinearLayout) findViewById(R.id.VisitorRecordings);
            tmpLL.setVisibility(View.INVISIBLE);
            tmpLL = (LinearLayout) findViewById(R.id.HomeRecordings);
            tmpLL.setVisibility(View.INVISIBLE);
            TogButton = (ToggleButton) findViewById(R.id.inforVisitors);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.inforHome);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.RecordGoalV);
            TogButton.setChecked(false);
            TogButton = (ToggleButton) findViewById(R.id.RecordGoalH);
            TogButton.setChecked(false);
        }
        TogButton = (ToggleButton) findViewById(R.id.Record); //Record Player Names

        //mRecorder.stop();
        for (int i = 1; i < 31; i++) {
            int id = getResources().getIdentifier("vp" + i, "id", getPackageName());
            TogButton = (ToggleButton) findViewById(id);
            TogButton.setChecked(false);
            id = getResources().getIdentifier("hp" + i, "id", getPackageName());
            TogButton = (ToggleButton) findViewById(id);
            TogButton.setChecked(false);
        }
    }
    public void InForHome (View view)throws Exception{
        int id = view.getId();
        //final String numPlayer;
        CurrTButton = (ToggleButton) findViewById(id);
        TogButton = (ToggleButton) findViewById(R.id.Record);
        if (TogButton.isChecked()) { //Recording Mode
            if (CurrTButton.isChecked()) {
                Toast.makeText(this, "Starting the Recording", Toast.LENGTH_LONG).show();
                if (mRecorder != null) {
                    mRecorder.release();
                }
                FILE = getFilesDir() + "/team_recordings/inforhome.3gpp";
                File fileOut = new File(FILE);
                if (fileOut != null) {
                    fileOut.delete();
                }

                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                mRecorder.setOutputFile(FILE);
                mRecorder.prepare();
                mRecorder.start();
            } else {
                Toast.makeText(this, "Stopping the Recording", Toast.LENGTH_LONG).show();
                mRecorder.stop();
            }
        }else { //review mode
            PlayFile("team_recordings/inforhome");
            CurrTButton.setChecked(false);
        }
    }
    public void RecordGoalV (View view)throws Exception{
        int id = view.getId();
        //final String numPlayer;
        CurrTButton = (ToggleButton) findViewById(id);
        TogButton = (ToggleButton) findViewById(R.id.Record);
        if (TogButton.isChecked()) { //Record Mode
            if (CurrTButton.isChecked()) {
                Toast.makeText(this, "Starting the Recording", Toast.LENGTH_LONG).show();
                if (mRecorder != null) {
                    mRecorder.release();
                }
                FILE = getFilesDir() + "/team_recordings/goalvisitors.3gpp";
                File fileOut = new File(FILE);
                if (fileOut != null) {
                    fileOut.delete();
                }

                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                mRecorder.setOutputFile(FILE);
                mRecorder.prepare();
                mRecorder.start();
            } else {
                Toast.makeText(this, "Stopping the Recording", Toast.LENGTH_LONG).show();
                mRecorder.stop();
            }
        } else { //Review Mode
            CurrTButton.setChecked(false);
            PlayFile("team_recordings/goalvisitors");
        }
    }
    public void RecordGoalH (View view)throws Exception{
        int id = view.getId();
        //final String numPlayer;
        CurrTButton = (ToggleButton) findViewById(id);
        TogButton = (ToggleButton) findViewById(R.id.Record);
        if (TogButton.isChecked()) { //Record Mode
            if (CurrTButton.isChecked()) {
                Toast.makeText(this, "Starting the Recording", Toast.LENGTH_LONG).show();
                if (mRecorder != null) {
                    mRecorder.release();
                }
                FILE = getFilesDir() + "/team_recordings/goalhome.3gpp";
                File fileOut = new File(FILE);
                if (fileOut != null) {
                    fileOut.delete();
                }

                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                mRecorder.setOutputFile(FILE);
                mRecorder.prepare();
                mRecorder.start();
            } else {
                Toast.makeText(this, "Stopping the Recording", Toast.LENGTH_LONG).show();
                mRecorder.stop();
            }
        }else { //Review Mode
            CurrTButton.setChecked(false);
            PlayFile("team_recordings/goalhome");
        }
    }
    public void InForVisitors (View view)throws Exception{
        int id = view.getId();
        //final String numPlayer;
        CurrTButton = (ToggleButton) findViewById(id);
        TogButton = (ToggleButton) findViewById(R.id.Record);
        if (TogButton.isChecked()) { //Record mode
            if (CurrTButton.isChecked()) {
                Toast.makeText(this, "Starting the Recording", Toast.LENGTH_LONG).show();
                if (mRecorder != null) {
                    mRecorder.release();
                }
                FILE = getFilesDir() + "/team_recordings/inforvisitors.3gpp";
                File fileOut = new File(FILE);
                if (fileOut != null) {
                    fileOut.delete();
                }

                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                mRecorder.setOutputFile(FILE);
                mRecorder.prepare();
                mRecorder.start();
            } else {
                Toast.makeText(this, "Stopping the Recording", Toast.LENGTH_LONG).show();
                mRecorder.stop();
            }
        } else { //Review mode
            PlayFile("team_recordings/inforvisitors");
            CurrTButton.setChecked(false);
        }
    }
    public void PlayFile(String strfile){
        if (mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        File file;
        FILE = getFilesDir()+ "/" + strfile + ".3gpp";
        file = new File(FILE);
        String Path_to_File = FILE;
        Log.v("PlayFile","Playing: " + Path_to_File);
        //Toast.makeText(this, "Playing: " + Path_to_File, Toast.LENGTH_SHORT).show();
        //Path_to_File = FILE;
        if (file.exists()) {

            try {
                mediaPlayer.setDataSource(Path_to_File);
                mediaPlayer.prepare();
            } catch (Exception e){

            }

            mediaPlayer.start();
            while (mediaPlayer.isPlaying()) {

            }

        }
    }
    public void GoalVisitors(View view) {
        ConfirmVisitorsGoal();

    }
    public void GoalHome(View view) {
        ConfirmHomeGoal();
    }
    public void UpdateButton(View view) {
        if (PlayerSelectedforNumber==true) {
            TogButton = (ToggleButton) findViewById(PlayerButtonSelected);
            TempValue = (EditText) findViewById(R.id.player_number);
            TogButton.setTextOff(TempValue.getText());
            TogButton.setTextOn(TempValue.getText());
            PlayerSelectedforNumber = false;
            TogButton.setChecked(false);
        }
    }
    public void PlayerSelected(View view) throws Exception{
        int id = view.getId();
        //final String numPlayer;
        CurrTButton = (ToggleButton) findViewById(id);
        String numPlayer2;
        String strID = Integer.valueOf(id).toString();
        String strName = getResources().getResourceEntryName(id);

        TogButton = (ToggleButton) findViewById(R.id.EnterNumbers); //Set Player Numbers
        if (TogButton.isChecked()){
            TempValue = (EditText) findViewById(R.id.player_number);
            String sundrytxt = TempValue.getText().toString();
            CurrTButton.setTextOff(TempValue.getText());
            CurrTButton.setTextOn(TempValue.getText());
            TempValue.setText("");
            CurrTButton.setChecked(false);

            SharedPreferences sharedPref = getSharedPreferences("mine",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(strName, id + "," + sundrytxt);
            editor.commit();
            //Toast.makeText(this, strName +", "+ id + ", "+ sundrytxt, Toast.LENGTH_SHORT).show();
        }

        TogButton = (ToggleButton) findViewById(R.id.Record); //Record Player Names
        if (TogButton.isChecked()) {
            if (CurrTButton.isChecked()) {
                Toast.makeText(this, "Starting the Recording", Toast.LENGTH_LONG).show();
                if (mRecorder != null) {
                    mRecorder.release();
                }
                FILE = getFilesDir() + "/team_recordings/" + strName + ".3gpp";
                File fileOut = new File(FILE);
                if (fileOut != null) {
                    fileOut.delete();
                }

                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
                mRecorder.setOutputFile(FILE);
                mRecorder.prepare();
                mRecorder.start();
            } else {
                Toast.makeText(this, "Stopping the Recording", Toast.LENGTH_LONG).show();
                mRecorder.stop();
            }
        }
            TogButton = (ToggleButton) findViewById(R.id.ReviewRecordings); //Playback Player names
            if (TogButton.isChecked()){
                if (mediaPlayer !=null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                mediaPlayer = new MediaPlayer();
                File file;
                FILE = getFilesDir()+ "/team_recordings/" + strName + ".3gpp";
                file = new File(FILE);
                String Path_to_File = FILE;
                //Path_to_File = FILE;
                if (file.exists()) {
                    mediaPlayer.setDataSource(Path_to_File);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                }
                CurrTButton.setChecked(false);
            }

    }
    public void Record_Sound(View view) throws Exception    {
        TogButton = (ToggleButton) findViewById(R.id.Record2);
        if (TogButton.isChecked()) {
           Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
            if (mRecorder != null) {
                mRecorder.release();
            }
            FILE = getFilesDir() + "/temp/recording.3gpp";
            File fileOut = new File(FILE);
            if (fileOut != null) {
                fileOut.delete();
            }

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mRecorder.setOutputFile(FILE);
            mRecorder.prepare();
            mRecorder.start();
        }else {
            if (mRecorder !=null) {
                mRecorder.stop();
                showInputDialog();
            }
        }
    }
    public void MPPause(View view){
        TempButton = (Button) findViewById(R.id.Pause) ;
        if (TempButton.getText().toString().equals("Pause")){
            mediaPlayer.pause();
            TempButton.setText("Continue");
        } else {
            mediaPlayer.start();
            TempButton.setText("Pause");
        }

    }
    public void MPStop(View view){
        mediaPlayer.stop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG);
                // .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void Copy_Raw_Files(Integer intSong, String strIn) throws Exception{

        InputStream in = getResources().openRawResource(intSong);
        FileOutputStream out = new FileOutputStream(getFilesDir()+"/" + strIn);
        byte[] buff = new byte[1024];
        int read = 0;

        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }

        } finally {
            in.close();
            out.close();
        }
    }
    public void UpdatePlaylist(){
        String strFile = getFilesDir().toString();
        File sundry = new File(strFile);
        //AssetManager mgr = getAssets();
        //Toast.makeText(this,strFile,Toast.LENGTH_LONG).show();
        String[] paths={"1","2"};

        paths = sundry.list();

        int j=0;
        for (int i = 0; i < sundry.list().length; i++) {

           File filetemp = new File(getFilesDir()+"/" + paths[i]);
            if (filetemp.isFile()){
                //Toast.makeText(this,paths[i] + " is a File",Toast.LENGTH_LONG).show();
                j++;
            }else {
                //Toast.makeText(this,paths[i] + " is NOT a File",Toast.LENGTH_LONG).show();
            }

        }
        String[] recordings = new String[j];
        j=0;
        for (int i = 0; i < sundry.list().length; i++) {
            File filetemp = new File(getFilesDir()+"/"+ paths[i]);
            if (filetemp.isFile()){
                recordings[j]=paths[i];
                j++;
            }

        }

        Spinner spinner = (Spinner) findViewById(R.id.ChooseSong);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,recordings);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
      }

    @Override
    public void onStart() {
        File walldir = new File(getFilesDir()+"/temp/");
        walldir.mkdirs();
/*        for (int i = 1; i < 31; i++) {
            FILE = getFilesDir()+"/vp"+ i + ".3gpp";
            File fileOut = new File(FILE);
            File fileOut2 = new File(getFilesDir()+"/team_recordings/vp" + i +".3gpp");
            fileOut.renameTo(fileOut2);
        }*/

/*        FILE = getFilesDir()+"/team_recordings2";
        File fileOut = new File(FILE);
        File fileOut2 = new File(getFilesDir()+"/team_recordings");
        fileOut.renameTo(fileOut2);*/
 /*       FILE = getFilesDir()+"/goalvisitors.3gpp";
        fileOut = new File(FILE);
        fileOut2 = new File(getFilesDir()+"/team_recordings/goalvisitors.3gpp");
        fileOut.renameTo(fileOut2);*/
/*        FILE = getFilesDir()+"/new one3gpp";
        File fileOut = new File(FILE);
        fileOut.delete();*/

        enternumbers=false;
        super.onStart();
        SharedPreferences sharedPref;
        sharedPref = getSharedPreferences("mine",Context.MODE_PRIVATE);
        String PN = getPackageName();
        for (int i = 1; i < 31; i++) {
            String temp1;
            String temp2;
            try {
                temp1 = sharedPref.getString("vp" + i, "").split(",")[0];
                temp2 = sharedPref.getString("vp" + i, "").split(",")[1];
            } catch(Exception e) {
                temp1 = "0";
                temp2 = "";
            }

            int id = getResources().getIdentifier("vp" + i, "id", PN);
            TogButton = (ToggleButton) findViewById(id);

            try {
                TogButton.setTextOff(temp2.toString());
                TogButton.setTextOn(temp2.toString());
                TogButton.setChecked(TogButton.isChecked());
            } catch(Exception e) {

            }
        }
        for (int i = 1; i < 31; i++) {
            String temp1;
            String temp2;
            try {
                temp1 = sharedPref.getString("hp" + i, "").split(",")[0];
                temp2 = sharedPref.getString("hp" + i, "").split(",")[1];
            } catch(Exception e) {
                temp1 = "0";
                temp2 = "";
            }
            int tempint = Integer.parseInt(temp1.trim());
            int id = getResources().getIdentifier("hp" + i, "id", PN);
            //TogButton = (ToggleButton) findViewById(tempint);
            TogButton = (ToggleButton) findViewById(id);

            try {
                TogButton.setTextOff(temp2.toString());
                TogButton.setTextOn(temp2.toString());
                TogButton.setChecked(TogButton.isChecked());
            } catch(Exception e) {

            }
        }

        //Toast.makeText(this,"I'm starting", Toast.LENGTH_SHORT).show();
        FILE = getFilesDir()+ "/temp/recording.3gpp";
        //Toast.makeText(this, FILE, Toast.LENGTH_LONG).show();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "My Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://tbville.myfirstapp/http/host/path")
        );
        //AppIndex.AppIndexApi.start(client, viewAction);
        try {
            Copy_Raw_Files(R.raw.aja, "Aja.mp3");
            Copy_Raw_Files(R.raw.slitskirts, "Slit Skirts.mp3");
            Copy_Raw_Files(R.raw.whentheleveebreaks, "When the Levee Breaks.mp3");
            Copy_Raw_Files(R.raw.nationalanthemusarmyband, "National Anthem by US Army Band and Chrous.mp3");
            Copy_Raw_Files(R.raw.nacarrieunderwood, "National Anthem - Carrie Underwood.mp3");
            Copy_Raw_Files(R.raw.namarineband, "National Anthem - US Marine Band.mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }

        UpdatePlaylist();

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "My Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://tbville.myfirstapp/http/host/path")
        );
        //AppIndex.AppIndexApi.end(client, viewAction);
        //client.disconnect();
    }


}
