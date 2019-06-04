package cos.mos.utils.widget.progress.bubble;

import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;


/**
 * @Author: woxingxiao
 * @Date: 2017年03月14日
 * @apiNote 基于https://github.com/woxingxiao/BubbleSeekBar修改
 * BubbleSeekBar Created by woxingxiao on 2016-10-27.
 */
public class KBubbleConfigBuilder {
    float min;
    float max;
    float progress;
    boolean floatType;
    int trackSize;
    int secondTrackSize;
    int thumbRadius;
    int thumbRadiusOnDragging;
    int trackColor;
    int secondTrackColor;
    int thumbColor;
    int sectionCount;
    boolean showSectionMark;
    boolean autoAdjustSectionMark;
    boolean showSectionText;
    int sectionTextSize;
    int sectionTextColor;
    @KBubbleSeekBar.TextPosition
    int sectionTextPosition;
    int sectionTextInterval;
    boolean showThumbText;
    int thumbTextSize;
    int thumbTextColor;
    boolean showProgressInFloat;
    long animDuration;
    boolean touchToSeek;
    boolean seekStepSection;
    boolean seekBySection;
    int bubbleColor;
    int bubbleTextSize;
    int bubbleTextColor;
    boolean alwaysShowBubble;
    long alwaysShowBubbleDelay;
    boolean hideBubble;
    boolean rtl;

    private KBubbleSeekBar mBubbleSeekBar;

    KBubbleConfigBuilder(KBubbleSeekBar bubbleSeekBar) {
        mBubbleSeekBar = bubbleSeekBar;
    }

    public void build() {
        mBubbleSeekBar.config(this);
    }

    public KBubbleConfigBuilder min(float min) {
        this.min = min;
        this.progress = min;
        return this;
    }

    public KBubbleConfigBuilder max(float max) {
        this.max = max;
        return this;
    }

    public KBubbleConfigBuilder progress(float progress) {
        this.progress = progress;
        return this;
    }

    public KBubbleConfigBuilder floatType() {
        this.floatType = true;
        return this;
    }

    public KBubbleConfigBuilder trackSize(int dp) {
        this.trackSize = KBubbleUtils.dp2px(dp);
        return this;
    }

    public KBubbleConfigBuilder secondTrackSize(int dp) {
        this.secondTrackSize = KBubbleUtils.dp2px(dp);
        return this;
    }

    public KBubbleConfigBuilder thumbRadius(int dp) {
        this.thumbRadius = KBubbleUtils.dp2px(dp);
        return this;
    }

    public KBubbleConfigBuilder thumbRadiusOnDragging(int dp) {
        this.thumbRadiusOnDragging = KBubbleUtils.dp2px(dp);
        return this;
    }

    public KBubbleConfigBuilder trackColor(@ColorInt int color) {
        this.trackColor = color;
        this.sectionTextColor = color;
        return this;
    }

    public KBubbleConfigBuilder secondTrackColor(@ColorInt int color) {
        this.secondTrackColor = color;
        this.thumbColor = color;
        this.thumbTextColor = color;
        this.bubbleColor = color;
        return this;
    }

    public KBubbleConfigBuilder thumbColor(@ColorInt int color) {
        this.thumbColor = color;
        return this;
    }

    public KBubbleConfigBuilder sectionCount(@IntRange(from = 1) int count) {
        this.sectionCount = count;
        return this;
    }

    public KBubbleConfigBuilder showSectionMark() {
        this.showSectionMark = true;
        return this;
    }

    public KBubbleConfigBuilder autoAdjustSectionMark() {
        this.autoAdjustSectionMark = true;
        return this;
    }

    public KBubbleConfigBuilder showSectionText() {
        this.showSectionText = true;
        return this;
    }

    public KBubbleConfigBuilder sectionTextSize(int sp) {
        this.sectionTextSize = KBubbleUtils.sp2px(sp);
        return this;
    }

    public KBubbleConfigBuilder sectionTextColor(@ColorInt int color) {
        this.sectionTextColor = color;
        return this;
    }

    public KBubbleConfigBuilder sectionTextPosition(@KBubbleSeekBar.TextPosition int position) {
        this.sectionTextPosition = position;
        return this;
    }

    public KBubbleConfigBuilder sectionTextInterval(@IntRange(from = 1) int interval) {
        this.sectionTextInterval = interval;
        return this;
    }

    public KBubbleConfigBuilder showThumbText() {
        this.showThumbText = true;
        return this;
    }

    public KBubbleConfigBuilder thumbTextSize(int sp) {
        this.thumbTextSize = KBubbleUtils.sp2px(sp);
        return this;
    }

    public KBubbleConfigBuilder thumbTextColor(@ColorInt int color) {
        thumbTextColor = color;
        return this;
    }

    public KBubbleConfigBuilder showProgressInFloat() {
        this.showProgressInFloat = true;
        return this;
    }

    public KBubbleConfigBuilder animDuration(long duration) {
        animDuration = duration;
        return this;
    }

    public KBubbleConfigBuilder touchToSeek() {
        this.touchToSeek = true;
        return this;
    }

    public KBubbleConfigBuilder seekStepSection() {
        this.seekStepSection = true;
        return this;
    }

    public KBubbleConfigBuilder seekBySection() {
        this.seekBySection = true;
        return this;
    }

    public KBubbleConfigBuilder bubbleColor(@ColorInt int color) {
        this.bubbleColor = color;
        return this;
    }

    public KBubbleConfigBuilder bubbleTextSize(int sp) {
        this.bubbleTextSize = KBubbleUtils.sp2px(sp);
        return this;
    }

    public KBubbleConfigBuilder bubbleTextColor(@ColorInt int color) {
        this.bubbleTextColor = color;
        return this;
    }

    public KBubbleConfigBuilder alwaysShowBubble() {
        this.alwaysShowBubble = true;
        return this;
    }

    public KBubbleConfigBuilder alwaysShowBubbleDelay(long delay) {
        alwaysShowBubbleDelay = delay;
        return this;
    }

    public KBubbleConfigBuilder hideBubble() {
        this.hideBubble = true;
        return this;
    }

    public KBubbleConfigBuilder rtl(boolean rtl) {
        this.rtl = rtl;
        return this;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getProgress() {
        return progress;
    }

    public boolean isFloatType() {
        return floatType;
    }

    public int getTrackSize() {
        return trackSize;
    }

    public int getSecondTrackSize() {
        return secondTrackSize;
    }

    public int getThumbRadius() {
        return thumbRadius;
    }

    public int getThumbRadiusOnDragging() {
        return thumbRadiusOnDragging;
    }

    public int getTrackColor() {
        return trackColor;
    }

    public int getSecondTrackColor() {
        return secondTrackColor;
    }

    public int getThumbColor() {
        return thumbColor;
    }

    public int getSectionCount() {
        return sectionCount;
    }

    public boolean isShowSectionMark() {
        return showSectionMark;
    }

    public boolean isAutoAdjustSectionMark() {
        return autoAdjustSectionMark;
    }

    public boolean isShowSectionText() {
        return showSectionText;
    }

    public int getSectionTextSize() {
        return sectionTextSize;
    }

    public int getSectionTextColor() {
        return sectionTextColor;
    }

    public int getSectionTextPosition() {
        return sectionTextPosition;
    }

    public int getSectionTextInterval() {
        return sectionTextInterval;
    }

    public boolean isShowThumbText() {
        return showThumbText;
    }

    public int getThumbTextSize() {
        return thumbTextSize;
    }

    public int getThumbTextColor() {
        return thumbTextColor;
    }

    public boolean isShowProgressInFloat() {
        return showProgressInFloat;
    }

    public long getAnimDuration() {
        return animDuration;
    }

    public boolean isTouchToSeek() {
        return touchToSeek;
    }

    public boolean isSeekStepSection() {
        return seekStepSection;
    }

    public boolean isSeekBySection() {
        return seekBySection;
    }

    public int getBubbleColor() {
        return bubbleColor;
    }

    public int getBubbleTextSize() {
        return bubbleTextSize;
    }

    public int getBubbleTextColor() {
        return bubbleTextColor;
    }

    public boolean isAlwaysShowBubble() {
        return alwaysShowBubble;
    }

    public long getAlwaysShowBubbleDelay() {
        return alwaysShowBubbleDelay;
    }

    public boolean isHideBubble() {
        return hideBubble;
    }

    public boolean isRtl() {
        return rtl;
    }
}