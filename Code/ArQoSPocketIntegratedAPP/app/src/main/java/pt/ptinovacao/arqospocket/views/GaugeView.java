package pt.ptinovacao.arqospocket.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.R;

@SuppressWarnings("deprecation")
public class GaugeView extends LinearLayout {
    private final static Logger LOGGER = LoggerFactory.getLogger(GaugeView.class);

    private int gaugeDiameter; // Gauge radius
    private int gaugeBackground; // Gauge background resource
    private int needleBackground; // Needle background resource
    private int needleWidth; // Needle width
    private int needleHeight; // Needle height
    private int needleX; // Needle X position
    private int needleY; // Needle Y position
    private int needleDeltaX; // Needle's X position from the centre of gauge
    private int needleDeltaY; // Needle's Y position from the centre of gauge
    private int deflectTime; // Animation time when needle deflects to a higher
    // angle
    private int releaseTime; // Animation time when needle deflects to a lower
    // angle
    private int pivotX; // Needles X Axis of rotation
    private int pivotY; // Needles Y Axis of rotation
    private int deltaXAxis; // Needles new X Axis of rotation
    private int deltaYAxis; // Needles new Y Axis of rotation
    private float currentValue; // Current needle value
    private float minValue; // Minimum needle value
    private float maxValue; // Maximum needle value
    private float currentAngle; // Current angular position of needle(Used in
    // rotate animation)
    private float previousAngle; // To store last known angular position of
    // needle(Used in rotate animation)
    private float minAngle; // Minimum angle of needle
    private float maxAngle; // Maximum angle of needle
    private float currentDegrees; // Current angular position of needle

    private boolean animateDeflect; // Enable/Disable rotate animation
    private GaugeType type; // Gauge type
    private NeedleDeflectListener NDL;

    public interface NeedleDeflectListener {
        /**
         * Called when needle value or angle is changed
         */
        public void onDeflect(float angle, float value);

    }

    /**
     * Register a callback to be invoked when the needle value/angle is changed.
     */
    public void setOnNeedleDeflectListener(NeedleDeflectListener eventListener) {
        NDL = eventListener;
    }

    private AbsoluteLayout guageBack;
    private LinearLayout gaugeNeedle;

    /**
     * Custom view used for creating analog gauges like speedometer
     */
    public GaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(R.layout.gauge_layout, this);
            initView();
        }

    }

    private void initView() // Initializes the view
    {
        this.gaugeBackground = R.mipmap.speedometer_network_mobile;
        this.needleBackground = R.mipmap.pointer45;
        this.type = GaugeType.Unknown;
        this.gaugeDiameter = 0;
        this.needleWidth = 0;
        this.needleHeight = 0;
        this.needleX = 0;
        this.needleY = 0;
        this.needleDeltaX = 0;
        this.needleDeltaY = 0;
        this.currentValue = 0;
        this.minValue = 0;
        this.maxValue = 100;
        this.currentAngle = 0;
        this.minAngle = 0;
        this.maxAngle = 360;
        this.deflectTime = 0;
        this.releaseTime = 0;
        this.pivotX = 0;
        this.pivotY = 0;
        this.previousAngle = 0;
        this.deltaXAxis = 0;
        this.deltaYAxis = 0;
        this.currentDegrees = 0;
        this.animateDeflect = true;
        this.gaugeNeedle = (LinearLayout) findViewById(R.id.gaugeNeedleLay);
        this.guageBack = (AbsoluteLayout) findViewById(R.id.gaugeFrame);
        this.guageBack.setBackgroundResource(gaugeBackground);
        this.gaugeNeedle.setBackgroundResource(needleBackground);
        this.gaugeNeedle.bringToFront();
    }

    /**
     * Sets a background resource for the gauge
     */
    public void setGaugeBackgroundResource(int resID) {
        gaugeBackground = resID;
        guageBack.setBackgroundResource(0);
        guageBack.setBackgroundResource(gaugeBackground);
        guageBack.refreshDrawableState();
    }

    /**
     * Sets the Diameter of the gauge
     */
    public void setDiameter(int diameter) {
        gaugeDiameter = diameter;
        guageBack.setLayoutParams(new LayoutParams(gaugeDiameter, gaugeDiameter));
    }

    /**
     * Sets the Type of the gauge
     */
    public void setType(GaugeType type) {

        this.type = type;

        switch (type) {
            case MobileNetwork2G:
                setGaugeBackgroundResource(R.mipmap.speedometer_2g);
                setNeedleBackgroundResource(R.mipmap.pointer_south);
                setValueRange(-140, -25);
                setAngleRange(0, 270);
                break;
            case MobileNetwork3G:
                setGaugeBackgroundResource(R.mipmap.speedometer_3g);
                setNeedleBackgroundResource(R.mipmap.pointer_south);
                setValueRange(-140, -25);
                setAngleRange(0, 270);
                break;
            case MobileNetwork4G:
                setGaugeBackgroundResource(R.mipmap.speedometer_4g);
                setNeedleBackgroundResource(R.mipmap.pointer_south);
                setValueRange(-140, -25);
                setAngleRange(0, 270);
                break;
            case WifiSignalLevel:
                setGaugeBackgroundResource(R.mipmap.wifi_signal_level);
                setNeedleBackgroundResource(R.mipmap.pointer_south);
                setValueRange(-90, -5);
                setAngleRange(0, 225);
                break;
            case Mobile_disabled:
                setGaugeBackgroundResource(R.mipmap.dashboard_mobile_network_unavailable);
                setNeedleBackgroundResource(R.mipmap.dashboard_pointer_disable);
                setValueRange(-115, -75);
                setAngleRange(0, 180);
                break;
            case Wifi_disabled:
                setGaugeBackgroundResource(R.mipmap.dashboard_wifi_unavailable);
                setNeedleBackgroundResource(R.mipmap.dashboard_pointer_disable);
                setValueRange(-90, -5);
                setAngleRange(0, 225);
                break;
        }
    }

    /**
     * Returns the type of this Gauge
     */
    public GaugeType getType() {
        return this.type;
    }

    /**
     * Sets a background resource for the needle
     */
    public void setNeedleBackgroundResource(int resID) {
        needleBackground = resID;
        gaugeNeedle.setBackgroundResource(needleBackground);
    }

    /**
     * Creates a needle at the centre of the gauge. <br>
     * <b>deltaX</b>: Adjusts needle's X position from the centre of gauge <br>
     * <b>deltaY</b>: Adjusts needle's Y position from the centre of gauge
     */
    public void createNeedle(int width, int height, int deltaX, int deltaY) {
        this.needleWidth = width;
        this.needleHeight = height;
        this.needleDeltaX = deltaX;
        this.needleDeltaY = deltaY;

        this.needleX = guageBack.getLeft() + (gaugeDiameter / 2) + needleDeltaX - needleWidth / 2;
        this.needleY = guageBack.getTop() + (gaugeDiameter / 2) + needleDeltaY;

        this.pivotX = needleWidth / 2;
        this.pivotY = Math.abs(needleDeltaY);

        AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(this.needleWidth, this.needleHeight, this.needleX, this.needleY);
        gaugeNeedle.setLayoutParams(params);
    }

    /**
     * Sets a reference background for the gauge
     */
    public void setReferenceBackground() {
        // guageBack.setBackgroundResource(R.drawable.degrees);
    }

    /**
     * Removes the reference background of the gauge
     */
    public void removeReferenceBackground() {
        guageBack.setBackgroundResource(this.gaugeBackground);
    }

    /**
     * Sets the current needle value
     */
    public void setCurrentValue(float value) {

        if (value > maxValue)
            this.currentValue = maxValue;
        else if (value < minValue)
            this.currentValue = minValue;
        else
            this.currentValue = value;


        if (this.type == GaugeType.MobileNetwork2G) {
     //       LOGGER.debug("MobileNetwork2G");

            float r = 45 / 10f;

            if (currentValue >= -140 && currentValue < -115) {
                r = 45 / 25f;
                this.currentAngle = (140 + currentValue) * r;
            } else if (currentValue >= -115 && currentValue < -105) {
                this.currentAngle = ((115 + currentValue) * r) + 45;
            } else if (currentValue >= -105 && currentValue < -95) {
                this.currentAngle = ((105 + currentValue) * r) + 90;
            } else if (currentValue >= -95 && currentValue < -85) {
                this.currentAngle = ((95 + currentValue) * r) + 135;
            } else if (currentValue >= -85 && currentValue <= -75) {
                this.currentAngle = ((85 + currentValue) * r) + 180;
            } else if (currentValue >= -75 && currentValue <= 0) {
                r = 45 / 50f;
                this.currentAngle = ((75 + currentValue) * r) + 225;
            }

        } else if (this.type == GaugeType.MobileNetwork3G) {
          //  LOGGER.debug("MobileNetwork3G");
            float r = 45 / 10f;

            if (currentValue >= -140 && currentValue < -115) {
                r = 45 / 25f;
                this.currentAngle = (140 + currentValue) * r;
            } else if (currentValue >= -115 && currentValue < -105) {
                this.currentAngle = ((115 + currentValue) * r) + 45;
            } else if (currentValue >= -105 && currentValue < -95) {
                this.currentAngle = ((105 + currentValue) * r) + 90;
            } else if (currentValue >= -95 && currentValue < -85) {
                this.currentAngle = ((95 + currentValue) * r) + 135;
            } else if (currentValue >= -85 && currentValue <= -75) {
                this.currentAngle = ((85 + currentValue) * r) + 180;
            } else if (currentValue >= -75 && currentValue <= 0) {
                r = 45 / 50f;
                this.currentAngle = ((75 + currentValue) * r) + 225;
            }

        } else if (this.type == GaugeType.MobileNetwork4G) {

        //    LOGGER.debug("MobileNetwork4G");
            float r = 45 / 10f;

            if (currentValue >= -140 && currentValue < -115) {
                r = 45 / 25f;
                this.currentAngle = (140 + currentValue) * r;
            } else if (currentValue >= -115 && currentValue < -105) {
                this.currentAngle = ((115 + currentValue) * r) + 45;
            } else if (currentValue >= -105 && currentValue < -95) {
                this.currentAngle = ((105 + currentValue) * r) + 90;
            } else if (currentValue >= -95 && currentValue < -85) {
                this.currentAngle = ((95 + currentValue) * r) + 135;
            } else if (currentValue >= -85 && currentValue <= -75) {
                this.currentAngle = ((85 + currentValue) * r) + 180;
            } else if (currentValue >= -75 && currentValue <= 0) {
                r = 45 / 50f;
                this.currentAngle = ((75 + currentValue) * r) + 225;
            }

        } else if (this.type == GaugeType.WifiSignalLevel) {

          //  LOGGER.debug("WifiSignalLevel");

            float r = 45 / 10f;

            if (currentValue >= -90 && currentValue < -85) {
                r = 45 / 5f;
                this.currentAngle = (90 + currentValue) * r;
            } else if (currentValue >= -85 && currentValue < -75) {
                this.currentAngle = ((85 + currentValue) * r) + 45;
            } else if (currentValue >= -75 && currentValue < -65) {
                this.currentAngle = ((75 + currentValue) * r) + 90;
            } else if (currentValue >= -65 && currentValue < -55) {
                this.currentAngle = ((65 + currentValue) * r) + 135;
            } else if (currentValue >= -55 && currentValue <= -5) {
                r = 45 / 50f;
                this.currentAngle = ((55 + currentValue) * r) + 180;
            }

        } else if (this.type == GaugeType.Wifi_disabled || this.type == GaugeType.Mobile_disabled) {
            this.currentAngle = 0f;
        }

        if (NDL != null)
            NDL.onDeflect(currentAngle, currentValue);

        setCurrentAngle(this.currentAngle);
    }

    /**
     * Sets the needle value range
     */
    public void setValueRange(float min_Value, float max_Value) {
        this.minValue = min_Value;
        this.maxValue = max_Value;

    }

    /**
     * Sets the needle angle range (0-360)
     */
    public void setAngleRange(float min_Angle, float max_Angle) {
        if (min_Angle < 0)
            min_Angle = 0;
        if (max_Angle > 360)
            max_Angle = 360;
        this.minAngle = min_Angle;
        this.maxAngle = max_Angle;
    }

    /**
     * Sets the current needle angle
     */
    public void setCurrentAngle(float angle) {
        if (angle > maxAngle)
            this.currentAngle = maxAngle;
        else if (angle < minAngle)
            this.currentAngle = minAngle;
        else
            this.currentAngle = angle;

        if (currentAngle == this.previousAngle) {
            return;
        }

        RotateAnimation needleDeflection = new RotateAnimation(this.previousAngle, this.currentAngle, this.pivotX, this.pivotY) {
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                super.applyTransformation(interpolatedTime, t);
            }

        };

        needleDeflection.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                previousAngle = currentAngle;
            }
        });

        if (currentAngle > this.previousAngle)
            needleDeflection.setDuration(this.deflectTime);
        else
            needleDeflection.setDuration(this.releaseTime);

        if (!animateDeflect)
            needleDeflection.setDuration(0);

        needleDeflection.setFillAfter(true);
        this.gaugeNeedle.startAnimation(needleDeflection);
        this.gaugeNeedle.refreshDrawableState();
    }

    /**
     * Sets the needle's animation time <br>
     * <b>deflectTime</b>: Time taken by the needle to deflect to a higher
     * value/angle <br>
     * <b>releaseTime</b>: Time taken by the needle to deflect to a lower
     * value/angle
     */
    public void setAnimationTime(int deflectTime, int releaseTime) {
        this.releaseTime = releaseTime;
        this.deflectTime = deflectTime;
    }

    /**
     * Sets the axis of needle rotation with respect to the centre of gauge
     */
    public void setDeltaAxis(int deltaX, int deltaY) {
        this.deltaXAxis = deltaX;
        this.deltaYAxis = deltaY;
        this.pivotX = (needleWidth / 2) + deltaXAxis;
        this.pivotY = deltaYAxis;
    }

    /**
     * Returns the current needle angle
     */
    public float getCurrentAngle() {
        return this.currentDegrees;
    }

    /**
     * Returns the Background resource ID of the gauge
     */
    public int getGaugeBackgroundResource() {
        return this.gaugeBackground;
    }

    /**
     * Returns the Diameter of the gauge
     */
    public int getDiameter() {
        return this.gaugeDiameter;
    }

    /**
     * Returns the Background resource ID of the needle
     */
    public int getNeedleBackgroundResource() {
        return this.needleBackground;
    }

    /**
     * Returns the current needle value
     */
    public float getCurrentValue() {
        return this.currentValue;
    }

    /**
     * Returns the needle width
     */
    public int getNeedleWidth() {
        return this.needleWidth;
    }

    /**
     * Returns the needle height
     */
    public int getNeedleHeight() {
        return this.needleHeight;
    }

    /**
     * Returns the X position of needle
     */
    public int getNeedlePositionX() {
        return this.needleX;
    }

    /**
     * Returns the Y position of needle
     */
    public int getNeedlePositionY() {
        return this.needleY;
    }

    /**
     * Returns the X axis of rotation of needle
     */
    public int getNeedleAxisX() {
        return this.pivotX;
    }

    /**
     * Returns the X axis of rotation of needle
     */
    public int getNeedleAxisY() {
        return this.pivotY;
    }

    /**
     * Returns the minimum needle value
     */
    public float getMinValue() {
        return this.minValue;
    }

    /**
     * Returns the maximum needle value
     */
    public float getMaxValue() {
        return this.maxValue;
    }

    /**
     * Returns the minimum needle angle
     */
    public float getMinAngle() {
        return this.minAngle;
    }

    /**
     * Returns the maximum needle angle
     */
    public float getMaxAngle() {
        return this.maxAngle;
    }

    /**
     * Returns the needle deflect time
     */
    public int getDeflectTime() {
        return this.deflectTime;
    }

    /**
     * Returns the needle release time
     */
    public int getReleaseTime() {
        return this.releaseTime;
    }

    /**
     * Enable/disable needle animation
     */
    public void setNeedleAnimation(boolean EnableAnimation) {
        this.animateDeflect = EnableAnimation;

    }

    /**
     * Returns needle animation state
     */
    public boolean getNeedletAnimation() {
        return this.animateDeflect;

    }

    @SuppressWarnings("ResourceType")
    public int getColor(GaugeType type, float angle) {

        TypedArray gaugeColors = getResources().obtainTypedArray(R.array.gauge_colors);

        if (type == GaugeType.MobileNetwork3G
                || type == GaugeType.MobileNetwork4G
                || type == GaugeType.WifiSignalLevel) {

            if (angle >= 0 && angle <= 45)
                return gaugeColors.getColor(0, 0);
            if (angle > 45 && angle <= 90)
                return gaugeColors.getColor(1, 0);
            if (angle > 90 && angle <= 135)
                return gaugeColors.getColor(2, 0);
            if (angle > 135 && angle <= 180)
                return gaugeColors.getColor(3, 0);
            if (angle > 180 && angle <= 225)
                return gaugeColors.getColor(4, 0);
            if (angle > 180 && angle <= 225)
                return gaugeColors.getColor(5, 0);

        } else if (type == GaugeType.MobileNetwork2G) {

            if (angle >= 0 && angle <= 45)
                return gaugeColors.getColor(1, 0);
            if (angle >= 45 && angle <= 90)
                return gaugeColors.getColor(2, 0);
            if (angle > 90 && angle <= 135)
                return gaugeColors.getColor(3, 0);
            if (angle > 135 && angle <= 180)
                return gaugeColors.getColor(4, 0);

        }
        return gaugeColors.getColor(0, 0);
    }

}
