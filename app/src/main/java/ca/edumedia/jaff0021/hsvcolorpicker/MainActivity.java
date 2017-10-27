package ca.edumedia.jaff0021.hsvcolorpicker;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import model.HSVModel;

/**
 * This an application that allows one to pick a color using presets and see the hsv values,
 * or using hsv slider they can choose a color
 * Zaheed Jaffer jaff0021@algonquinlive.com
 */


public class MainActivity extends Activity implements Observer, SeekBar.OnSeekBarChangeListener {

    //used to log test infromtaion
    private static final String ABOUT_DIALOG_TAG = "About";
    private static final String LOG_TAG = "HSV";

    //declaring variable
    private AboutDialogFragment mAboutDialog;
    private TextView mColorSwatch;
    private HSVModel mModel;
    private SeekBar mHueSB;
    private SeekBar mSaturationSB;
    private SeekBar mValueSB;
    private TextView mHueTV;
    private TextView mSaturationTV;
    private TextView mValueTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create model instance
        mAboutDialog = new AboutDialogFragment();
        mModel = new HSVModel();
        mModel.setHSV(HSVModel.MIN_HSV, HSVModel.MIN_HSV, HSVModel.MIN_HSV);

        mModel.addObserver(this);

        //Get References
        mColorSwatch = (TextView) findViewById(R.id.colorSwatch);
        mHueSB = (SeekBar) findViewById(R.id.hueSB);
        mSaturationSB = (SeekBar) findViewById(R.id.saturationSB);
        mValueSB = (SeekBar) findViewById(R.id.valueSB);
        mHueTV = (TextView) findViewById(R.id.hue);
        mSaturationTV = (TextView) findViewById(R.id.saturation);
        mValueTV = (TextView) findViewById(R.id.value);

        //Set max values for sliding bars
        mHueSB.setMax(HSVModel.MAX_HUE);
        mSaturationSB.setMax(100);
        mValueSB.setMax(100);

        //regesters event handlers
        mHueSB.setOnSeekBarChangeListener(this);
        mSaturationSB.setOnSeekBarChangeListener(this);
        mValueSB.setOnSeekBarChangeListener(this);

        mColorSwatch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int hue = mModel.getHue();
                float sat = mModel.getSaturation();
                sat = sat * 100;
                float val = mModel.getValue();
                val = val * 100;
                String messageToDisplay = "H: " + hue + "\u00B0 S: " + (int) sat + "% V: " + (int) val + "%";
                createToast(messageToDisplay);
                return true;
            }
        });

        this.updateView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            DialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(getFragmentManager(), ABOUT_DIALOG_TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // Did the user cause this event, if yes continue if no go away
        if (fromUser == false) {
            return;
        }

        // Determine which <SeekBark> caused the event
        //each case will take the value and display it to the view
        switch (seekBar.getId()) {
            case R.id.hueSB:
                mModel.setHue(mHueSB.getProgress());
                String hueString = getResources().getString(R.string.hueProgress, progress).toUpperCase() + "\u00B0";
                mHueTV.setText(hueString);
                break;


            //for both saturation and value had to be grabbed then multiplied because they are floats
            //between 0-1, they are converted to whole numbers to represent precentages
            case R.id.saturationSB:
                float sat = mSaturationSB.getProgress();
                sat = sat / 100;
                mModel.setSaturation(sat);
                String satString = getResources().getString(R.string.saturationProgress, progress).toUpperCase() + "%";
                mSaturationTV.setText(satString);
                break;

            case R.id.valueSB:
                float val = mValueSB.getProgress();
                val = val / 100;
                mModel.setValue(val);
                String valString = getResources().getString(R.string.valueProgress, progress).toUpperCase() + "%";
                mValueTV.setText(valString);
                break;
        }
    }

    public void changeColorPreset(View v) {
        //this function handles all the preset colors the user can click at the bottom of the view
        switch (v.getId()) {
            case R.id.buttonBlack:
                mModel.asBlack();
                break;

            case R.id.buttonRed:
                mModel.asRed();
                break;

            case R.id.buttonLime:
                mModel.asLime();
                break;

            case R.id.buttonBlue:
                mModel.asBlue();
                break;

            case R.id.buttonYellow:
                mModel.asYellow();
                break;

            case R.id.buttonCyan:
                mModel.asCyan();
                break;

            case R.id.buttonMagenta:
                mModel.asMagenta();
                break;

            case R.id.buttonSilver:
                mModel.asSilver();
                break;

            case R.id.buttonGray:
                mModel.asGray();
                break;

            case R.id.buttonMaroon:
                mModel.asMaroon();
                break;

            case R.id.buttonOlive:
                mModel.asOlive();
                break;

            case R.id.buttonGreen:
                mModel.asGreen();
                break;

            case R.id.buttonPurple:
                mModel.asPurple();
                break;

            case R.id.buttonTeal:
                mModel.asTeal();
                break;

            case R.id.buttonNavy:
                mModel.asNavy();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // No-Operation
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //resets the text after the seek bar is no longer being moved
        switch (seekBar.getId()) {
            case R.id.hueSB:
                mHueTV.setText(getResources().getString(R.string.hue));
                break;
            case R.id.saturationSB:
                mSaturationTV.setText(getResources().getString(R.string.saturation));
                break;
            case R.id.valueSB:
                mValueTV.setText(getResources().getString(R.string.value));
                break;
        }
    }

    private void createToast(String message) {
        //this creats a toast message
        Context c = getApplicationContext();
        CharSequence s = message;
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(c, s, duration).show();

    }

    @Override
    public void update(Observable observable, Object data) {
        this.updateView();
    }

    private void updateHueSB() {
        mHueSB.setProgress(mModel.getHue());
    }

    private void updateColorSwatch() {
        //GET the model's r,g,b,a values, and SET the background colour of the swatch <TextView>
        float[] hsv = {mModel.getHue(), mModel.getSaturation(), mModel.getValue()};

        mColorSwatch.setBackgroundColor(Color.HSVToColor(hsv));
    }

    private void updateSaturationSB() {
        float sat = mModel.getSaturation();
        sat = sat * 100;
        mSaturationSB.setProgress((int) sat);
    }

    private void updateValueSB() {
        float val = mModel.getValue();
        val = val * 100;
        mValueSB.setProgress((int) val);
    }

    // synchronize each View component with the Model
    public void updateView() {
        this.updateColorSwatch();
        this.updateHueSB();
        this.updateSaturationSB();
        this.updateValueSB();
    }
}
