
package com.example.soham_pc.whenexpires.ui;

import android.content.Context;

import com.example.soham_pc.whenexpires.ui.camera.BcodeGraphicOverlay;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private final BcodeGraphicOverlay<BarcodeGraphic> mBcodeGraphicOverlay;
    private final Context mContext;

    public BarcodeTrackerFactory(BcodeGraphicOverlay<BarcodeGraphic> mBcodeGraphicOverlay,
                                 Context mContext) {
        this.mBcodeGraphicOverlay = mBcodeGraphicOverlay;
        this.mContext = mContext;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mBcodeGraphicOverlay);
        return new BarcodeGraphicTracker(mBcodeGraphicOverlay, graphic, mContext);
    }

}

