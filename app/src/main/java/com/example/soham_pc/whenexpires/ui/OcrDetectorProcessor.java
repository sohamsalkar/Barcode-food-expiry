
package com.example.soham_pc.whenexpires.ui;

import android.util.SparseArray;

import com.example.soham_pc.whenexpires.ui.camera.OcrGraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;


public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private final OcrGraphicOverlay<OcrGraphic> mOcrGraphicOverlay;

    OcrDetectorProcessor(OcrGraphicOverlay<OcrGraphic> ocrOcrGraphicOverlay) {
        mOcrGraphicOverlay = ocrOcrGraphicOverlay;
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mOcrGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            OcrGraphic graphic = new OcrGraphic(mOcrGraphicOverlay, item);
            mOcrGraphicOverlay.add(graphic);
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mOcrGraphicOverlay.clear();
    }
}
