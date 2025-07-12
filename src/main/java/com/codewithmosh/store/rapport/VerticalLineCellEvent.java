package com.codewithmosh.store.rapport;

import com.lowagie.text.pdf.*;
import com.lowagie.text.*;

class VerticalLineCellEvent implements PdfPCellEvent {
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];

        float x = (position.getLeft() + position.getRight()) / 2;
        float offset = 2f; // shift the line a bit to the right

        canvas.setLineWidth(1f); // Line thickness
        canvas.setGrayStroke(0.6f); // Semi-transparent gray

        canvas.moveTo(x + offset, position.getBottom());
        canvas.lineTo(x + offset, position.getTop());
        canvas.stroke();
    }
}
